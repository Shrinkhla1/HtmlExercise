package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

public class Binder {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("domains")
    private Set<String> domains;
    @JsonProperty("firstAction")
    private FirstAction firstAction;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @JsonProperty("firstActionDate")
    private LocalDate firstActionDate;
    @JsonProperty("dockets")
    private Docket dockets;
    @JsonProperty("parties")
    private Set<Party> parties;
    @JsonProperty("rights")
    private Set<Right> rights;
    @JsonProperty("decisions")
    private Decisions decisions;

    @JsonProperty("applicantDetails")
    private ApplicantDetails applicantDetails;

    public Binder() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<String> getDomains() {
        return domains;
    }

    public void setDomains(Set<String> domains) {
        this.domains = domains;
    }

    public FirstAction getFirstAction() {
        return firstAction;
    }

    public void setFirstAction(FirstAction firstAction) {
        this.firstAction = firstAction;
    }

    public LocalDate getFirstActionDate() {
        return firstActionDate;
    }

    public void setFirstActionDate(LocalDate firstActionDate) {
        this.firstActionDate = firstActionDate;
    }

    public Docket getDockets() {
        return dockets;
    }

    public void setDockets(Docket dockets) {
        this.dockets = dockets;
    }

    public Set<Party> getParties() {
        return parties;
    }

    public void setParties(Set<Party> parties) {
        this.parties = parties;
    }

    public Set<Right> getRights() {
        return rights;
    }

    public void setRights(Set<Right> rights) {
        this.rights = rights;
    }

    public Decisions getDecisions() {
        return decisions;
    }

    public void setDecisions(Decisions decisions) {
        this.decisions = decisions;
    }

    public ApplicantDetails getApplicantDetails() {
        return applicantDetails;
    }

    public void setApplicantDetails(ApplicantDetails applicantDetails) {
        this.applicantDetails = applicantDetails;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Binder binder = (Binder) o;
        return Objects.equals(id, binder.id) && Objects.equals(domains, binder.domains) && firstAction == binder.firstAction &&
               Objects.equals(firstActionDate, binder.firstActionDate) && Objects.equals(dockets, binder.dockets) && Objects.equals(parties, binder.parties) &&
               Objects.equals(rights, binder.rights) && Objects.equals(decisions, binder.decisions) &&
               Objects.equals(applicantDetails, binder.applicantDetails);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, domains, firstAction, firstActionDate, dockets, parties, rights, decisions, applicantDetails);
    }

    @Override
    public String toString() {
        return "Binder{" +
               "id=" + id +
               ", domains=" + domains +
               ", firstAction=" + firstAction +
               ", firstActionDate=" + firstActionDate +
               ", dockets=" + dockets +
               ", parties=" + parties +
               ", rights=" + rights +
               ", decisions=" + decisions +
               ", applicantDetails=" + applicantDetails +
               '}';
    }
}
