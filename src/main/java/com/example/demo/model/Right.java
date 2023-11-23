package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Right {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("isOpponent")
    private boolean isOpponent;
    @JsonProperty("classification")
    private Classification classification;
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("reference")
    private String reference;

    public Right() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isOpponent() {
        return isOpponent;
    }

    public void setOpponent(boolean opponent) {
        isOpponent = opponent;
    }

    public Classification getClassification() {
        return classification;
    }

    public void setClassification(Classification classification) {
        this.classification = classification;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Right right = (Right) o;
        return Objects.equals(id, right.id) && isOpponent == right.isOpponent && Objects.equals(classification, right.classification) && Objects.equals(name, right.name) && Objects.equals(type, right.type) && Objects.equals(reference, right.reference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isOpponent, classification, name, type, reference);
    }

    @Override
    public String toString() {
        return "Right{" +
                "id=" + id +
                ", isOpponent=" + isOpponent +
                ", classification=" + classification +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", reference='" + reference + '\'' +
                '}';
    }
}
