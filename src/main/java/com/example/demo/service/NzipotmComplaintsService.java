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
    protected  WebClient webClient;

    @Autowired
    DownloadWriteHelper downloadWriteHelper;

    public static final BrowserVersion BROWSER_VERSION = BrowserVersion.EDGE;
    protected void initWebClient() {
        webClient =  new WebClient(BROWSER_VERSION);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setTimeout(150000);

    }

    public Binder runNzipotmComplaintsRobot() throws InterruptedException {
        initWebClient();
        NzipotmComplaintsResultTradeMarkPage resultPage=getResultPage();
        NzoIpoComplaintsResultPage finalPage=resultPage.clickResultPage();
        Binder binder= finalPage.fetchDataAndMap();
        finalPage.downloadPdf(binder);
        downloadWriteHelper.writeBinderToJsFile(binder);
        return binder;
    }

    private NzipotmComplaintsResultTradeMarkPage getResultPage() {
        NzipotmComplaintsSearchTradeMarkPage searchTradeMarkPage = new NzipotmComplaintsSearchTradeMarkPage(webClient);
        searchTradeMarkPage.openWebsite();
        searchTradeMarkPage.selectStatusAndSelect();
        return searchTradeMarkPage.searchAndSort();
    }


}
