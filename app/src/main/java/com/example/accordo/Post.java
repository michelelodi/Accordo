package com.example.accordo;

public abstract class Post {

    private int pid;
    private User author;
    private String ctitle;

    public Post(int pid, User author, String ctitle) {
        this.pid = pid;
        this.author = author;
        this.ctitle = ctitle;
    }
}
