package com.example.myapplication;

public class PlacesDesc {

    String imagename, imageurl, placesdesc;

    public PlacesDesc(String imagename, String imageurl, String placesdesc) {
        this.imagename = imagename;
        this.imageurl = imageurl;
        this.placesdesc = placesdesc;
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
