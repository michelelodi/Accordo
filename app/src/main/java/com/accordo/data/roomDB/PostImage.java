package com.accordo.data.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "post_image")
public class PostImage {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "post_id")
    private String pid;

    @ColumnInfo(name = "img")
    private String img;

    public PostImage(String pid, String img) {
        this.pid = pid;
        this.img = img;
    }

    public String getPid() { return pid; }

    public String getImg() { return img; }
}
