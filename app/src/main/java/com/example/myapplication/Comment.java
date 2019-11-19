package com.example.myapplication;

import com.google.firebase.database.ServerValue;

public class Comment {

    private String content,uid,uname;
    private String timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uname, String timestamp) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public Comment(String content, String uid, String uname) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
