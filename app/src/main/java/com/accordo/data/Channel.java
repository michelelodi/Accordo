package com.accordo.data;

import org.jetbrains.annotations.NotNull;

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

    @Override
    public @NotNull String toString() {
        return "Channel{" +
                "ctitle='" + ctitle + '\'' +
                ", creator=" + creator +
                '}';
    }
}
