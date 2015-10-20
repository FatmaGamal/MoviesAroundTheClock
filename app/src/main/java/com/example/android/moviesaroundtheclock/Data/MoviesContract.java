
package com.example.android.moviesaroundtheclock.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Defines table and column names for the movies database.
 */
public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "com.example.android.moviesaroundtheclock";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_POPULAR = "POPULAR_MOVIES";
    public static final String PATH_RATING = "RATED_MOVIES";
    public static final String PATH_FAV = "FAVOURITE_MOVIES";

    /* Inner class that defines the table contents of the Highest-Rated Movies table */
    public static final class MovieRatingsEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_RATING).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATING;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_RATING;

        public static final String TABLE_NAME = "RATED_MOVIES";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_AVERAGE_VOTE = "vote";
        public static final String COLUMN_POSTER_PATH = "poster";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TRAILERS = "trailer";
        public static final String COLUMN_REVIEWS = "review";
        //public static final String COLUMN_IS_FAVOURITE = "favourite";


        public static Uri buildCertainMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


        public static Uri buildMoviesUri() {
            return CONTENT_URI;
        }


        public static int getMovieIDFromUri(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
/*
        public static long getMovieIDFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }*/
    }

    public static final class PopularMovieEntry implements BaseColumns {

        //base + authority (package name)+ path to required table in each state
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_POPULAR).build();

        //in case i was returning a table
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;
        //in case i was returning a certain row from the table
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_POPULAR;

        public static final String TABLE_NAME = "POPULAR_MOVIES";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_AVERAGE_VOTE = "vote";
        public static final String COLUMN_POSTER_PATH = "poster";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TRAILERS = "trailer";
        public static final String COLUMN_REVIEWS = "review";
        //public static final String COLUMN_IS_FAVOURITE = "isfavourite";


        public static Uri buildCertainMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesUri() {
            return CONTENT_URI;
        }


        public static long getMovieIDFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }
    }

    public static final class FavMovieEntry implements BaseColumns {

        //base + authority (package name)+ path to required table in each state
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAV).build();

        //in case i was returning a table
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV;
        //in case i was returning a certain row from the table
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAV;

        public static final String TABLE_NAME = "FAVOURITE_MOVIES";
        public static final String COLUMN_RELEASE_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_AVERAGE_VOTE = "vote";
        public static final String COLUMN_POSTER_PATH = "poster";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TRAILERS = "trailer";
        public static final String COLUMN_REVIEWS = "review";


        public static Uri buildCertainMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesUri() {
            return CONTENT_URI;
        }

        public static long getMovieIDFromUri(Uri uri){
            return ContentUris.parseId(uri);
        }

    }
}
