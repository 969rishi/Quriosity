package com.quriosity.quriosity.models;

import java.util.List;

public class FirestoreTripModelPost {
    private String id;
    private String tripcode;
    private String tripname;
    private String overview;
    private String shortdescription;
    private String icon;
    private String cover;
    private String tripstartdate;
    private String tripenddate;
    private List<String> tags;
    private List<String> destinationtags;
    private List<String> triptypetags;
    private List<String> activitiestags;
    private String createat;
    private String lastmodified;
    private String visibility;
    private boolean ispublished;

    public FirestoreTripModelPost(String tripid, String tripcode,
                                  String tripname,
                                  String tripOverView,
                                  String tripShortDesc,
                                  List<String> tripTypes,
                                  List<String> tripLocations,
                                  List<String> tripActivities,
                                  List<String> tripTags,
                                  String startDateTxt,
                                  String endDateTxt,
                                  String iconImgUri,
                                  String coverImgUri,
                                  boolean ispublished,
                                  String visibility,
                                  String createat,
                                  String lastmodified) {
        this.id = tripid;
        this.tripcode = tripcode;
        this.tripname = tripname;
        this.overview = tripOverView;
        this.shortdescription = tripShortDesc;
        this.icon = iconImgUri;
        this.cover = coverImgUri;
        this.tripstartdate = startDateTxt;
        this.tripenddate = endDateTxt;
        this.tags = tripTags;
        this.destinationtags = tripLocations;
        this.triptypetags = tripTypes;
        this.activitiestags = tripActivities;
        this.createat = createat;
        this.lastmodified = lastmodified;
        this.visibility = visibility;
        this.ispublished = ispublished;
    }

    public String getId() {
        return id;
    }

    public void setId(String tripid) {
        this.id = tripid;
    }

    public String getTripcode() {
        return tripcode;
    }

    public void setTripcode(String tripcode) {
        this.tripcode = tripcode;
    }

    public String getTripname() {
        return tripname;
    }

    public void setTripname(String tripname) {
        this.tripname = tripname;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getShortdescription() {
        return shortdescription;
    }

    public void setShortdescription(String showdescription) {
        this.shortdescription = showdescription;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getTripstartdate() {
        return tripstartdate;
    }

    public void setTripstartdate(String tripstartdate) {
        this.tripstartdate = tripstartdate;
    }

    public String getTripenddate() {
        return tripenddate;
    }

    public void setTripenddate(String tripenddate) {
        this.tripenddate = tripenddate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<String> getDestinationtags() {
        return destinationtags;
    }

    public void setDestinationtags(List<String> destinationtags) {
        this.destinationtags = destinationtags;
    }

    public List<String> getTriptypetags() {
        return triptypetags;
    }

    public void setTriptypetags(List<String> triptypetags) {
        this.triptypetags = triptypetags;
    }

    public List<String> getActivitiestags() {
        return activitiestags;
    }

    public void setActivitiestags(List<String> activitiestags) {
        this.activitiestags = activitiestags;
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

}