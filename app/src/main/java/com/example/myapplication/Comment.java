package com.example.myapplication;


import com.google.firebase.database.ServerValue;

public class Comment {

    private String content,uid,uname;
    private Object timestamp;

    public Comment() {
    }

    public Comment(String content, String uid, String uname, Object timestamp) {
        this.content = content;
        this.uid = uid;
        this.uname = uname;
        this.timestamp = timestamp;
    }

    public Comment(String content, String uid, String uname) {
        this.content = content;
        this.uid = uid;

        this.uname = uname;
        this.timestamp = ServerValue.TIMESTAMP;

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

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
