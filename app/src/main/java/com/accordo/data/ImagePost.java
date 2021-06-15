package com.accordo.data;


import org.jetbrains.annotations.NotNull;

public class ImagePost implements Post{

    private String pid, author, cTitle, content;

    public ImagePost(String pid, String author, String cTitle) {
        this.pid = pid;
        this.author = author;
        this.cTitle = cTitle;
    }

    @Override
    public String getAuthor() { return author; }

    @Override
    public String getContent() {
        return content;
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
