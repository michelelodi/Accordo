package com.accordo.data.roomDB;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {PostImage.class, ProfilePicture.class}, version = 1, exportSchema = false)
public abstract class AccordoDB extends RoomDatabase {
    public abstract PostImageDao postImageDao();
    public abstract  ProfilePictureDao profilePictureDao();

    private static volatile AccordoDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    static AccordoDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AccordoDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AccordoDB.class, "accordo_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
