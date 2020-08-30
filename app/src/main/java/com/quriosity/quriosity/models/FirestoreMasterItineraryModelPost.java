package com.quriosity.quriosity.models;

import java.util.List;
import java.util.Map;

public class FirestoreMasterItineraryModelPost {
    private long id;
    private String itercode;
    private String itertitle;
    private String shortdesc;
    private String iternotes;
    private String profileurl;
    private String coverurl;
    private String startingfrom;
    private String goingto;
    private String duration;
    private String itertype;
    private List<String> tags;
    private List<String> destinations;
    private List<String> activities;
    private String status;
    private String visibility;
    private boolean ispublished;
    private Map plans;
    private String createdby;
    private String createdtime;
    private String lastmodified;

    public FirestoreMasterItineraryModelPost(long id, String itercode, String itertitle, String shortdesc, String iternotes, String profileurl, String coverurl, String startingfrom, String goingto, String duration, String itertype, List<String> tags, List<String> destinations, List<String> activities, String status, String visibility, boolean ispublished, Map plans, String createdby, String createdtime, String lastmodified) {
        this.id = id;
        this.itercode = itercode;
        this.itertitle = itertitle;
        this.shortdesc = shortdesc;
        this.iternotes = iternotes;
        this.profileurl = profileurl;
        this.coverurl = coverurl;
        this.startingfrom = startingfrom;
        this.goingto = goingto;
        this.duration = duration;
        this.itertype = itertype;
        this.tags = tags;
        this.destinations = destinations;
        this.activities = activities;
        this.status = status;
        this.visibility = visibility;
        this.ispublished = ispublished;
        this.plans = plans;
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

    public String getItercode() {
        return itercode;
    }

    public void setItercode(String itercode) {
        this.itercode = itercode;
    }

    public String getItertitle() {
        return itertitle;
    }

    public void setItertitle(String itertitle) {
        this.itertitle = itertitle;
    }

    public String getShortdesc() {
        return shortdesc;
    }

    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    public String getIternotes() {
        return iternotes;
    }

    public void setIternotes(String iternotes) {
        this.iternotes = iternotes;
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

    public String getStartingfrom() {
        return startingfrom;
    }

    public void setStartingfrom(String startingfrom) {
        this.startingfrom = startingfrom;
    }

    public String getGoingto() {
        return goingto;
    }

    public void setGoingto(String goingto) {
        this.goingto = goingto;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getItertype() {
        return itertype;
    }

    public void setItertype(String itertype) {
        this.itertype = itertype;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    public List<String> getActivities() {
        return activities;
    }

    public void setActivities(List<String> activities) {
        this.activities = activities;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public boolean isIspublished() {
        return ispublished;
    }

    public void setIspublished(boolean ispublished) {
        this.ispublished = ispublished;
    }

    public Map getPlans() {
        return plans;
    }

    public void setPlans(Map plans) {
        this.plans = plans;
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