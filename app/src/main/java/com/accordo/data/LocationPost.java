package com.accordo.data;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class LocationPost implements Post{

    private String pid, author, cTitle;
    String[] content;

    public LocationPost(String pid, String author, String cTitle) {
        this.pid = pid;
        this.author = author;
        this.cTitle = cTitle;
    }

    @Override
    public String getAuthor() { return author; }

    @Override
    public String getContent() {
        return Arrays.toString(content);
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
