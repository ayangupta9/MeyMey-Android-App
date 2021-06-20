package com.ayandev.meymey;

import android.app.Application;
//import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.ayandev.meymey.databasehelperclasses.MemeDao;
import com.ayandev.meymey.databasehelperclasses.MemeDatabase;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FavouriteMemesViewModel extends AndroidViewModel {

    private LiveData<List<MemeModel>> allSavedMemes;
    private final MemeDao memeDao;

    public FavouriteMemesViewModel(@NonNull Application application) {
        super(application);
        MemeDatabase memeDatabase = MemeDatabase.getInstance(application);
        memeDao = memeDatabase.memeDao();
        allSavedMemes = memeDao.fetchAllSavedMemes();
    }

    public void insertMeme(MemeModel memeModelEntity) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                memeDao.insert(memeModelEntity);
            }
        });
        executorService.shutdown();
    }

    public void deleteMeme(MemeModel memeModelEntity) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                memeDao.delete(memeModelEntity);
            }
        });
        executorService.shutdown();
    }

    public LiveData<List<MemeModel>> fetchAllMemes() {
        return allSavedMemes;
    }

    public boolean memeExists(String url) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<Boolean> runFuture = executorService.submit(new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                return memeDao.memeExists(url);
            }
        });

        boolean returnValue = false;
        try {
            returnValue = runFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        return returnValue;
    }

    public MemeModel fetchSingleMeme(String url) {
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<MemeModel> runFuture = executorService.submit(new Callable<MemeModel>() {
            @Override
            public MemeModel call() throws Exception {
                return memeDao.fetchSingleMeme(url);
            }
        });

        MemeModel fetchedMeme = null;
        try {
            fetchedMeme = runFuture.get();
        } catch (Exception e) {
//            Log.d(TAG, "fetchSingleMeme: \n");
            e.printStackTrace();
        }
        return fetchedMeme;
    }

}
