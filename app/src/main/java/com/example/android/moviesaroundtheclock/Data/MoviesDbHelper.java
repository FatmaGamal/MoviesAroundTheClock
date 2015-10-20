/*
 * Copyright (C) 2014 The Android Open Source Project
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
package com.example.android.moviesaroundtheclock.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.moviesaroundtheclock.Data.MoviesContract.*;

/**
 * Manages a local database for weather data.
 */
public class MoviesDbHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "moviesDB.db";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_POPULAR_MOVIES_TABLE = "CREATE TABLE " + PopularMovieEntry.TABLE_NAME + " (" +
                PopularMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieRatingsEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                PopularMovieEntry.COLUMN_OVERVIEW + " TEXT, " +
                PopularMovieEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                PopularMovieEntry.COLUMN_AVERAGE_VOTE + " REAL, " +
                PopularMovieEntry.COLUMN_POSTER_PATH + " TEXT," +
                PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                PopularMovieEntry.COLUMN_TRAILERS + " TEXT NOT NULL," +
                PopularMovieEntry.COLUMN_REVIEWS + " TEXT NOT NULL" +
                //PopularMovieEntry.COLUMN_IS_FAVOURITE + " TEXT NOT NULL" +
                 ");";

        final String SQL_CREATE_RATED_MOVIES_TABLE = "CREATE TABLE " + MovieRatingsEntry.TABLE_NAME + " (" +
                MovieRatingsEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieRatingsEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieRatingsEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieRatingsEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieRatingsEntry.COLUMN_AVERAGE_VOTE + " REAL, " +
                MovieRatingsEntry.COLUMN_POSTER_PATH + " TEXT, " +
                PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                PopularMovieEntry.COLUMN_TRAILERS + " TEXT NOT NULL," +
                PopularMovieEntry.COLUMN_REVIEWS + " TEXT NOT NULL" +
                //PopularMovieEntry.COLUMN_IS_FAVOURITE + " TEXT NOT NULL" +
                " );";

        final String SQL_CREATE_FAV_MOVIES_TABLE = "CREATE TABLE " + FavMovieEntry.TABLE_NAME + " (" +
                FavMovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieRatingsEntry.COLUMN_TITLE + " TEXT NOT NULL," +
                MovieRatingsEntry.COLUMN_OVERVIEW + " TEXT, " +
                MovieRatingsEntry.COLUMN_RELEASE_DATE + " TEXT, " +
                MovieRatingsEntry.COLUMN_AVERAGE_VOTE + " REAL, " +
                MovieRatingsEntry.COLUMN_POSTER_PATH + " TEXT, " +
                PopularMovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                PopularMovieEntry.COLUMN_TRAILERS + " TEXT NOT NULL," +
                PopularMovieEntry.COLUMN_REVIEWS + " TEXT NOT NULL" +
                " );";


        sqLiteDatabase.execSQL(SQL_CREATE_POPULAR_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_RATED_MOVIES_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_FAV_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + PopularMovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieRatingsEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
