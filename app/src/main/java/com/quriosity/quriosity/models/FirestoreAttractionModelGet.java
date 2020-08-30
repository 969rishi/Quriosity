package com.quriosity.quriosity.models;

import java.util.List;

public class FirestoreAttractionModelGet {
    private long id;
    private String attrcode;
    private String attrtitle;
    private String attrnotes;
    private String profileurl;
    private String coverurl;
    private String attrtype;
    private AddressModel address;
    private Object parentdest;
    private List<String> tags;
    private String createdby;
    private String createdtime;
    private String lastmodified;

    public FirestoreAttractionModelGet() {
    }

    public String getAttrtype() {
        return attrtype;
    }

    public void setAttrtype(String attrtype) {
        this.attrtype = attrtype;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAttrcode() {
        return attrcode;
    }

    public void setAttrcode(String attrcode) {
        this.attrcode = attrcode;
    }

    public String getAttrtitle() {
        return attrtitle;
    }

    public void setAttrtitle(String attrtitle) {
        this.attrtitle = attrtitle;
    }

    public String getAttrnotes() {
        return attrnotes;
    }

    public void setAttrnotes(String attrnotes) {
        this.attrnotes = attrnotes;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }

    public String getCoverurl() {
        return coverurl;
    }

    public void setCoverurl(String coverurl) {
        this.coverurl = coverurl;
    }

    public AddressModel getAddress() {
        return address;
    }

    public void setAddress(AddressModel address) {
        this.address = address;
    }

    public Object getParentdest() {
        return parentdest;
    }

    public void setParentdest(Object parentdest) {
        this.parentdest = parentdest;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }
}