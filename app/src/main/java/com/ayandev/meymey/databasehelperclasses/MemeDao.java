package com.ayandev.meymey.databasehelperclasses;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.ayandev.meymey.MemeModel;

import java.util.List;

@Dao
public interface MemeDao {

    @Insert
    public void insert(MemeModel memeModel);

    @Delete
    public void delete(MemeModel memeModel);

    @Query("SELECT * FROM savedmemes")
    public LiveData<List<MemeModel>> fetchAllSavedMemes();

    @Query("SELECT EXISTS (SELECT 1 FROM savedmemes WHERE url=:url)")
    public boolean memeExists(String url);

    @Query("SELECT * FROM savedmemes WHERE url=:url")
    public MemeModel fetchSingleMeme(String url);
}
