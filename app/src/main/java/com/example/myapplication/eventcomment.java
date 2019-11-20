package com.example.myapplication;

public class eventcomment {

    String user,userid,time,comment;

    public eventcomment(String user, String time, String comment) {
        this.user = user;
        this.time = time;
        this.comment = comment;
    }

    public eventcomment() {


    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
