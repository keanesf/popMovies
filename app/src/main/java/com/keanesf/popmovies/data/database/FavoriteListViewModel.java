package com.keanesf.popmovies.data.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class FavoriteListViewModel extends AndroidViewModel {

    private final LiveData<List<FavoriteEntry>> favoriteEntries;

    private PopMoviesDatabase popMoviesDatabase;

    public FavoriteListViewModel(Application application){
        super(application);

        popMoviesDatabase = PopMoviesDatabase.getInstance(this.getApplication());

        favoriteEntries = popMoviesDatabase.favoriteDao().getAll();
    }

    public LiveData<List<FavoriteEntry>> getFavoriteEntries() {
        return favoriteEntries;
    }
}
