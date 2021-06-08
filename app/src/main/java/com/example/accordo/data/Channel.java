package com.example.accordo.data;

public class Channel {

    private String ctitle;
    private User creator;

    public Channel(String ctitle, User creator) {
        this.ctitle = ctitle;
        this.creator = creator;
    }

    public String getCTitle() {
        return ctitle;
    }

    public User getCreator() {
        return creator;
    }
}
