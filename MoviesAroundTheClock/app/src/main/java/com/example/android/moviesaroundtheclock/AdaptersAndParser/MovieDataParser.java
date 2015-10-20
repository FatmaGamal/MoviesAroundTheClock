package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Movie;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by FATMA on 07-Sep-15.
 */
public class MovieDataParser {
    static final String LOG_TAG = MovieDataParser.class.getSimpleName();


    protected static ArrayList<Movie> getMovieDataFromJson(String movieJsonStr)
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
        JSONArray movieArray = movieJson.getJSONArray(M_RESULTS);

        //Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Movie> resultStrs = new ArrayList<Movie>();
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
            resultStrs.add(m);

            FetchMovieTask task = new FetchMovieTask(id, "trailers");
            task.execute();
            task = new FetchMovieTask(id, "reviews");
            task.execute();
        }

        return resultStrs;

    }

    protected static ArrayList<Trailer> getTrailersDataFromJson(String trailerJsonStr)
            throws JSONException, ParseException {


        //Log.v("Parser from Fetch", movieJsonStr);

        final String T_RESULTS = "results";
        final String T_KEY = "key";
        final String T_NAME = "name";
        final String T_ID = "id";

        JSONObject trailerJson = new JSONObject(trailerJsonStr);
        JSONArray trailerArray = trailerJson.getJSONArray(T_RESULTS);

        //Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Trailer> resultStrs = new ArrayList<Trailer>();
        for (int i = 0; i < trailerArray.length(); i++) {
            String name;
            String key;
            Long id;

            JSONObject trailerItem = trailerArray.getJSONObject(i);

            //Log.v("Parser movieItem.toSt", movieItem.toString());

            name = trailerItem.getString(T_NAME);
            key = trailerItem.getString(T_KEY);
            id = trailerItem.getLong(T_ID);

            Trailer t = new Trailer();
            t.settName(name);
            t.settKey(key);
            t.settId(id);
            resultStrs.add(t);
        }

        return resultStrs;

    }

    protected static ArrayList<Review> getReviewsDataFromJson(String reviewJsonStr)
            throws JSONException, ParseException {

        //Log.v("Parser from Fetch", movieJsonStr);

        final String R_RESULTS = "results";
        final String R_ID = "id";
        final String R_AUTHOR = "author";
        final String R_BODY = "content";

        JSONObject reviewJson = new JSONObject(reviewJsonStr);
        JSONArray reviewArray = reviewJson.getJSONArray(R_RESULTS);

        //Log.v("Parse movieArray.toSt", movieArray.toString());

        ArrayList<Review> resultStrs = new ArrayList<Review>();
        for (int i = 0; i < reviewArray.length(); i++) {
            String author;
            String body;
            Long id;

            JSONObject reviewItem = reviewArray.getJSONObject(i);

            //Log.v("Parser movieItem.toSt", movieItem.toString());

            author = reviewItem.getString(R_AUTHOR);
            body = reviewItem.getString(R_BODY);
            id = reviewItem.getLong(R_ID);

            Review r = new Review();
            r.setrAuthor(author);
            r.setrBody(body);
            r.setrId(id);
            resultStrs.add(r);
        }

        return resultStrs;

    }


}
