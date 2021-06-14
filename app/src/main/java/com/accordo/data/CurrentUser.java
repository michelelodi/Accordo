package com.accordo.data;

import java.util.Base64;

public class CurrentUser extends User{

    private String sid;

    public CurrentUser(String uid, String sid) {
        super(uid);
        this.sid = sid;
    }

    public CurrentUser(String name, Base64 picture, String pversion, String uid, String sid) {
        super(name, picture, pversion, uid);
        this.sid = sid;
    }
}
