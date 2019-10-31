package com.example.myapplication;

import android.net.Uri;

public class UserInfo{

    String name,email,phoneNum;

    public UserInfo(String name, String email, String phoneNum) {
        this.name = name;
        this.email = email;
        this.phoneNum = phoneNum;

    }

    public UserInfo(String name, String email, String phoneNum, Uri uri) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

}