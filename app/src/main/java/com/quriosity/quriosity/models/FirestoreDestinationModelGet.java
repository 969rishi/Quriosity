package com.quriosity.quriosity.models;

import java.util.List;

public class FirestoreDestinationModelGet {
    private long id;
    private String destcode;
    private String desttitle;
    private String destnotes;
    private String desttype;
    private String profileurl;
    private String coverurl;
    private AddressModel address;
    private Object parentdest;
    private List<String> tags;
    private String createdby;
    private String createdtime;
    private String lastmodified;

    private FirestoreDestinationModelGet() {
        //empty constructor
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDestcode() {
        return destcode;
    }

    public void setDestcode(String destcode) {
        this.destcode = destcode;
    }

    public String getDesttitle() {
        return desttitle;
    }

    public void setDesttitle(String desttitle) {
        this.desttitle = desttitle;
    }

    public String getDestnotes() {
        return destnotes;
    }

    public void setDestnotes(String destnotes) {
        this.destnotes = destnotes;
    }

    public String getDesttype() {
        return desttype;
    }

    public void setDesttype(String desttype) {
        this.desttype = desttype;
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