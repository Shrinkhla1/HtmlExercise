package com.example.demo.service.nz.page;

import com.example.demo.helper.ErrorCodeException;
import com.example.demo.model.ErrorCode;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlCheckBoxInput;
import com.gargoylesoftware.htmlunit.html.HtmlHeading4;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;

public class NzipotmComplaintsSearchTradeMarkPage {
    private static final String URL = "https://app.iponz.govt.nz/app/Extra/Default.aspx?op=EXTRA_tm_qbe&fcoOp=EXTRA__Default&directAccess=true";
    private final WebClient webClient;
    private HtmlPage searchTradeMarkPage;

    public NzipotmComplaintsSearchTradeMarkPage(WebClient webClient) {

        this.webClient = webClient;
    }

    public void openWebsite() {
        try {
            searchTradeMarkPage = webClient.getPage(URL);
            webClient.waitForBackgroundJavaScript(5000);
        } catch (IOException e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_01);
        }
    }

    public void selectCaseStatusAndSelect() {
        try {
            HtmlHeading4 statusSearchButton = searchTradeMarkPage.getFirstByXPath(".//h4[@id='MainContent_ctrlTMSearch_hdrClassifStatusCriteria_header']");
            searchTradeMarkPage = statusSearchButton.click();
            HtmlAnchor selectCaseStatusButton = searchTradeMarkPage.getFirstByXPath(".//a[@id='MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_lnkBtnSearch']");
            searchTradeMarkPage = selectCaseStatusButton.click();

            HtmlCheckBoxInput underOppositionButton = searchTradeMarkPage.getFirstByXPath(".//input[@id = 'MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_ctrlCaseStatusSearch_ctrlCaseStatusList_gvCaseStatuss_chckbxSelected_7']");
            underOppositionButton.click();

            underOppositionButton = searchTradeMarkPage.getFirstByXPath(".//input[@id='MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_ctrlCaseStatusSearch_ctrlCaseStatusList_gvCaseStatuss_chckbxSelected_8']");
            underOppositionButton.click();


            HtmlAnchor selectStatusButton = searchTradeMarkPage.getFirstByXPath(".//a[@id='MainContent_ctrlTMSearch_ctrlCaseStatusSearchDialog_lnkBtnSelect']");
            searchTradeMarkPage = selectStatusButton.click();

            Thread.sleep(10000);

        } catch (Exception e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_02);
        }
    }

    public NzipotmComplaintsResultTradeMarkPage searchAndSort() {

        try {
            HtmlAnchor searchButton = searchTradeMarkPage.getFirstByXPath(".//a[@id = 'MainContent_ctrlTMSearch_lnkbtnSearch']");
            searchButton.click();
            Thread.sleep(10000);
            webClient.waitForBackgroundJavaScript(10000);
            HtmlAnchor sortingButton = searchTradeMarkPage.getFirstByXPath(".//a[contains(@href,'ctl00$MainContent$ctrlTMSearch$ctrlProcList$gvwIPCases')]");
            sortingButton.click();
            Thread.sleep(10000);
            return new NzipotmComplaintsResultTradeMarkPage(webClient, (HtmlPage) webClient.getCurrentWindow().getEnclosedPage());
        } catch (Exception e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_02);
        }

    }
}
