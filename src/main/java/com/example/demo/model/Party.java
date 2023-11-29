package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;
import java.util.Set;

public class Party {

    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("representatives")
    private Set<String> representatives;

    public Party() {
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

    public Set<String> getRepresentatives() {
        return representatives;
    }

    public void setRepresentatives(Set<String> representatives) {
        this.representatives = representatives;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Party party = (Party) o;
        return Objects.equals(name, party.name) && Objects.equals(type, party.type) && Objects.equals(representatives, party.representatives);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, representatives);
    }

    @Override
    public String toString() {
        return "Party{" +
               "name='" + name + '\'' +
               ", type='" + type + '\'' +
               ", representatives=" + representatives +
               '}';
    }
}
