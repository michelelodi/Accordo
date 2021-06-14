package com.accordo.data;

import java.util.Base64;

public class ImagePost implements Post{

    private String pid,ctitle;
    private User author;
    private Base64 content;

    public ImagePost(String pid, User author, String ctitle, Base64 content) {
        this.pid = pid;
        this.author = author;
        this.ctitle = ctitle;
        this.content = content;
    }

    @Override
    public void setContent() {

    }
}
