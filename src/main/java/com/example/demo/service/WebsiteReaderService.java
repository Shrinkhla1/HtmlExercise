package com.example.demo.service;

import com.example.demo.helper.ErrorCodeException;
import com.example.demo.model.ErrorCode;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class WebsiteReaderService {
    private static final BrowserVersion BROWSER_VERSION = BrowserVersion.FIREFOX;
    private static final String URL = "http://www.clarivate.com/";

    private static final String ARROW_FORWARD = "arrow_forward";
    private WebClient webClient;

    private void initWebClient() {
        webClient = new WebClient(BROWSER_VERSION);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setCssEnabled(false);
    }

    public Map<String, List<String>> collectInformation() {
        initWebClient();
        try {
            final HtmlPage page = webClient.getPage(URL);
            DomElement menuElement = page.getElementById("menu-item-7");
            DomElement productLink = menuElement.getFirstElementChild();
            HtmlPage productPage = productLink.click();
            List<HtmlElement> allProductDataList = productPage.getByXPath(".//div[@class='subnav-col border-left']");
            return createResponse(allProductDataList);
        } catch (Exception e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_01);
        }
    }

    private Map<String, List<String>> createResponse(List<HtmlElement> allProductDataList) {
        Map<String, List<String>> productHeaderDescriptionMap = new LinkedHashMap<>();

        allProductDataList.forEach(allProductData -> {
            List<HtmlAnchor> productDataList =
                    allProductData.getByXPath(".//a[contains(@href, 'https://clarivate.com/products/') or contains(., 'Consulting & Data')]");
            if (!productDataList.isEmpty()) {
                productDataList.forEach(productData -> {
                    createProductHeaderDescriptionMap(productData, productHeaderDescriptionMap);

                });
            }
        });
        return productHeaderDescriptionMap;
    }

    private void createProductHeaderDescriptionMap(HtmlAnchor productData, Map<String, List<String>> productHeaderDescriptionMap) {
        String dataToBeProcessed = productData.getTextContent().trim();
        if (dataToBeProcessed.contains(ARROW_FORWARD)) {
            productHeaderDescriptionMap.put(dataToBeProcessed.replace(ARROW_FORWARD, ""),
                    new ArrayList<>());
        } else {
            productHeaderDescriptionMap.get(productHeaderDescriptionMap.keySet().toArray()
                    [productHeaderDescriptionMap.size() - 1]).add(dataToBeProcessed);
        }
    }
}
