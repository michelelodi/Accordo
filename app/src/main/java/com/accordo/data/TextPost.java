package com.accordo.data;

import org.jetbrains.annotations.NotNull;

public class TextPost implements Post{

    private String pid, author, content;

    public TextPost(String pid, String author) {

        this.pid = pid;
        this.author = author;
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
