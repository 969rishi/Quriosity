package com.quriosity.quriosity.models;

public class FirebaseSocialModelPost {
    private String itemid;
    private String itemtype;
    private String fuid;
    private String wedamoruid;
    private String timestamp;
    private String username;
    private String commentText;

    public FirebaseSocialModelPost() {
        //empty constructor needed
    }

    public FirebaseSocialModelPost(String itemid, String itemtype, String fuid, String wedamoruid, String timestamp, String username, String commenttext) {
        this.itemid = itemid;
        this.itemtype = itemtype;
        this.fuid = fuid;
        this.wedamoruid = wedamoruid;
        this.timestamp = timestamp;
        this.username = username;
        this.commentText = commenttext;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getFuid() {
        return fuid;
    }

    public void setFuid(String fuid) {
        this.fuid = fuid;
    }

    public String getWedamoruid() {
        return wedamoruid;
    }

    public void setWedamoruid(String wedamoruid) {
        this.wedamoruid = wedamoruid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}