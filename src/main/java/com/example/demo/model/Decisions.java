package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class Decisions {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("reference")
    private String reference;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyyMMdd")
    @JsonProperty("judgementDate")
    private LocalDate judgementDate;

    @JsonProperty("level")
    private String level;
    @JsonProperty("nature")
    private List<String> nature;
    @JsonProperty("robotSource")
    private String robotSource;

    public Decisions() {

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

    public LocalDate getJudgementDate() {
        return judgementDate;
    }

    public void setJudgementDate(LocalDate judgementDate) {
        this.judgementDate = judgementDate;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<String> getNature() {
        return nature;
    }

    public void setNature(List<String> nature) {
        this.nature = nature;
    }

    public String getRobotSource() {
        return robotSource;
    }

    public void setRobotSource(String robotSource) {
        this.robotSource = robotSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Decisions decisions = (Decisions) o;
        return Objects.equals(id, decisions.id) && Objects.equals(reference, decisions.reference) && Objects.equals(judgementDate, decisions.judgementDate) &&
               Objects.equals(level, decisions.level) && Objects.equals(nature, decisions.nature) && Objects.equals(robotSource, decisions.robotSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, reference, judgementDate, level, nature, robotSource);
    }

    @Override
    public String toString() {
        return "Decisions{" +
               "id=" + id +
               ", reference='" + reference + '\'' +
               ", judgementDate=" + judgementDate +
               ", level='" + level + '\'' +
               ", nature='" + nature + '\'' +
               ", robotSource='" + robotSource + '\'' +
               '}';
    }
}
