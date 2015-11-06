package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.content.ContentValues;
import android.util.Log;

import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Extra;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Movie;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.DetailsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by FATMA on 07-Sep-15.
 */
public class MovieDataParser {
    static final String LOG_TAG = MovieDataParser.class.getSimpleName();


    protected static Void getMovieDataFromJson(String movieJsonStr, String sortby)
            throws JSONException, ParseException {

        //Log.v("Parser from Fetch", movieJsonStr);


        final String M_RESULTS = "results";
        final String M_OVERVIEW = "overview";
        final String M_RELEASEDATE = "release_date";
        final String M_POSTERPATH = "poster_path";
        final String M_TITLE = "title";
        final String M_VOTEAVERAGE = "vote_average";
        final String M_ID = "id";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        if(movieJson != null){
        JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);

        //Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Movie> movieResultStrs = new ArrayList<Movie>();
        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String overview;
            String release_date;
            double vote_average;
            String poster;
            Long id;

            JSONObject movieItem = movieArray.getJSONObject(i);

            //Log.v("Parser movieItem.toSt", movieItem.toString());

            title = movieItem.getString(M_TITLE);
            overview = movieItem.getString(M_OVERVIEW);
            release_date = movieItem.getString(M_RELEASEDATE);
            vote_average = movieItem.getDouble(M_VOTEAVERAGE);
            poster = movieItem.getString(M_POSTERPATH);
            id = movieItem.getLong(M_ID);

            Movie m = new Movie();
            m.setmTitle(title);
            m.setmOverview(overview);
            m.setmVoteAverage(vote_average);
            m.setmReleaseDate(release_date);
            m.setmPoster(poster);
            m.setmId(id);
            movieResultStrs.add(m);

            Vector<ContentValues> cVVector = new Vector<ContentValues>(movieArray.length());
            ContentValues movieValues = new ContentValues();
            switch (sortby) {
                case MoviesContract.POPULARITY: {
                    movieValues.put(MoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE, m.getmReleaseDate());
                    movieValues.put(MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID, m.getmId());
                    movieValues.put(MoviesContract.PopularMovieEntry.COLUMN_AVERAGE_VOTE, m.getmVoteAverage());
                    movieValues.put(MoviesContract.PopularMovieEntry.COLUMN_POSTER_PATH, m.getmPoster());
                    movieValues.put(MoviesContract.PopularMovieEntry.COLUMN_TITLE, m.getmTitle());
                    movieValues.put(MoviesContract.PopularMovieEntry.COLUMN_OVERVIEW, m.getmOverview());
                    break;
                }
                case MoviesContract.HIGHEST_RATED: {
                    movieValues.put(MoviesContract.MovieRatingsEntry.COLUMN_RELEASE_DATE, m.getmReleaseDate());
                    movieValues.put(MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID, m.getmId());
                    movieValues.put(MoviesContract.MovieRatingsEntry.COLUMN_AVERAGE_VOTE, m.getmVoteAverage());
                    movieValues.put(MoviesContract.MovieRatingsEntry.COLUMN_POSTER_PATH, m.getmPoster());
                    movieValues.put(MoviesContract.MovieRatingsEntry.COLUMN_TITLE, m.getmTitle());
                    movieValues.put(MoviesContract.MovieRatingsEntry.COLUMN_OVERVIEW, m.getmOverview());
                    break;
                }
            }
            cVVector.add(movieValues);


            int inserted = 0;
            if (cVVector.size() > 0) {
                ContentValues[] cVArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cVArray);
                switch (sortby) {
                    case MoviesContract.POPULARITY: {
                        inserted = FetchMovieTask.mContext.getContentResolver().bulkInsert(MoviesContract.PopularMovieEntry.CONTENT_URI, cVArray);
                        break;
                    }
                    case MoviesContract.HIGHEST_RATED: {
                        inserted = FetchMovieTask.mContext.getContentResolver().bulkInsert(MoviesContract.MovieRatingsEntry.CONTENT_URI, cVArray);
                        break;
                    }
                }
                Log.d("grid", "FetchMoviesTask complete " + inserted + " inserted");
            }
        }
        }
        return null;
    }

    protected static ArrayList<Extra> getTrailersDataFromJson(String trailerJsonStr)
            throws JSONException, ParseException {


        //Log.v("Parser from Fetch", movieJsonStr);

        final String T_RESULTS = "youtube";
        final String T_KEY = "source";
        final String T_NAME = "name";
        //final String T_ID = "id";

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray(T_RESULTS);

        //Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Extra> trailerResultStrs = new ArrayList<Extra>();
        for (int i = 0; i < trailerArray.length(); i++) {
            String name;
            String key;
            //Long id;

            JSONObject trailerItem = trailerArray.getJSONObject(i);

            //Log.v("Parser movieItem.toSt", movieItem.toString());

            name = trailerItem.getString(T_NAME);
            key = trailerItem.getString(T_KEY);
            //id = trailerItem.getLong(T_ID);

            Extra t = new Trailer();
            t.settName(name);
            t.settKey(key);
            //t.settId(id);
            trailerResultStrs.add(t);
        }

        //DetailsFragment.trailers = trailerResultStrs;
        return trailerResultStrs;

    }

    protected static ArrayList<Extra> getReviewsDataFromJson(String reviewJsonStr)
            throws JSONException, ParseException {

        //Log.v("Parser from Fetch", movieJsonStr);

        final String R_RESULTS = "results";
        final String R_ID = "id";
        final String R_AUTHOR = "author";
        final String R_BODY = "content";
        final String R_URL = "url";

        JSONObject reviewJson = new JSONObject(reviewJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(R_RESULTS);

        //Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Extra> reviewResultStrs = new ArrayList<Extra>();
        for (int i = 0; i < reviewArray.length(); i++) {
            String author;
            String body;
            String url;
            String id;

            JSONObject reviewItem = reviewArray.getJSONObject(i);

            //Log.v("Parser movieItem.toSt", movieItem.toString());

            author = reviewItem.getString(R_AUTHOR);
            body = reviewItem.getString(R_BODY);
            id = reviewItem.getString(R_ID);
            url = reviewItem.getString(R_URL);

            Extra r = new Review();
            r.setrAuthor(author);
            r.setrBody(body);
            r.setrId(id);
            r.setrUrl(url);
            reviewResultStrs.add(r);
        }

        //DetailsFragment.reviews = reviewResultStrs;
        return reviewResultStrs;

    }


}
