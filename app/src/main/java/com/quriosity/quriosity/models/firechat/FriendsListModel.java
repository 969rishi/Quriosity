package com.quriosity.quriosity.models.firechat;

public class FriendsListModel {


    public FriendsListModel(String conversationTitle) {
        this.firebaseuid = conversationTitle;
    }

    private String firebaseuid;

    public FriendsListModel() {
    }

    public String getFirebaseuid() {
        return firebaseuid;
    }

    public void setFirebaseuid(String firebaseuid) {
        this.firebaseuid = firebaseuid;
    }
}