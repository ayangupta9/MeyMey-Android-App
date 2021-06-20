package com.ayandev.meymey.databasehelperclasses;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.ayandev.meymey.MemeModel;

@Database(entities = MemeModel.class, version = 1)
public abstract class MemeDatabase extends RoomDatabase {
    private static MemeDatabase instance;

    public abstract MemeDao memeDao();

    public static synchronized MemeDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(), MemeDatabase.class, "meme_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
