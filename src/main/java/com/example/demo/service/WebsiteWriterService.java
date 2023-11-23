package com.example.demo.service;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;
import java.time.Duration;

@Service
public class WebsiteWriterService {

    protected WebDriver initWebClient() {
        WebDriverManager.chromedriver().setup();
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        return webDriver;
    }
    public String getOutputAfterExecution() {
        WebDriver webDriver =initWebClient();
        JavascriptExecutor js = (JavascriptExecutor) webDriver;

        WebDriverWait wait = new WebDriverWait(webDriver,Duration.ofSeconds(100));
        webDriver.get("https://leetcode.com/");

        scrollThePage(js,webDriver);
        selectJavaButtonAndClick(wait,webDriver);

        String finalResult=writeInEditorAndExtractResult(wait,webDriver);

        webDriver.quit();
        return finalResult;
    }

    private String writeInEditorAndExtractResult(WebDriverWait wait, WebDriver webDriver) {
        String script = "class HelloWorld { public static void main(String[] args) {System.out.println(\"Hello, World!\"); }}";
        WebElement codeTextArea = wait.until(ExpectedConditions.
                elementToBeClickable(By.xpath(".//div[@class='CodeMirror-lines']")));

        Actions actions= new Actions(webDriver);
        actions.moveToElement(codeTextArea).click().build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).build().perform();
        actions.sendKeys(script).build().perform();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

        WebElement runButton = wait.until(ExpectedConditions.
                presenceOfElementLocated(By.xpath(".//button[@class='btn btn-success run-code-btn']")));
        runButton.click();

        WebElement output = wait.until(ExpectedConditions.
                presenceOfElementLocated(By.className("output")));

        return output.getText();
    }

    private void selectJavaButtonAndClick(WebDriverWait wait,WebDriver webDriver) {
        WebElement javaButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//button[text()='Java']")));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
        javaButton.click();
    }

    private void scrollThePage(JavascriptExecutor js, WebDriver webDriver) {
        js.executeScript("window.scrollBy(0,1500);");
        webDriver.switchTo().frame(0);
    }
}
