package com.quriosity.quriosity.models;

public class NotificationModelClass{
    String name;
    String thread;
    String uui;
    String importance;
    String Seen;

    public String getImportance() {
        return importance;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    String Type;
    public String getSeen() {
        return Seen;
    }

    public void setSeen(String seen) {
        Seen = seen;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThread() {
        return thread;
    }

    public void setThread(String thread) {
        this.thread = thread;
    }

    public String getUui() {
        return uui;
    }

    public void setUui(String uui) {
        this.uui = uui;
    }

    public NotificationModelClass(String name, String thread, String uui,String Seen,String importance,String Type) {
        this.name = name;
        this.thread = thread;
        this.uui = uui;
        this.Seen=Seen;
        this.importance=importance;
        this.Type=Type;
    }

    public  NotificationModelClass(){

    }
}
