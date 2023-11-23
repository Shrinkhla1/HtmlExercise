package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Docket {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("reference")
    private String reference;
    @JsonProperty("courtId")
    private Long courtId;
    @JsonProperty("judge")
    private String judge;

    public Docket() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Long getCourtId() {
        return courtId;
    }

    public void setCourtId(Long courtId) {
        this.courtId = courtId;
    }

    public String getJudge() {
        return judge;
    }

    public void setJudge(String judge) {
        this.judge = judge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Docket docket = (Docket) o;
        return Objects.equals(id, docket.id) && Objects.equals(reference, docket.reference) && Objects.equals(courtId, docket.courtId) && Objects.equals(judge, docket.judge);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, courtId, judge);
    }

    @Override
    public String toString() {
        return "Docket{" +
                "id=" + id +
                ", reference='" + reference + '\'' +
                ", courtId=" + courtId +
                ", judge='" + judge + '\'' +
                '}';
    }
}
