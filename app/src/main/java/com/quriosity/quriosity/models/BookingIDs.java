package com.quriosity.quriosity.models;

import android.graphics.Bitmap;

public class BookingIDs {
String hotelname;
String bookingID;

    public BookingIDs(String hotelname, String bookingID, Bitmap img) {
        this.hotelname = hotelname;
        this.bookingID = bookingID;
        this.img = img;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    Bitmap img;
    public String getHotelname() {
        return hotelname;
    }

    public void setHotelname(String hotelname) {
        this.hotelname = hotelname;
    }

    public String getBookingID() {
        return bookingID;
    }

    public void setBookingID(String bookingID) {
        this.bookingID = bookingID;
    }
}
