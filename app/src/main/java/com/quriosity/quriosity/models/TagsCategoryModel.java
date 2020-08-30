package com.quriosity.quriosity.models;

public class TagsCategoryModel {
    private long id;
    private String tagcatcode;
    private String tctitle;
    private String description;


    public TagsCategoryModel(long id, String tagcatcode, String tctitle, String description) {
        this.id = id;
        this.tagcatcode = tagcatcode;
        this.tctitle = tctitle;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTagcatcode() {
        return tagcatcode;
    }

    public void setTagcatcode(String tagcatcode) {
        this.tagcatcode = tagcatcode;
    }

    public String getTctitle() {
        return tctitle;
    }

    public void setTctitle(String tctitle) {
        this.tctitle = tctitle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}