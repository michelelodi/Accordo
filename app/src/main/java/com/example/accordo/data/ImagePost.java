package com.example.accordo.data;

import java.util.Base64;

public class ImagePost extends Post{

    private Base64 content;

    public ImagePost(int pid, User author, String ctitle, Base64 content) {
        super(pid, author, ctitle);
        this.content = content;
    }
}
