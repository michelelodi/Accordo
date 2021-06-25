package com.accordo.data;


import android.graphics.Bitmap;

public class ImagePost extends Post{

    private String content;

    public ImagePost(String pid, String author, String cTitle, Bitmap authorProfilePicture) {
        super(pid, author, cTitle, authorProfilePicture);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) { this.content = content; }

}
