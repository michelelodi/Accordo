package com.example.accordo;

public class TextPost extends Post{

    private String content;

    public TextPost(int pid, User author, String ctitle, String content) {
        super(pid, author, ctitle);
        this.content = content;
    }
}