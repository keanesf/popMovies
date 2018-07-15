package com.keanesf.popmovies.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/**
 * {@link Dao} which provides an api for all data operations
 */
@Dao
public interface FavoriteDao {

    @Query("SELECT * from favorite")
    List<FavoriteEntry> getAll();

    @Query("Select * from favorite where id =  :id")
    FavoriteEntry getById(long id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteEntry favoriteEntry);

    @Delete
    void delete(FavoriteEntry favoriteEntry);

}
