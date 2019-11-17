package com.example.myapplication;


import com.google.firebase.database.Exclude;

public class PlacesDesc {

    String imagename, imageurl, placesdesc, key;


    public PlacesDesc(String imagename, String imageurl, String placesdesc) {
        this.imagename = imagename;
        this.imageurl = imageurl;
        this.placesdesc = placesdesc;
    }
    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }

    public PlacesDesc() {
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getPlacesdesc() {
        return placesdesc;
    }

    public void setPlacesdesc(String placesdesc) {
        this.placesdesc = placesdesc;
    }
}
