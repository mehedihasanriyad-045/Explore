package com.example.myapplication;

import com.google.firebase.database.Exclude;

public class PlacesDesc {

    String imagename, imageurl, placesdesc, key;
    double rating, sum;
    int count;

    public PlacesDesc(String imagename, String imageurl, String placesdesc, double rating, double sum, int count) {
        this.imagename = imagename;
        this.imageurl = imageurl;
        this.placesdesc = placesdesc;
        this.rating = rating;
        this.sum = sum;
        this.count = count;
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
