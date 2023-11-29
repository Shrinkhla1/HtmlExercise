package com.example.demo.service;

import com.example.demo.helper.DownloadWriteHelper;
import com.example.demo.model.Binder;
import com.example.demo.service.nz.page.NzipotmComplaintsResultTradeMarkPage;
import com.example.demo.service.nz.page.NzipotmComplaintsSearchTradeMarkPage;
import com.example.demo.service.nz.page.NzoIpoComplaintsResultPage;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class NzipotmComplaintsService {
    public static final BrowserVersion BROWSER_VERSION = BrowserVersion.EDGE;
    protected WebClient webClient;
    @Autowired
    DownloadWriteHelper downloadWriteHelper;

    protected void initWebClient() {
        webClient = new WebClient(BROWSER_VERSION);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(150000);

    }

    public Binder runNzipotmComplaintsRobot() throws InterruptedException {
        initWebClient();
        NzipotmComplaintsResultTradeMarkPage searchTradeMarkCasePage = searchTradeMarkCases();
        NzoIpoComplaintsResultPage caseDetailsPage = searchTradeMarkCasePage.fetchCaseDetails();
        Binder binder = caseDetailsPage.fetchCaseDataAndMapToBinder();
        caseDetailsPage.downloadPdf(binder);
        downloadWriteHelper.writeBinderToJsFile(binder);
        return binder;
    }

    private NzipotmComplaintsResultTradeMarkPage searchTradeMarkCases() {
        NzipotmComplaintsSearchTradeMarkPage searchTradeMarkPage = new NzipotmComplaintsSearchTradeMarkPage(webClient);
        searchTradeMarkPage.openWebsite();
        searchTradeMarkPage.selectCaseStatusAndSelect();
        return searchTradeMarkPage.searchAndSort();
    }

}
