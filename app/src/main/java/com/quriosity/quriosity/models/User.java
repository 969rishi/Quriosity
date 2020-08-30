package com.quriosity.quriosity.models;

public class User {
    String city;
    String contactno;
    String country;
    String dob;
    String firstname;
    String gender;
    String lastname;
    double latitude;
    double longitude;
    int pincode;
    String primaryemail;
    String profileimage;
    String profilesummary;
    String secondaryemail;
    String state;
    int uid;
    int upid;
    public User(){

    }
    public User(String city, String contactno, String country, String dob, String firstname, String gender, String lastname, double latitude, double longitude, int pincode, String primaryemail, String profileimage, String profilesummary, String secondaryemail, String state, int uid, int upid) {
        this.city = city;
        this.contactno = contactno;
        this.country = country;
        this.dob = dob;
        this.firstname = firstname;
        this.gender = gender;
        this.lastname = lastname;
        this.latitude = latitude;
        this.longitude = longitude;
        this.pincode = pincode;
        this.primaryemail = primaryemail;
        this.profileimage = profileimage;
        this.profilesummary = profilesummary;
        this.secondaryemail = secondaryemail;
        this.state = state;
        this.uid = uid;
        this.upid = upid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getContactno() {
        return contactno;
    }

    public void setContactno(String contactno) {
        this.contactno = contactno;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getPincode() {
        return pincode;
    }

    public void setPincode(int pincode) {
        this.pincode = pincode;
    }

    public String getPrimaryemail() {
        return primaryemail;
    }

    public void setPrimaryemail(String primaryemail) {
        this.primaryemail = primaryemail;
    }

    public String getProfileimage() {
        return profileimage;
    }

    public void setProfileimage(String profileimage) {
        this.profileimage = profileimage;
    }

    public String getProfilesummary() {
        return profilesummary;
    }

    public void setProfilesummary(String profilesummary) {
        this.profilesummary = profilesummary;
    }

    public String getSecondaryemail() {
        return secondaryemail;
    }

    public void setSecondaryemail(String secondaryemail) {
        this.secondaryemail = secondaryemail;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getUpid() {
        return upid;
    }

    public void setUpid(int upid) {
        this.upid = upid;
    }
}
