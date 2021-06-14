package com.accordo.data;

import java.util.Base64;

public class User {
    private String name, pversion, uid;
    private Base64 picture;

    public User(String uid) {
        this.uid = uid;
    }

    public User(String name, Base64 picture, String pversion, String uid) {
        this.name = name;
        this.picture = picture;
        this.pversion = pversion;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getPversion() {
        return pversion;
    }

    public String getUid() {
        return uid;
    }

    public Base64 getPicture() {
        return picture;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPicture(Base64 picture) {
        this.picture = picture;
    }
}
