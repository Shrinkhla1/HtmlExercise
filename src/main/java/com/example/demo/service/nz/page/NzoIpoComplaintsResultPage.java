package com.example.demo.service.nz.page;

import com.example.demo.helper.DownloadWriteHelper;
import com.example.demo.model.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class NzoIpoComplaintsResultPage {
    private final WebClient webClient;
    private final HtmlPage resultPage;

    private static final String OPPOSITION_PROCEEDING_COMMENCED = "Opposition proceeding commenced";

    public static final Long IP_NEW_ZEALAND_COURT_ID = 1845L;

    public static final String UNIQUE_IDENTIFIER = "NZ-Ipo-Complaints";

    public static final String COMPLAINT_EXTENSION = "_Complaint_IS";

    private final DownloadWriteHelper downloadWriteHelper;

    public NzoIpoComplaintsResultPage(WebClient webClient, HtmlPage resultPage,DownloadWriteHelper downloadWriteHelper) {
        this.webClient = webClient;
        this.resultPage = resultPage;
        this.downloadWriteHelper= downloadWriteHelper;
    }

    public void getIPNumber(Binder binder) throws InterruptedException {
        Thread.sleep(1000);
        HtmlSpan span = resultPage.getFirstByXPath(".//span[@id='MainContent_ctrlTM_txtAppNr']");
        binder.setId(Long.valueOf(span.getTextContent()));
    }

    public Binder fetchDataAndMap() throws InterruptedException {
        HtmlTable mainTable = resultPage.getFirstByXPath(".//table[@class='layout']");
        HtmlTableBody mainTBody = mainTable.getFirstByXPath(".//tbody");
        Binder binder= new Binder();
        getIPNumber(binder);
        setDomainField(binder);
        fetchApplicantData(mainTBody,binder);
        setRightsField(binder);
        Classification classification=fetchClassNo();
        fetchMarkInformation(mainTBody,classification,binder);
        fetchRedParty(binder);
        clickHistoryAndFetchDetails(binder);
        String firstActionDate = binder.getFirstActionDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String reference=StringUtils.join("nz-nzipotm-op-", binder.getId(), "_", firstActionDate);
        mapDecisions(binder,reference);
        setDocketFields(binder,reference);
        return binder;
    }

    private void setRightsField(Binder binder) {
        Right right = new Right();
        right.setId(binder.getId());
        right.setOpponent(true);
        Set<Right> rightSet = Set.of(right);
        binder.setRights(rightSet);
    }

    private void setDomainField(Binder binder) {
        Set<String> domains = Set.of("TM", "CR", "DM", "PT");
        binder.setDomains(domains);
    }

    private void clickHistoryAndFetchDetails(Binder binder) {
        try {
            Thread.sleep(10000);
            HtmlAnchor historyClick = resultPage.getFirstByXPath(".//a[@id='ui-id-2']");
            HtmlPage historyPage = (HtmlPage) historyClick.click().getEnclosingWindow().getEnclosedPage();
            List<HtmlTableRow> rows = historyPage.getByXPath(".//table[@id = 'MainContent_ctrlHistoryList_gvHistory']//tbody//tr");
            for (HtmlTableRow row : rows) {
                String text = row.getCell(0).getTextContent().trim();
                if (StringUtils.equals(text, OPPOSITION_PROCEEDING_COMMENCED)) {
                    LocalDate date = LocalDate.parse(row.getCell(2).getTextContent().trim(), DateTimeFormatter.ofPattern("d MMM yyyy"));
                    String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
                    binder.setFirstActionDate(LocalDate.parse(formattedDate,DateTimeFormatter.ofPattern("yyyyMMdd")));
                    binder.setFirstAction(FirstAction.OPPOSITION);
                }
            }
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private void fetchRedParty(Binder binder) throws InterruptedException {

        Thread.sleep(10000);
        List<HtmlTableRow> rows = resultPage.getByXPath(".//table[@id = 'MainContent_ctrlProcedureList_gvwIPCases']//tbody//tr");
        Set<Party> partyList = new HashSet<>();
        for (HtmlTableRow row : rows) {
            Party partyObject = new Party();
            String status = row.getCell(1).getTextContent().trim();
            if (StringUtils.contains(status, "Proceedings")) {
                String partyName = row.getCell(6).getTextContent().trim();
                if (StringUtils.isNotBlank(partyName) && !StringUtils.equals(partyName, "N/A")) {
                    partyObject.setName(partyName);
                    partyObject.setType(status);
                }
                partyList.add(partyObject);
            }
            binder.setParties(partyList);
        }}

    private void mapDecisions(Binder binder,String reference) {
        Decisions decisions  = new Decisions();
        decisions.setId(binder.getId());
        decisions.setLevel(String.valueOf(DecisionLevel.FIRST_INSTANCE));
        decisions.setNature(List.of(String.valueOf(RecordNature.COMPLAINT),String.valueOf(RecordNature.HEARING)));
        decisions.setRobotSource(UNIQUE_IDENTIFIER);
        decisions.setReference(StringUtils.join(reference, COMPLAINT_EXTENSION));
        decisions.setJudgementDate(binder.getFirstActionDate());
        binder.setDecisions(decisions);
    }

    private void setDocketFields(Binder binder,String reference) {

        Docket docket = new Docket();
        docket.setId(binder.getId());
        docket.setCourtId(IP_NEW_ZEALAND_COURT_ID);
        docket.setReference(reference);
        binder.setDockets(docket);
    }

    private void fetchMarkInformation(HtmlTableBody body,Classification classification,Binder binder) throws InterruptedException {

            Thread.sleep(10000);
            webClient.waitForBackgroundJavaScript(10000);
            List<HtmlTableRow> markInformationRows = body.getByXPath(".//*[@id=\"MainContent_ctrlTM_ctl30\"]/div/table//tbody//tr");

            markInformationRows.stream()
                    .filter(row -> "MainContent_ctrlTM_trTMType".equals(row.getAttribute("id"))
                            || "MainContent_ctrlTM_trTMName".equals(row.getAttribute("id"))
                            || "MainContent_ctrlTM_trPicture".equals(row.getAttribute("id")))
                    .forEach(row -> {
                        HtmlTableCell cell = row.getFirstByXPath(".//td[@class='data break-word']");

                        if ("MainContent_ctrlTM_trTMType".equals(row.getAttribute("id"))) {
                            classification.setType(cell.getTextContent().trim());
                        } else if ("MainContent_ctrlTM_trTMName".equals(row.getAttribute("id"))) {
                            classification.setName(cell.getTextContent().trim());
                        }
                        else if ("MainContent_ctrlTM_trPicture".equals(row.getAttribute("id"))) {
                            HtmlAnchor anchor = cell.getFirstByXPath(".//a[@id='MainContent_ctrlTM_ctrlPictureList_lvDocumentView_hlnkCasePicture_0']");
                            webClient.waitForBackgroundJavaScript(1000);
                            String imageUrl = anchor.getAttribute("thmb");
                            String directoryPath = "C:\\Users\\U6075110\\OneDrive - Clarivate Analytics\\Documents\\bots";
                            String fileName = "Tm_Image"+".jpg";
                            downloadWriteHelper.writeContentToSystem(directoryPath, fileName, imageUrl);
                            try {
                                Path imageFile = Paths.get(directoryPath, fileName);
                                byte[] imageBytes = Files.readAllBytes(imageFile);
                                classification.setImage(Base64.getEncoder().encodeToString(imageBytes));
                            } catch (IOException ex) {
                                throw new RuntimeException(ex);
                            }

                        }

                        binder.getRights().forEach(right -> right.setClassification(classification));
                    });
        }





    private Classification fetchClassNo() {
        Classification classification= new Classification();
        HtmlTableCell classNumberCell = resultPage.getFirstByXPath("//*[@id=\"MainContent_ctrlTM_ctrlClassif_gvClassifications\"]/tbody/tr[2]/td[1]");
        classification.setClassId(Long.valueOf(classNumberCell.getTextContent().trim()));
        return classification;
    }

    private void fetchApplicantData(HtmlTableBody body,Binder binder) {
        webClient.waitForBackgroundJavaScript(10000);
        HtmlTable applicantTable=body.getFirstByXPath("//*[@id=\"MainContent_ctrlTM_ctrlApplicant_UpdatePanel1\"]/table");
        HtmlTable applicantDataTable=applicantTable.getFirstByXPath(".//table[@id='MainContent_ctrlTM_ctrlApplicant_ctrlApplicant_gvCustomers']");
        applicantDataTable.getBodies().forEach(tbody ->
                tbody.getRows().stream()
                        .filter(row -> row.getAttribute("class").equals("alt1"))
                        .forEach(row -> {
                                ApplicantDetails applicantDetails = new ApplicantDetails();
                                List<HtmlTableCell> cells = row.getCells();
                                if (cells.size() >= 3) {
                                    applicantDetails.setId(Long.valueOf(cells.get(0).getTextContent().trim()));
                                    applicantDetails.setName(cells.get(1).getTextContent().trim());
                                    applicantDetails.setAddress(cells.get(2).getTextContent().trim());
                                }

                                binder.setApplicantDetails(applicantDetails);
                            }));


    }

    public void downloadPdf(Binder binder) {
        try {
            HtmlAnchor documentsClick = resultPage.getFirstByXPath(".//a[@id='ui-id-3']");
            HtmlPage documentsPage = (HtmlPage) documentsClick.click().getEnclosingWindow().getEnclosedPage();
            HtmlAnchor anchor= documentsPage.getFirstByXPath("//*[@id=\"MainContent_ctrlDocumentList_gvDocuments_hnkView_0\"]");
            String pdfUrl = anchor.getAttribute("href");
            downloadWriteHelper.downloadPdf(pdfUrl, "C:\\Users\\U6075110\\OneDrive - Clarivate Analytics\\Documents\\bots",
                    binder.getDecisions().getReference()+".pdf");
        }
        catch(IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
