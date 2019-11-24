package com.example.myapplication;

public class Rating {

    String email,rating;

    public Rating(String email, String rating) {
        this.email = email;
        this.rating = rating;
    }

    public Rating() {

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }


}
