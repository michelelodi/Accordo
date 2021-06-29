package com.accordo.data;

import org.jetbrains.annotations.NotNull;

public class Channel {

    private final String cTitle;
    private final boolean creator;

    public Channel(String cTitle, boolean creator) {
        this.cTitle = cTitle;
        this.creator = creator;
    }

    public String getCTitle() {
        return cTitle;
    }

    public boolean isMine() {
        return creator;
    }

    @Override
    public @NotNull String toString() { return "Channel{" + "cTitle='" + cTitle + '\'' + ", creator=" + creator + '}'; }
}
