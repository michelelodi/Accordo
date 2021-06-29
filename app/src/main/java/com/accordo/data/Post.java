package com.accordo.data;

import android.graphics.Bitmap;

import org.jetbrains.annotations.NotNull;


public abstract class Post {

    private final String pid, author, cTitle;
    private Bitmap authorProfilePicture;

    public Post(String pid, String author, String cTitle, Bitmap authorProfilePicture){
        this.pid = pid;
        this.author = author;
        this.cTitle = cTitle;
        this.authorProfilePicture = authorProfilePicture;
    }

    public String getAuthor() {return author;}

    public abstract String getContent();

    public String getCTitle() {return cTitle;}

    public String getPid() { return pid; }

    public abstract void setContent(String content);

    @Override
    public @NotNull String toString() {
        return "Post{" + "pid='" + pid + '\'' + ", author='" + author + '\'' + ", cTitle='" + cTitle + '\'' + '}';
    }
}
