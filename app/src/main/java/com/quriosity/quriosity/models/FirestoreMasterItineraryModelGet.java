package com.quriosity.quriosity.models;

import java.util.List;

public class FirestoreMasterItineraryModelGet {
    private String mitid;
    private String mitcode;
    private String mitname;
    private String numdays;
    private String visibility;
    private boolean ispublished;
    private Object plans;
    private List<String> tags;
    private List<String> locationstags;
    private String createat;
    private String lastmodified;

    public FirestoreMasterItineraryModelGet() {
        //empty constructor
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean getIspublished() {
        return ispublished;
    }

    public void setIspublished(boolean ispublished) {
        this.ispublished = ispublished;
    }

    public List<String> getLocationstags() {
        return locationstags;
    }

    public void setLocationstags(List<String> locationstags) {
        this.locationstags = locationstags;
    }

    public String getMitid() {
        return mitid;
    }

    public void setMitid(String mitid) {
        this.mitid = mitid;
    }

    public String getMitcode() {
        return mitcode;
    }

    public void setMitcode(String mitcode) {
        this.mitcode = mitcode;
    }

    public String getMitname() {
        return mitname;
    }

    public void setMitname(String mitname) {
        this.mitname = mitname;
    }

    public String getNumdays() {
        return numdays;
    }

    public void setNumdays(String numdays) {
        this.numdays = numdays;
    }

    public Object getPlans() {
        return plans;
    }

    public void setPlans(Object plans) {
        this.plans = plans;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getCreateat() {
        return createat;
    }

    public void setCreateat(String createat) {
        this.createat = createat;
    }

    public String getLastmodified() {
        return lastmodified;
    }

    public void setLastmodified(String lastmodified) {
        this.lastmodified = lastmodified;
    }

    @Override
    public String toString() {
        return mitname;
    }

}