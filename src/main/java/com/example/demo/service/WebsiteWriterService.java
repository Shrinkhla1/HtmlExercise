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

    private static final String URL = "https://leetcode.com/";

    private static final String helloWorldCode = "class HelloWorld { public static void main(String[] args) {System.out.println(\"Hello, World!\"); }}";

    private WebDriver initWebClient() {
        WebDriverManager.chromedriver().setup();
        WebDriver webDriver = new ChromeDriver();
        webDriver.manage().window().maximize();
        return webDriver;
    }

    public String getOutputAfterExecution() {
        WebDriver webDriver = initWebClient();
        webDriver.get(URL);

        WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(100));
        scrollThePage(webDriver);
        selectJavaButtonAndClick(wait, webDriver);
        String computeOutput = writeInEditorAndExtractResult(wait, webDriver);

        webDriver.quit();
        return computeOutput;
    }

    private String writeInEditorAndExtractResult(WebDriverWait wait, WebDriver webDriver) {
        writeCodeInEditor(wait, webDriver);

        WebElement runButton = wait.until(ExpectedConditions.
                presenceOfElementLocated(By.xpath(".//button[@class='btn btn-success run-code-btn']")));
        runButton.click();

        WebElement computeOutput = wait.until(ExpectedConditions.
                presenceOfElementLocated(By.className("output")));

        return computeOutput.getText().trim();
    }

    private void writeCodeInEditor(WebDriverWait wait, WebDriver webDriver) {
        WebElement codeTextArea = wait.until(ExpectedConditions.
                elementToBeClickable(By.xpath(".//div[@class='CodeMirror-lines']")));
        Actions actions = new Actions(webDriver);
        actions.moveToElement(codeTextArea).click().build().perform();
        actions.keyDown(Keys.CONTROL).sendKeys("a").keyUp(Keys.CONTROL).sendKeys(Keys.DELETE).build().perform();
        actions.sendKeys(helloWorldCode).build().perform();
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }

    private void selectJavaButtonAndClick(WebDriverWait wait, WebDriver webDriver) {
        WebElement javaButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(".//button[text()='Java']")));
        webDriver.manage().timeouts().implicitlyWait(Duration.ofSeconds(50));
        javaButton.click();
    }

    private void scrollThePage(WebDriver webDriver) {
        JavascriptExecutor js = (JavascriptExecutor) webDriver;
        js.executeScript("window.scrollBy(0,1500);");
        webDriver.switchTo().frame(0);
    }
}
