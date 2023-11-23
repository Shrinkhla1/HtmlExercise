package com.example.demo.service.nz.page;

import com.example.demo.helper.DownloadWriteHelper;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;

public class NzipotmComplaintsResultTradeMarkPage {
    private final WebClient webClient;
    private final HtmlPage resultTradeMarkPage;

    public NzipotmComplaintsResultTradeMarkPage(WebClient webClient,HtmlPage resultTradeMarkPage) {
        this.webClient = webClient;
        this.resultTradeMarkPage = resultTradeMarkPage;
    }

    public NzoIpoComplaintsResultPage clickResultPage() {
        try {
            HtmlTable resultTable = resultTradeMarkPage.getFirstByXPath(".//table[@id='MainContent_ctrlTMSearch_ctrlProcList_gvwIPCases']");
            HtmlAnchor anchor = resultTable.getFirstByXPath(".//a[@id='MainContent_ctrlTMSearch_ctrlProcList_gvwIPCases_lnkBtnCaseBrowser_0']");
            anchor.click();
            Thread.sleep(5000);
            webClient.waitForBackgroundJavaScript(20000);
            return new NzoIpoComplaintsResultPage(webClient, (HtmlPage) webClient.getCurrentWindow().getEnclosedPage(), new DownloadWriteHelper());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
