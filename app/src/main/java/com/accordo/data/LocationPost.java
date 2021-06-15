package com.accordo.data;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LocationPost implements Post{

    private String pid, author;
    String[] content;

    public LocationPost(String pid, String author) {
        this.pid = pid;
        this.author = author;
    }

    @Override
    public void setContent(String content) {
        this.content = content.split(",");
    }

    @Override
    public @NotNull String toString() {
        return "LocationPost{" +
                "pid='" + pid + '\'' +
                ", author='" + author + '\'' +
                ", content=" + Arrays.toString(content) +
                '}';
    }
}
