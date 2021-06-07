package com.example.accordo.data;

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
}
