
package com.example.android.moviesaroundtheclock.Data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

public class MoviesProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDbHelper mOpenHelper;

    static final int MOVIE_POPULAR = 100;
    private static final int MOVIE_POPULAR_ITEM_N = 101;

    static final int MOVIE_RATINGS = 200;
    private static final int MOVIE_RATINGS_ITEM_N = 201;

    private static final int MOVIE_REVIEW_WITH_ID = 400;
    private static final int MOVIE_TRAILER_WITH_ID = 401;

    //private static final SQLiteQueryBuilder MovieBuilder;

    /*static{
        MovieBuilder = new SQLiteQueryBuilder();

        //This is an inner join which looks like
        //weather INNER JOIN location ON weather.location_id = location._id
        MovieBuilder.setTables(
                MoviesContract.PopularMovieEntry.TABLE_NAME + " INNER JOIN " +
                        MoviesContract.LocationEntry.TABLE_NAME +
                        " ON " + MoviesContract.WeatherEntry.TABLE_NAME +
                        "." + MoviesContract.WeatherEntry.COLUMN_LOC_KEY +
                        " = " + MoviesContract.LocationEntry.TABLE_NAME +
                        "." + MoviesContract.LocationEntry._ID);
    }*/
/*
    //location.location_setting = ?
    private static final String sLocationSettingSelection =
            MoviesContract.MovieRatingsEntry.TABLE_NAME+
                    "." + MoviesContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? ";

    //location.location_setting = ? AND date >= ?
    private static final String sLocationSettingWithStartDateSelection =
            MoviesContract.LocationEntry.TABLE_NAME+
                    "." + MoviesContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    MoviesContract.WeatherEntry.COLUMN_DATE + " >= ? ";

    //location.location_setting = ? AND date = ?
    private static final String sLocationSettingAndDaySelection =
            MoviesContract.LocationEntry.TABLE_NAME +
                    "." + MoviesContract.LocationEntry.COLUMN_LOCATION_SETTING + " = ? AND " +
                    MoviesContract.WeatherEntry.COLUMN_DATE + " = ? ";*/

    /*private Cursor getMoviesList(Uri uri, String[] projection, String sortOrder) {
        //String locationSetting = MoviesContract.WeatherEntry.getLocationSettingFromUri(uri);
        //long startDate = MoviesContract.WeatherEntry.getStartDateFromUri(uri);

        String[] selectionArgs;
        String selection;

        /*if (startDate == 0) {
            selection = sLocationSettingSelection;
            selectionArgs = new String[]{locationSetting};
        } else {
            selectionArgs = new String[]{locationSetting, Long.toString(startDate)};
            selection = sLocationSettingWithStartDateSelection;
        }

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCertainMovie(
            Uri uri, String[] projection, String sortOrder) {
        String locationSetting = MoviesContract.WeatherEntry.getLocationSettingFromUri(uri);
        long date = MoviesContract.WeatherEntry.getDateFromUri(uri);

        return sWeatherByLocationSettingQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sLocationSettingAndDaySelection,
                new String[]{locationSetting, Long.toString(date)},
                null,
                null,
                sortOrder
        );
    }*/

    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MoviesContract.PATH_POPULAR, MOVIE_POPULAR);
        matcher.addURI(authority, MoviesContract.PATH_POPULAR + "/#", MOVIE_POPULAR_ITEM_N);
        //supposed to get an item according to their title, will see about that later
        //matcher.addURI(authority, MoviesContract.PATH_POPULAR + "/*", MOVIE_POPULAR_ITEM_S);

        matcher.addURI(authority, MoviesContract.PATH_RATING, MOVIE_RATINGS);
        matcher.addURI(authority, MoviesContract.PATH_RATING + "/#", MOVIE_RATINGS_ITEM_N);
        //supposed to get an item according to their title, will see about that later
        //matcher.addURI(authority, MoviesContract.PATH_RATING + "/*", MOVIE_RATINGS_ITEM_S);

        //matcher.addURI(authority, MoviesContract.PATH_RATING, MOVIE_RATINGS);
        //matcher.addURI(authority, MoviesContract.PATH_RATING + "/#", MOVIE_RATINGS_ITEM_N);
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
            case MOVIE_POPULAR_ITEM_N:
                return MoviesContract.PopularMovieEntry.CONTENT_ITEM_TYPE;
            case MOVIE_REVIEW_WITH_ID:
                return MoviesContract.MovieRatingsEntry.CONTENT_ITEM_TYPE;
            case MOVIE_TRAILER_WITH_ID:
                return MoviesContract.MovieRatingsEntry.CONTENT_ITEM_TYPE;
            //case MOVIE_REVIEW_WITH_ID:

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
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
            case MOVIE_POPULAR_ITEM_N: {
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
/*            case MOVIE_POPULAR_ITEM_S: {
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
            }*/
            case MOVIE_RATINGS_ITEM_N: {
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
/*            case MOVIE_RATINGS_ITEM_S: {
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
            }*/

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //something that has to do with content observer, don't know what it is yet so taking a look later
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }
    /*
    //will be used in case of favourites
            @Override
            public Uri insert(Uri uri, ContentValues values) {
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                final int match = sUriMatcher.match(uri);
                Uri returnUri;

                switch (match) {
                    case WEATHER: {
                        normalizeDate(values);
                        long _id = db.insert(MoviesContract.WeatherEntry.TABLE_NAME, null, values);
                        if ( _id > 0 )
                            returnUri = MoviesContract.WeatherEntry.buildWeatherUri(_id);
                        else
                            throw new android.database.SQLException("Failed to insert row into " + uri);
                        break;
                    }
                    case LOCATION: {
                        long _id = db.insert(MoviesContract.LocationEntry.TABLE_NAME, null, values);
                        if ( _id > 0 )
                            returnUri = MoviesContract.LocationEntry.buildLocationUri(_id);
                        else
                            throw new android.database.SQLException("Failed to insert row into " + uri);
                        break;
                    }
                    default:
                        throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnUri;
            }

    //will be used in case of favourites to remove a movie from favourites
            @Override
            public int delete(Uri uri, String selection, String[] selectionArgs) {
                final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
                final int match = sUriMatcher.match(uri);
                int rowsDeleted;
                // this makes delete all rows return the number of rows deleted
                if ( null == selection ) selection = "1";
                switch (match) {
                    case WEATHER:
                        rowsDeleted = db.delete(
                                MoviesContract.WeatherEntry.TABLE_NAME, selection, selectionArgs);
                        break;
                    case LOCATION:
                        rowsDeleted = db.delete(
                                MoviesContract.LocationEntry.TABLE_NAME, selection, selectionArgs);
                        break;
                    default:
                        throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
                // Because a null deletes all rows
                if (rowsDeleted != 0) {
                    getContext().getContentResolver().notifyChange(uri, null);
                }
                return rowsDeleted;
            }

        @Override
        public int update(
                Uri uri, ContentValues values, String selection, String[] selectionArgs) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            int rowsUpdated;

            switch (match) {
                case WEATHER:
                    normalizeDate(values);
                    rowsUpdated = db.update(MoviesContract.WeatherEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                case LOCATION:
                    rowsUpdated = db.update(MoviesContract.LocationEntry.TABLE_NAME, values, selection,
                            selectionArgs);
                    break;
                default:
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
            if (rowsUpdated != 0) {
                getContext().getContentResolver().notifyChange(uri, null);
            }
            return rowsUpdated;
        }

    //in case of multiple rows
        @Override
        public int bulkInsert(Uri uri, ContentValues[] values) {
            final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
            final int match = sUriMatcher.match(uri);
            switch (match) {
                case WEATHER:
                    db.beginTransaction();
                    int returnCount = 0;
                    try {
                        for (ContentValues value : values) {
                            normalizeDate(value);
                            long _id = db.insert(MoviesContract.WeatherEntry.TABLE_NAME, null, value);
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
                default:
                    return super.bulkInsert(uri, values);
            }
        }*/

        // You do not need to call this method. This is a method specifically to assist the testing
        // framework in running smoothly. You can read more at:
        // http://developer.android.com/reference/android/content/ContentProvider.html#shutdown()
        @Override
        @TargetApi(11)
        public void shutdown() {
            mOpenHelper.close();
            super.shutdown();
        }
    }


