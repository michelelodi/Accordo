package com.accordo.data;

import android.graphics.Bitmap;

import java.util.Arrays;

public class LocationPost extends Post{

    String[] content;

    public LocationPost(String pid, String author, String cTitle, Bitmap authorProfilePicture) {
        super(pid, author, cTitle, authorProfilePicture);
    }

    @Override
    public String getContent() {
        return Arrays.toString(content);
    }

    public String[] getCoords() { return content; }

    @Override
    public void setContent(String content) {
        this.content = content.split(",");
    }

}
