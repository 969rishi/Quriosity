package com.quriosity.quriosity.models.firechat;

public class FirestoreUserProfileModel {
    private String firebaseuid;
    private String firstname;
    private String lastname;
    private String emailId;
    private String gender;
    private String profilePic;
    private String lastLoginAt;
    private String onlineStatus;
    private String lastOnlineTimestamp;

    public FirestoreUserProfileModel(String firebaseuid, String firstname, String lastname, String emailId, String gender, String profilePic, String lastLoginAt, String onlineStatus, String lastOnlineTimestamp) {
        this.firebaseuid = firebaseuid;
        this.firstname = firstname;
        this.lastname = lastname;
        this.emailId = emailId;
        this.gender = gender;
        this.profilePic = profilePic;
        this.lastLoginAt = lastLoginAt;
        this.onlineStatus = onlineStatus;
        this.lastOnlineTimestamp = lastOnlineTimestamp;
    }

    public FirestoreUserProfileModel() {
    }

    public String getFirebaseuid() {
        return firebaseuid;
    }

    public void setFirebaseuid(String firebaseuid) {
        this.firebaseuid = firebaseuid;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(String lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getLastOnlineTimestamp() {
        return lastOnlineTimestamp;
    }

    public void setLastOnlineTimestamp(String lastOnlineTimestamp) {
        this.lastOnlineTimestamp = lastOnlineTimestamp;
    }
}