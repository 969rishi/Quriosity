package com.quriosity.quriosity.models;

public class TagsCategory {
    private String description;

    private String tcid;

    private String tctitle;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTcid() {
        return tcid;
    }

    public void setTcid(String tcid) {
        this.tcid = tcid;
    }

    public String getTctitle() {
        return tctitle;
    }

    public void setTctitle(String tctitle) {
        this.tctitle = tctitle;
    }

    @Override
    public String toString() {
        return "ClassPojo [description = " + description + ", tcid = " + tcid + ", tctitle = " + tctitle + "]";
    }
}
