package com.keanesf.popmovies.data.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

/**
 * {@link Dao} which provides an api for all data operations
 */
@Dao
public interface FavoriteDao {


    @Query("SELECT * FROM favorite WHERE id = :id")
    FavoriteEntry getFavoriteById(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void bulkInsert(FavoriteEntry... favorite);

}
