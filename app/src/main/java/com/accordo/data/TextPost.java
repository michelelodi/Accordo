package com.accordo.data;

import org.jetbrains.annotations.NotNull;

public class TextPost implements Post{

    private String pid, author, cTitle, content;

    public TextPost(String pid, String author, String cTitle) {

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
    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public @NotNull String toString() {
        return "TextPost{" +
                "pid='" + pid + '\'' +
                ", author='" + author + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
