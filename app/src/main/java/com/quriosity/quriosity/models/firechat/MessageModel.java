package com.quriosity.quriosity.models.firechat;

import java.util.List;

public class MessageModel {
    public MessageModel() {
    }

    public MessageModel(String mid, String message, String msgtype, String sentby, String senton, String sentto, String url,
                        List<String> sentarray, List<String> readarray, List<String> deletedarray, List<String> receivedarray) {
        this.mid = mid;
        this.message = message;
        this.msgtype = msgtype;
        this.sentby = sentby;
        this.senton = senton;
        this.sentto = sentto;
        this.url = url;
        this.sentarray = sentarray;
        this.readarray = readarray;
        this.deletedarray = deletedarray;
        this.receivedarray = receivedarray;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby) {
        this.sentby = sentby;
    }

    public String getSenton() {
        return senton;
    }

    public void setSenton(String senton) {
        this.senton = senton;
    }

    public String getSentto() {
        return sentto;
    }

    public void setSentto(String sentto) {
        this.sentto = sentto;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getSentarray() {
        return sentarray;
    }

    public void setSentarray(List<String> sentarray) {
        this.sentarray = sentarray;
    }

    public List<String> getReadarray() {
        return readarray;
    }

    public void setReadarray(List<String> readarray) {
        this.readarray = readarray;
    }

    public String getMid() {
        return mid;
    }

    public void setMid(String mid) {
        this.mid = mid;
    }

    public List<String> getReceivedarray() {
        return receivedarray;
    }

    public void setReceivedarray(List<String> receivedarray) {
        this.receivedarray = receivedarray;
    }

    public List<String> getDeletedarray() {
        return deletedarray;
    }

    public void setDeletedarray(List<String> deletedarray) {
        this.deletedarray = deletedarray;
    }

    private String mid;
    private String message;
    private String msgtype;
    private String sentby;
    private String senton;
    private String sentto;
    private String url;
    private List<String> sentarray;
    private List<String> readarray;
    private List<String> deletedarray;
    private List<String> receivedarray;


}
