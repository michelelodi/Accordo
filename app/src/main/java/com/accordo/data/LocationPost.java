package com.accordo.data;

import java.util.Arrays;

public class LocationPost extends Post{

    String[] content;

    public LocationPost(String pid, String authorUid, String author, String cTitle) {
        super(pid, authorUid, author, cTitle);
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
