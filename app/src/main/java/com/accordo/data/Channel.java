package com.accordo.data;

public class Channel {

    private String ctitle;
    private boolean creator;

    public Channel(String ctitle, boolean creator) {
        this.ctitle = ctitle;
        this.creator = creator;
    }

    public String getCTitle() {
        return ctitle;
    }

    public boolean isMine() {
        return creator;
    }
}
