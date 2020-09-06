package com.quriosity.quriosity.models.firechat;

public class FirestoreCategoryModel {
    private String title;
    private String description;

    public FirestoreCategoryModel(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public FirestoreCategoryModel() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}