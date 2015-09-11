package com.example.android.moviesaroundtheclock;

import android.text.format.Time;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by FATMA on 07-Sep-15.
 */
public class MovieDataParser {
    static final String LOG_TAG = MovieDataParser.class.getSimpleName();


    protected static ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
            throws JSONException, ParseException {

        Log.v("Parser from Fetch", movieJsonStr);

        final String M_RESULTS = "results";
        final String M_OVERVIEW = "overview";
        final String M_RELEASEDATE = "release_date";
        final String M_POSTERPATH = "poster_path";
        final String M_TITLE = "title";
        final String M_VOTEAVERAGE = "vote_average";
        final String M_ID = "id";

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);

        Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Movie> resultStrs = new ArrayList<Movie>();
        for (int i = 0; i < movieArray.length(); i++) {
            String title;
            String overview;
            String release_date;
            double vote_average;
            String poster;
            Long id;

            JSONObject movieItem = movieArray.getJSONObject(i);

            Log.v("Parser movieItem.toSt", movieItem.toString());

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
            resultStrs.add(m);
        }

        return resultStrs;

    }


}
