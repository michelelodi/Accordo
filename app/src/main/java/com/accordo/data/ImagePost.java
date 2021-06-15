package com.accordo.data;


import org.jetbrains.annotations.NotNull;

public class ImagePost implements Post{

    private String pid, author, content;

    public ImagePost(String pid, String author) {
        this.pid = pid;
        this.author = author;
    }

    @Override
    public void setContent(String content) { this.content = content; }

    @Override
    public @NotNull String toString() {
        return "ImagePost{" +
                "pid='" + pid + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
