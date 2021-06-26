package com.accordo.data.roomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "profile_picture")
public class ProfilePicture {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "uid")
    private String uid;

    @ColumnInfo(name = "pversion")
    private String pversion;

    @ColumnInfo(name = "img")
    private String img;

    public ProfilePicture(String uid, String pversion, String img) {
        this.uid = uid;
        this.pversion = pversion;
        this.img = img;
    }

    @NonNull
    public String getUid() {
        return uid;
    }

    public String getPversion() {
        return pversion;
    }

    public String getImg() {
        return img;
    }
}
