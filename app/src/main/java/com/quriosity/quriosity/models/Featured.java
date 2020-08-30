package com.quriosity.quriosity.models;

public class Featured {
    String Title;
    String img;

    public Featured(String title, String img) {
        Title = title;
        this.img = img;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String Title) {
        this.Title = Title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }


}
