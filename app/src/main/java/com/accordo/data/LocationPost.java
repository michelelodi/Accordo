package com.accordo.data;

import java.util.Arrays;

public class LocationPost extends Post{

    String[] content;

    public LocationPost(String pid, String author, String cTitle) {
        super(pid, author, cTitle);
    }


    @Override
    public String getContent() {
        return Arrays.toString(content);
    }

    public String[] getCoords() {
        String[] coords = { content[0], content[1] };
        return coords;
    }

    @Override
    public void setContent(String content) {
        this.content = content.split(",");
    }

}
