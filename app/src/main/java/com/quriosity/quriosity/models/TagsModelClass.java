package com.quriosity.quriosity.models;

public class TagsModelClass {
    private long id;
    private String tagtitle;
    private String tagid;
    private String tagcategory;
    private String description;
    private String metainfo;
    private String tagcode;

    public TagsModelClass(long id, String tagcode, String tagtitle, String tagcategory, String description, String metainfo) {
        this.id = id;
        this.tagtitle = tagtitle;
        this.tagcategory = tagcategory;
        this.description = description;
        this.metainfo = metainfo;
        this.tagcode = tagcode;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTagcode() {
        return tagcode;
    }

    public void setTagcode(String tagcode) {
        this.tagcode = tagcode;
    }

    public String getTagtitle() {
        return tagtitle;
    }

    public void setTagtitle(String tagtitle) {
        this.tagtitle = tagtitle;
    }

    public String getTagid() {
        return tagid;
    }

    public void setTagid(String tagid) {
        this.tagid = tagid;
    }

    public String getTagcategory() {
        return tagcategory;
    }

    public void setTagcategory(String tagcategory) {
        this.tagcategory = tagcategory;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMetainfo() {
        return metainfo;
    }

    public void setMetainfo(String metainfo) {
        this.metainfo = metainfo;
    }

    @Override
    public String toString() {
        return "ClassPojo [tagtitle = " + tagtitle + ", tagid = " + tagid + ", tagcategory = " + tagcategory + ", description = " + description + ", metainfo = " + metainfo + "]";
    }
}