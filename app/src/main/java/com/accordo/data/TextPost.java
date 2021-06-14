package com.accordo.data;

public class TextPost implements Post{

    private String pid,ctitle;
    private User author;
    private String content;

    public TextPost(String pid, User author, String ctitle, String content) {

        this.pid = pid;
        this.author = author;
        this.ctitle = ctitle;
        this.content = content;
    }

    @Override
    public void setContent() {

    }
}
