package com.quriosity.quriosity.models;

import java.util.List;
import java.util.Map;

public class FirestoreDestinationModelPost {
    private long id;
    private String destcode;
    private String desttitle;
    private String destnotes;
    private String desttype;
    private String profileurl;
    private String coverurl;
    private Map address;
    private Map parentdest;
    private List<String> tags;
    private String createdby;
    private String createdtime;
    private String lastmodified;

    public FirestoreDestinationModelPost(long id, String destcode, String desttitle, String destnotes,
                                         String desttype, String profileurl, String coverurl, Map address,
                                         Map parentdest, List<String> tags, String createdby,
                                         String createdtime, String lastmodified) {
        this.id = id;
        this.destcode = destcode;
        this.desttitle = desttitle;
        this.destnotes = destnotes;
        this.desttype = desttype;
        this.profileurl = profileurl;
        this.coverurl = coverurl;
        this.address = address;
        this.parentdest = parentdest;
        this.tags = tags;
        this.createdby = createdby;
        this.createdtime = createdtime;
        this.lastmodified = lastmodified;
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

    public Map getAddress() {
        return address;
    }

    public void setAddress(Map address) {
        this.address = address;
    }

    public Map getParentdest() {
        return parentdest;
    }

    public void setParentdest(Map parentdest) {
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