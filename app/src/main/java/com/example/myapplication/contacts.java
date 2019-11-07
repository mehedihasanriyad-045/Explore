package com.example.myapplication;

public class contacts {

    String name,phone;

    public contacts(String name, String phone) {
        this.name = name;
        this.phone = phone;
    }

    public contacts() {
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
