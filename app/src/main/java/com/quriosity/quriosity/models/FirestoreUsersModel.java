package com.quriosity.quriosity.models;

public class FirestoreUsersModel {
    private String lastname;
    private String nickname;
    private String gender;
    private String age;
    private String fullname;
    private Boolean isEmailVerified;
    private String username;
    private String firebaseuid;
    private String signupmethod;
    private String lastlogin;
    private String createdate;
    private String usermobile;
    private String primaryemail;
    private String userpassword;
    private String user10digitmobile;
    private String firstname;
    private String registrationToken;
    private String lastOnlineTimestamp;
    private String onlineStatus;
    private String addressline1;
    private String addressline2;

    public FirestoreUsersModel() {
    }

    public FirestoreUsersModel(String lastname, String nickname, String gender, String age, String fullname, Boolean isEmailVerified, String username, String firebaseuid, String signupmethod, String lastlogin, String createdate, String usermobile, String primaryemail, String userpassword, String user10digitmobile, String firstname, String registrationToken, String lastOnlineTimestamp, String onlineStatus, String addressline1, String addressline2) {
        this.lastname = lastname;
        this.nickname = nickname;
        this.gender = gender;
        this.age = age;
        this.fullname = fullname;
        this.isEmailVerified = isEmailVerified;
        this.username = username;
        this.firebaseuid = firebaseuid;
        this.signupmethod = signupmethod;
        this.lastlogin = lastlogin;
        this.createdate = createdate;
        this.usermobile = usermobile;
        this.primaryemail = primaryemail;
        this.userpassword = userpassword;
        this.user10digitmobile = user10digitmobile;
        this.firstname = firstname;
        this.registrationToken = registrationToken;
        this.lastOnlineTimestamp = lastOnlineTimestamp;
        this.onlineStatus = onlineStatus;
        this.addressline1 = addressline1;
        this.addressline2 = addressline2;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public Boolean getEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(Boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirebaseuid() {
        return firebaseuid;
    }

    public void setFirebaseuid(String firebaseuid) {
        this.firebaseuid = firebaseuid;
    }

    public String getSignupmethod() {
        return signupmethod;
    }

    public void setSignupmethod(String signupmethod) {
        this.signupmethod = signupmethod;
    }

    public String getLastlogin() {
        return lastlogin;
    }

    public void setLastlogin(String lastlogin) {
        this.lastlogin = lastlogin;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getUsermobile() {
        return usermobile;
    }

    public void setUsermobile(String usermobile) {
        this.usermobile = usermobile;
    }

    public String getPrimaryemail() {
        return primaryemail;
    }

    public void setPrimaryemail(String primaryemail) {
        this.primaryemail = primaryemail;
    }

    public String getUserpassword() {
        return userpassword;
    }

    public void setUserpassword(String userpassword) {
        this.userpassword = userpassword;
    }

    public String getUser10digitmobile() {
        return user10digitmobile;
    }

    public void setUser10digitmobile(String user10digitmobile) {
        this.user10digitmobile = user10digitmobile;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getRegistrationToken() {
        return registrationToken;
    }

    public void setRegistrationToken(String registrationToken) {
        this.registrationToken = registrationToken;
    }

    public String getLastOnlineTimestamp() {
        return lastOnlineTimestamp;
    }

    public void setLastOnlineTimestamp(String lastOnlineTimestamp) {
        this.lastOnlineTimestamp = lastOnlineTimestamp;
    }

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    public String getAddressline1() {
        return addressline1;
    }

    public void setAddressline1(String addressline1) {
        this.addressline1 = addressline1;
    }

    public String getAddressline2() {
        return addressline2;
    }

    public void setAddressline2(String addressline2) {
        this.addressline2 = addressline2;
    }
}