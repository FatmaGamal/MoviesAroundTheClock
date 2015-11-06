
package com.example.android.moviesaroundtheclock.Data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.preference.PreferenceManager;

import com.example.android.moviesaroundtheclock.DetailsFragment;
import com.example.android.moviesaroundtheclock.R;

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    static final int MOVIE_POPULAR = 100;
    private static final int MOVIE_POPULAR_WITH_ID = 101;

    static final int MOVIE_RATINGS = 200;
    private static final int MOVIE_HIGHEST_RATED_WITH_ID = 201;

    static final int MOVIE_FAV = 300;
    private static final int MOVIE_FAV_WITH_ID = 301;


    public static long getMovieKeyFromUri(Context context, long id, Boolean isFav) {
        Cursor cursor;
        if (isFav) {
            cursor = context.getContentResolver().query(MoviesContract.FavMovieEntry.CONTENT_URI,
                    new String[]{MoviesContract.FavMovieEntry._ID, MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID},
                    MoviesContract.FavMovieEntry._ID + " = ?",
                    new String[]{String.valueOf(id)}, null);
            if (cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndex(MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID);
                String key = cursor.getString(columnIndex);
                cursor.close();
                return Long.parseLong(key);
            } else {
                return 0;
            }
        } else {
            switch (DetailsFragment.sortby) {
                case MoviesContract.POPULARITY: {
                    cursor = context.getContentResolver().query(MoviesContract.PopularMovieEntry.CONTENT_URI,
                            new String[]{MoviesContract.PopularMovieEntry._ID, MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID},
                            MoviesContract.PopularMovieEntry._ID + " = ?",
                            new String[]{String.valueOf(id)}, null);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID);
                        String key = cursor.getString(columnIndex);
                        cursor.close();
                        return Long.parseLong(key);
                    } else {
                        return 0;
                    }

                }
                case MoviesContract.HIGHEST_RATED: {
                    cursor = context.getContentResolver().query(MoviesContract.MovieRatingsEntry.CONTENT_URI,
                            new String[]{MoviesContract.MovieRatingsEntry._ID, MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID},
                            MoviesContract.MovieRatingsEntry._ID + " = ?",
                            new String[]{String.valueOf(id)}, null);
                    if (cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndex(MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID);
                        String key = cursor.getString(columnIndex);
                        cursor.close();
                        return Long.parseLong(key);
                    } else {
                        return 0;
                    }
                }
            }
        }

        return 0;
    }


    public static boolean isFav(Context context, String movieKey) {
        boolean isFavorite = false;
        Cursor retCursor = context.getContentResolver().query(
                MoviesContract.FavMovieEntry.CONTENT_URI,
                new String[]{MoviesContract.FavMovieEntry._ID},
                MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieKey}, null);
        if (retCursor.moveToFirst() && retCursor.getCount() >= 1) {
            if (!retCursor.isNull(retCursor.getColumnIndex(MoviesContract.FavMovieEntry._ID)))
                isFavorite = true;
        }

        return isFavorite;
    }

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MoviesContract.PATH_POPULAR, MOVIE_POPULAR);
        matcher.addURI(authority, MoviesContract.PATH_RATING, MOVIE_RATINGS);
        matcher.addURI(authority, MoviesContract.PATH_FAV, MOVIE_FAV);

        matcher.addURI(authority, MoviesContract.PATH_POPULAR + "/#", MOVIE_POPULAR_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_RATING + "/#", MOVIE_HIGHEST_RATED_WITH_ID);
        matcher.addURI(authority, MoviesContract.PATH_FAV + "/#", MOVIE_FAV_WITH_ID);


        return matcher;
    }


    @Override
    public boolean onCreate() {
        mOpenHelper = new MoviesDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {

        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_POPULAR:
                return MoviesContract.PopularMovieEntry.CONTENT_TYPE;
            case MOVIE_RATINGS:
                return MoviesContract.MovieRatingsEntry.CONTENT_TYPE;
            case MOVIE_FAV:
                return MoviesContract.MovieRatingsEntry.CONTENT_TYPE;
            case MOVIE_HIGHEST_RATED_WITH_ID:
                return MoviesContract.MovieRatingsEntry.CONTENT_ITEM_TYPE;
            case MOVIE_POPULAR_WITH_ID:
                return MoviesContract.PopularMovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_FAV_WITH_ID:
                return MoviesContract.PopularMovieEntry.CONTENT_ITEM_TYPE;

            default:
                throw new UnsupportedOperationException("Unknown " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE_POPULAR: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.PopularMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_RATINGS: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieRatingsEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_FAV: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.FavMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case MOVIE_POPULAR_WITH_ID: {
                long _id = MoviesContract.PopularMovieEntry.getMovieIDFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.PopularMovieEntry.TABLE_NAME, projection,
                        MoviesContract.PopularMovieEntry._ID + " = ?",
                        new String[]{Long.toString(_id)},
                        null, null, sortOrder);
                break;
            }
            case MOVIE_HIGHEST_RATED_WITH_ID: {
                long _id = MoviesContract.MovieRatingsEntry.getMovieIDFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieRatingsEntry.TABLE_NAME, projection,
                        MoviesContract.MovieRatingsEntry._ID + " = ?",
                        new String[]{Long.toString(_id)}, null, null, sortOrder);
                break;
            }
            case MOVIE_FAV_WITH_ID: {
                long _id = MoviesContract.MovieRatingsEntry.getMovieIDFromUri(uri);
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MoviesContract.MovieRatingsEntry.TABLE_NAME, projection,
                        MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{Long.toString(_id)}, null, null, sortOrder);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIE_POPULAR: {
                long _id = db.insert(MoviesContract.PopularMovieEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.PopularMovieEntry.buildCertainMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed " + uri);
                break;
            }
            case MOVIE_RATINGS: {
                long _id = db.insert(MoviesContract.MovieRatingsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.MovieRatingsEntry.buildCertainMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed " + uri);
                break;
            }
            case MOVIE_FAV: {
                long _id = db.insert(MoviesContract.MovieRatingsEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MoviesContract.MovieRatingsEntry.buildCertainMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsDeleted;

        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case MOVIE_POPULAR:
                rowsDeleted = db.delete(MoviesContract.PopularMovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_RATINGS:
                rowsDeleted = db.delete(MoviesContract.MovieRatingsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case MOVIE_FAV:
                rowsDeleted = db.delete(MoviesContract.MovieRatingsEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown " + uri);
        }
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIE_POPULAR:
                rowsUpdated = db.update(MoviesContract.PopularMovieEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_RATINGS:
                rowsUpdated = db.update(MoviesContract.MovieRatingsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case MOVIE_FAV:
                rowsUpdated = db.update(MoviesContract.MovieRatingsEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE_POPULAR:
                return bulkInsertHelper(MoviesContract.PopularMovieEntry.TABLE_NAME, db, values, uri);
            case MOVIE_RATINGS:
                return bulkInsertHelper(MoviesContract.MovieRatingsEntry.TABLE_NAME, db, values, uri);
            case MOVIE_FAV:
                return bulkInsertHelper(MoviesContract.MovieRatingsEntry.TABLE_NAME, db, values, uri);
            default:
                return super.bulkInsert(uri, values);
        }
    }

    private int bulkInsertHelper(String tableName, SQLiteDatabase db, ContentValues[] values, Uri uri) {
        db.beginTransaction();
        int returnCount = 0;
        try {
            for (ContentValues value : values) {
                long _id = db.insert(tableName, null, value);
                if (_id != -1) {
                    returnCount++;
                }
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}


