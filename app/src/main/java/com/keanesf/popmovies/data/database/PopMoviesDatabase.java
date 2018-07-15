/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.keanesf.popmovies.data.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;
import android.util.Log;

/**
 * {@link PopMoviesDatabase} database for the application including a table for {@link FavoriteEntry}
 * with the DAO {@link FavoriteDao}.
 */

// List of the entry classes and associated TypeConverters
@Database(entities = {FavoriteEntry.class}, version = 3)
public abstract class PopMoviesDatabase extends RoomDatabase {

    private static final String LOG_TAG = PopMoviesDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "favorite";

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static PopMoviesDatabase sInstance;

    public static PopMoviesDatabase getInstance(Context context) {
        Log.d(LOG_TAG, "Getting the database");
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        PopMoviesDatabase.class, PopMoviesDatabase.DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
                Log.d(LOG_TAG, "Made new database");
            }
        }
        return sInstance;
    }

    // The associated DAOs for the database
    public abstract FavoriteDao favoriteDao();
}
