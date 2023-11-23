package com.example.demo.service.nz.page;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;

public class NzipotmComplaintsSearchTradeMarkPage {
    private final WebClient webClient;

    private HtmlPage searchTradeMarkPage;

    public NzipotmComplaintsSearchTradeMarkPage(WebClient webClient) {

        this.webClient = webClient;
    }

    public void openWebsite() {
        try {
            searchTradeMarkPage = webClient.getPage("https://app.iponz.govt.nz/app/Extra/Default.aspx?op=EXTRA_tm_qbe&fcoOp=EXTRA__Default&directAccess=true");
            webClient.waitForBackgroundJavaScript(5000);
        } catch (IOException e) {
            throw new RuntimeException("Page not found, check website");
        }
    }

    public void selectStatusAndSelect() {
        try {
            HtmlHeading4 htmlHeading= searchTradeMarkPage.getFirstByXPath(".//h4[@id='MainContent_ctrlTMSearch_hdrClassifStatusCriteria_header']");
            searchTradeMarkPage=htmlHeading.click();
            HtmlAnchor htmlAnchor=searchTradeMarkPage.getFirstByXPath(".//a[@id='MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_lnkBtnSearch']");
            searchTradeMarkPage= htmlAnchor.click();
            HtmlCheckBoxInput checkBoxInput = searchTradeMarkPage.getFirstByXPath(".//input[@id = 'MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_ctrlCaseStatusSearch_ctrlCaseStatusList_gvCaseStatuss_chckbxSelected_7']");
            checkBoxInput.click();

            checkBoxInput= searchTradeMarkPage.getFirstByXPath(".//input[@id='MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_ctrlCaseStatusSearch_ctrlCaseStatusList_gvCaseStatuss_chckbxSelected_8']");
            checkBoxInput.click();


            HtmlAnchor selectStatusButton=  searchTradeMarkPage.getFirstByXPath(".//a[@id='MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_lnkBtnSelect']");
            searchTradeMarkPage=selectStatusButton.click();

            Thread.sleep(10000);


        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public NzipotmComplaintsResultTradeMarkPage searchAndSort() {

        try {
            HtmlAnchor searchButton = searchTradeMarkPage.getFirstByXPath(".//a[@id = 'MainContent_ctrlTMSearch_lnkbtnSearch']");
            searchButton.click();
            Thread.sleep(10000);
            webClient.waitForBackgroundJavaScript(10000);
            HtmlAnchor sortingElement = searchTradeMarkPage.getFirstByXPath(".//a[contains(@href,'ctl00$MainContent$ctrlTMSearch$ctrlProcList$gvwIPCases')]");
            sortingElement.click();
            Thread.sleep(10000);
            return new NzipotmComplaintsResultTradeMarkPage(webClient, (HtmlPage) webClient.getCurrentWindow().getEnclosedPage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}
