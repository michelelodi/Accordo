package com.accordo.data;


public class ImagePost extends Post{

    private String content;

    public ImagePost(String pid, String author, String cTitle) {
        super(pid, author, cTitle);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) { this.content = content; }

}
