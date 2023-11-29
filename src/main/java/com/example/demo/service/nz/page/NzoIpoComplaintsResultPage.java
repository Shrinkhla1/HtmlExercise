package com.example.demo.service.nz.page;

import com.example.demo.helper.DownloadWriteHelper;
import com.example.demo.helper.ErrorCodeException;
import com.example.demo.model.*;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class NzoIpoComplaintsResultPage {
    public static final Long IP_NEW_ZEALAND_COURT_ID = 1845L;
    public static final String UNIQUE_IDENTIFIER = "NZ-Ipo-Complaints";
    public static final String COMPLAINT_EXTENSION = "_Complaint_IS";
    public static final String UNIQUE_NAME="nz-nzipotm-op-";
    private static final String OPPOSITION_PROCEEDING_COMMENCED = "Opposition proceeding commenced";
    private static final String PROCEEDINGS="Proceedings";
    private static final String MARK_TYPE="MainContent_ctrlTM_trTMType";
    private static final String MARK_NAME="MainContent_ctrlTM_trTMName";
    private static final String MARK_PICTURE="MainContent_ctrlTM_trPicture";
    private static final String DIRECTORY_PATH = "C:\\Users\\U6075110\\OneDrive - Clarivate Analytics\\Documents\\bots";
    private static final  String FILE_NAME = "Tm_Image" + ".jpg";
    private static final DateTimeFormatter ORIGINAL_DATE_FORMAT=DateTimeFormatter.ofPattern("d MMM yyyy");
    private static final DateTimeFormatter DATE_FORMAT=DateTimeFormatter.ofPattern("yyyyMMdd");
    private final WebClient webClient;
    private final HtmlPage caseDetailsPage;
    private final DownloadWriteHelper downloadWriteHelper;

    public NzoIpoComplaintsResultPage(WebClient webClient, HtmlPage caseDetailsPage, DownloadWriteHelper downloadWriteHelper) {
        this.webClient = webClient;
        this.caseDetailsPage = caseDetailsPage;
        this.downloadWriteHelper = downloadWriteHelper;
    }

    public void getIPNumber(Binder binder) throws InterruptedException {
        Thread.sleep(1000);
        HtmlSpan ipNumberSpan = caseDetailsPage.getFirstByXPath(".//span[@id='MainContent_ctrlTM_txtAppNr']");
        binder.setId(Long.valueOf(ipNumberSpan.getTextContent().trim()));
    }

    public Binder fetchCaseDataAndMapToBinder() throws InterruptedException {
        HtmlTable caseDetailsTable = caseDetailsPage.getFirstByXPath(".//table[@class='layout']");
        HtmlTableBody caseDetailsBody = caseDetailsTable.getFirstByXPath(".//tbody");
        Binder binder = new Binder();
        getIPNumber(binder);
        setDomains(binder);
        getApplicantDetails(caseDetailsBody, binder);
        setRights(binder);
        Classification classification = getClassNumber();
        getMarkInformation(caseDetailsBody, classification, binder);
        getRedPartyDetails(binder);
        clickHistoryTabAndGetDetails(binder);
        String firstActionDate = binder.getFirstActionDate().format(DATE_FORMAT);
        String reference = StringUtils.join(UNIQUE_NAME, binder.getId(), "_", firstActionDate);
        setDecisions(binder, reference);
        setDockets(binder, reference);
        return binder;
    }

    private void setRights(Binder binder) {
        Right right = new Right();
        right.setId(binder.getId());
        right.setOpponent(Boolean.TRUE);
        Set<Right> rightSet = Set.of(right);
        binder.setRights(rightSet);
    }

    private void setDomains(Binder binder) {
        Set<String> domains = Set.of("TM", "CR", "DM", "PT");
        binder.setDomains(domains);
    }

    private void clickHistoryTabAndGetDetails(Binder binder) {
        try {
            Thread.sleep(10000);
            HtmlAnchor historyClick = caseDetailsPage.getFirstByXPath(".//a[@id='ui-id-2']");
            HtmlPage historyPage = (HtmlPage) historyClick.click().getEnclosingWindow().getEnclosedPage();
            List<HtmlTableRow> historyRows = historyPage.getByXPath(".//table[@id = 'MainContent_ctrlHistoryList_gvHistory']//tbody//tr");
            for (HtmlTableRow historyDetails : historyRows) {
                String historyType = historyDetails.getCell(0).getTextContent().trim();
                if (StringUtils.equals(historyType, OPPOSITION_PROCEEDING_COMMENCED)) {
                    LocalDate firstActionDate = LocalDate.parse(historyDetails.getCell(2).getTextContent().trim(),ORIGINAL_DATE_FORMAT);
                    String formatFirstActionDate = firstActionDate.format(DATE_FORMAT);
                    binder.setFirstActionDate(LocalDate.parse(formatFirstActionDate, DATE_FORMAT));
                    binder.setFirstAction(FirstAction.OPPOSITION);
                }
            }
        } catch (Exception e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_02);
        }
    }

    private void getRedPartyDetails(Binder binder) throws InterruptedException {

        Thread.sleep(10000);
        List<HtmlTableRow> partyRows = caseDetailsPage.getByXPath(".//table[@id = 'MainContent_ctrlProcedureList_gvwIPCases']//tbody//tr");
        Set<Party> partyList = new HashSet<>();
        for (HtmlTableRow party : partyRows) {
            Party partyObject = new Party();
            String status = party.getCell(1).getTextContent().trim();
            if (StringUtils.contains(status, PROCEEDINGS)) {
                String partyName = party.getCell(6).getTextContent().trim();
                if (StringUtils.isNotBlank(partyName) && !StringUtils.equals(partyName, "N/A")) {
                    partyObject.setName(partyName);
                    partyObject.setType(status);
                }
                partyList.add(partyObject);
            }
            binder.setParties(partyList);
        }
    }

    private void setDecisions(Binder binder, String reference) {
        Decisions decisions = new Decisions();
        decisions.setId(binder.getId());
        decisions.setLevel(String.valueOf(DecisionLevel.FIRST_INSTANCE));
        decisions.setNature(List.of(String.valueOf(RecordNature.COMPLAINT), String.valueOf(RecordNature.HEARING)));
        decisions.setRobotSource(UNIQUE_IDENTIFIER);
        decisions.setReference(StringUtils.join(reference, COMPLAINT_EXTENSION));
        decisions.setJudgementDate(binder.getFirstActionDate());
        binder.setDecisions(decisions);
    }

    private void setDockets(Binder binder, String reference) {

        Docket docket = new Docket();
        docket.setId(binder.getId());
        docket.setCourtId(IP_NEW_ZEALAND_COURT_ID);
        docket.setReference(reference);
        binder.setDockets(docket);
    }

    private void getMarkInformation(HtmlTableBody caseDetailsBody, Classification classification, Binder binder) throws InterruptedException {

        Thread.sleep(10000);
        webClient.waitForBackgroundJavaScript(10000);
        List<HtmlTableRow> markInformationRowsList = caseDetailsBody.getByXPath(".//*[@id=\"MainContent_ctrlTM_ctl30\"]/div/table//tbody//tr");

        markInformationRowsList.stream()
                .filter(markInformationRows -> MARK_TYPE.equals(markInformationRows.getAttribute("id"))
                               || MARK_NAME.equals(markInformationRows.getAttribute("id"))
                               || MARK_PICTURE.equals(markInformationRows.getAttribute("id")))
                .forEach(markInformationRows -> {
                    HtmlTableCell markInformationData = markInformationRows.getFirstByXPath(".//td[@class='data break-word']");

                    if (MARK_TYPE.equals(markInformationRows.getAttribute("id"))) {
                        classification.setType(markInformationData.getTextContent().trim());
                    } else if (MARK_NAME.equals(markInformationRows.getAttribute("id"))) {
                        classification.setName(markInformationData.getTextContent().trim());
                    } else if (MARK_PICTURE.equals(markInformationRows.getAttribute("id"))) {
                        classification.setImage(getImage(markInformationData));
                    }

                    binder.getRights().forEach(right -> right.setClassification(classification));
                });
    }

    private String getImage(HtmlTableCell markInformationData) {
        HtmlAnchor markPictureAnchor = markInformationData.getFirstByXPath(".//a[@id='MainContent_ctrlTM_ctrlPictureList_lvDocumentView_hlnkCasePicture_0']");
        webClient.waitForBackgroundJavaScript(1000);
        String imageUrl = markPictureAnchor.getAttribute("thmb");
        downloadWriteHelper.writeContentToSystem(DIRECTORY_PATH, FILE_NAME, imageUrl);
        try {
            Path imageFile = Paths.get(DIRECTORY_PATH, FILE_NAME);
            byte[] imageBytes = Files.readAllBytes(imageFile);
            return Base64.getEncoder().encodeToString(imageBytes);
        } catch (IOException ex) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_03);
        }
    }


    private Classification getClassNumber() {
        Classification classification = new Classification();
        HtmlTableCell classNumberCell = caseDetailsPage.getFirstByXPath("//*[@id=\"MainContent_ctrlTM_ctrlClassif_gvClassifications\"]/tbody/tr[2]/td[1]");
        classification.setClassId(Long.valueOf(classNumberCell.getTextContent().trim()));
        return classification;
    }

    private void getApplicantDetails(HtmlTableBody body, Binder binder) {
        webClient.waitForBackgroundJavaScript(10000);
        HtmlTable applicantTable = body.getFirstByXPath("//*[@id=\"MainContent_ctrlTM_ctrlApplicant_UpdatePanel1\"]/table");
        HtmlTable applicantDataTable = applicantTable.getFirstByXPath(".//table[@id='MainContent_ctrlTM_ctrlApplicant_ctrlApplicant_gvCustomers']");
        applicantDataTable.getBodies().forEach(applicantData ->
                applicantData.getRows().stream()
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
            HtmlAnchor documentsClick = caseDetailsPage.getFirstByXPath(".//a[@id='ui-id-3']");
            HtmlPage documentsPage = (HtmlPage) documentsClick.click().getEnclosingWindow().getEnclosedPage();
            HtmlAnchor downloadButton = documentsPage.getFirstByXPath("//*[@id=\"MainContent_ctrlDocumentList_gvDocuments_hnkView_0\"]");
            String pdfUrl = downloadButton.getAttribute("href");
            downloadWriteHelper.downloadPdf(pdfUrl, DIRECTORY_PATH,
                    binder.getDecisions().getReference() + ".pdf");
        } catch (IOException e) {
            throw new ErrorCodeException(ErrorCode.ERROR_CODE_02);
        }
    }
}
