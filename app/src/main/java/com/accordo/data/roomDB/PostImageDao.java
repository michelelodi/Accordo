package com.accordo.data.roomDB;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface PostImageDao {

    @Query("SELECT * FROM post_image")
    List<PostImage> getAll();

    @Query("SELECT img FROM post_image WHERE post_id LIKE :pid")
    String get(String pid);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PostImage postImage);
}
