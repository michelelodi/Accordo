package com.example.accordo;

import java.util.Base64;

public class CurrentUser extends User{

    private int sid;

    public CurrentUser(String name, Base64 picture, int pversion, int uid, int sid) {
        super(name, picture, pversion, uid);
        this.sid = sid;
    }
}
