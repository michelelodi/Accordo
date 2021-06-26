package com.accordo.data.roomDB;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface ProfilePictureDao {
    @Query("SELECT pversion FROM profile_picture WHERE uid LIKE :uid")
    String getVersion(String uid);

    @Query("SELECT img FROM profile_picture WHERE uid LIKE :uid")
    String getPicture(String uid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(ProfilePicture profilePicture);
}
