package com.accordo.data;

import org.jetbrains.annotations.NotNull;


public abstract class Post {

    private final String pid, author, cTitle, authorUid, pversion;

    public Post(String pid, String authorUid, String author, String cTitle, String pversion){
        this.pid = pid;
        this.author = author;
        this.cTitle = cTitle;
        this.authorUid = authorUid;
        this.pversion = pversion;
    }

    public String getAuthor() {return author;}

    public String getAuthorUid() { return authorUid; }

    public abstract String getContent();

    public String getCTitle() {return cTitle;}

    public String getPid() { return pid; }

    public String getPVersion() { return pversion; }

    public abstract void setContent(String content);

    @Override
    public @NotNull String toString() {
        return "Post{" + "pid='" + pid + '\'' + ", author='" + author + '\'' + ", cTitle='" + cTitle + '\'' + '}';
    }
}
