package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Objects;

public class Classification {
    @JsonProperty("name")
    private String name;
    @JsonProperty("type")
    private String type;
    @JsonProperty("classId")
    private Long classId;
    @JsonProperty("image")
    private String image;

    public Classification() {
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

    public Long getClassId() {
        return classId;
    }

    public void setClassId(Long classId) {
        this.classId = classId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Classification that = (Classification) o;
        return Objects.equals(name, that.name) && Objects.equals(type, that.type) && Objects.equals(classId, that.classId) && Objects.equals(image, that.image);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, classId, image);
    }

    @Override
    public String toString() {
        return "Classification{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", classId='" + classId + '\'' +
                ", image=" + image +
                '}';
    }
}
