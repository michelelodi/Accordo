package com.example.accordo;

import java.util.Base64;

public class User {
    private String name;
    private Base64 picture;
    private int pversion, uid;

    public User(String name, Base64 picture, int pversion, int uid) {
        this.name = name;
        this.picture = picture;
        this.pversion = pversion;
        this.uid = uid;
    }
}
