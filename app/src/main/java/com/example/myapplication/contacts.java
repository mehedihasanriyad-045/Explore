package com.example.myapplication;

import com.google.firebase.database.Exclude;

public class contacts {

    String name,phone, key;

    public contacts(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public contacts() {
    }


    @Exclude
    public String getKey() {
        return key;
    }
    @Exclude
    public void setKey(String key) {
        this.key = key;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
