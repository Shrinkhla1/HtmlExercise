package com.example.demo.service;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;

@Service
public class WebsiteReaderService {
    protected WebClient webClient;

    public static final BrowserVersion BROWSER_VERSION = BrowserVersion.FIREFOX;
    protected void initWebClient() {
        webClient = new WebClient(BROWSER_VERSION);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }
    public  Map<String, List<String>>  collectInformation()  {
        initWebClient();
        try {
            final HtmlPage page = webClient.getPage("http://www.clarivate.com/");
            if(page==null) {
                throw new RuntimeException("Page not found, check website!");
            }
            HtmlElement productsAndServicesLink = page.getFirstByXPath(".//a[@class='nav-link' and text()='Products & Services']");
            HtmlPage productPage = productsAndServicesLink.click();
            List<HtmlElement> listOfDropDown = productPage.getByXPath(".//div[@class='subnav-col border-left']");
            return createResponse(listOfDropDown);
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    private Map<String, List<String>> createResponse(List<HtmlElement> listOfDropDown) {
        Map<String, List<String>> productServicesMap = new LinkedHashMap<>();
        listOfDropDown.forEach(element -> {
            List<HtmlAnchor> htmlAnchor = element.getByXPath(".//a[contains(@href, 'https://clarivate.com/products/') or contains(., 'Consulting & Data')]");
            List<HtmlAnchor> filteredList = htmlAnchor.stream()
                    .filter(anchor -> !anchor.getTextContent().contains("arrow_forward\n")
                    ).toList();
            final List<String> subcategoryList = new ArrayList<>();
            String[] heading = {null};
            filteredList.forEach(anchorElement -> {String title = anchorElement.getTextContent().trim();
             if (!title.contains("arrow_forward")) {
                    subcategoryList.add(title);

                } else {
                    heading[0] = title.replace("arrow_forward", "");
                    if (!subcategoryList.isEmpty()) {
                        productServicesMap.put(heading[0],new ArrayList<>(subcategoryList));
                    subcategoryList.clear();
                }}

            });
            if (heading[0]!=null && !subcategoryList.isEmpty()) {
                productServicesMap.put(heading[0], new ArrayList<>(subcategoryList));
            }
        });
        return productServicesMap;
    }
}
