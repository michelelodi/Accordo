package com.accordo.data;


public class ImagePost extends Post{

    private String content;

    public ImagePost(String pid, String authorUid, String author, String cTitle, String pversion) {
        super(pid, authorUid, author, cTitle, pversion);
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public void setContent(String content) { this.content = content; }

}
