package com.accordo.data;

public abstract class Post {

    private String pid, author, cTitle;

    public Post(String pid, String author, String cTitle){
        this.pid = pid;
        this.author = author;
        this.cTitle = cTitle;
    }

    public String getAuthor() {return author;};

    public abstract String getContent();

    public abstract void setContent(String content);

    @Override
    public String toString() {
        return "Post{" +
                "pid='" + pid + '\'' +
                ", author='" + author + '\'' +
                ", cTitle='" + cTitle + '\'' +
                '}';
    }
}
