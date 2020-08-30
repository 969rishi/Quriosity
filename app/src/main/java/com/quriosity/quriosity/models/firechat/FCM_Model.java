package com.quriosity.quriosity.models.firechat;

public class FCM_Model {
    String actorOtherName;
    String chatID;
    String body;
    String click_action;
    String msgtype;
    String url;
    String actorOther;
    String actorMe;
    String msgID;

    public String getMsgID() {
        return msgID;
    }

    public void setMsgID(String msgID) {
        this.msgID = msgID;
    }



    public FCM_Model(String actorOtherName, String chatID, String body, String click_action, String msgtype,
                     String url, String actorOther, String actorMe, String actorOtherProfileUrl,
                     String msgID) {
        this.actorOtherName = actorOtherName;
        this.chatID = chatID;
        this.body = body;
        this.click_action = click_action;
        this.msgtype = msgtype;
        this.url = url;
        this.actorOther = actorOther;
        this.actorMe = actorMe;
        this.actorOtherProfileUrl = actorOtherProfileUrl;
        this.msgID = msgID;
    }

    String actorOtherProfileUrl;

    public String getActorOtherName() {
        return actorOtherName;
    }

    public void setActorOtherName(String actorOtherName) {
        this.actorOtherName = actorOtherName;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getClick_action() {
        return click_action;
    }

    public void setClick_action(String click_action) {
        this.click_action = click_action;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getActorOther() {
        return actorOther;
    }

    public void setActorOther(String actorOther) {
        this.actorOther = actorOther;
    }

    public String getActorMe() {
        return actorMe;
    }

    public void setActorMe(String actorMe) {
        this.actorMe = actorMe;
    }

    public String getActorOtherProfileUrl() {
        return actorOtherProfileUrl;
    }

    public void setActorOtherProfileUrl(String actorOtherProfileUrl) {
        this.actorOtherProfileUrl = actorOtherProfileUrl;
    }
}
