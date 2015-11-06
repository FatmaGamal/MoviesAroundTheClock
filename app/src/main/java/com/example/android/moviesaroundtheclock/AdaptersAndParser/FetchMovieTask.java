package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Extra;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Movie;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.DetailsFragment;
import com.example.android.moviesaroundtheclock.MainActivity;
import com.example.android.moviesaroundtheclock.MovieFragment;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by FATMA on 07-Sep-15.
 */
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Extra>> {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    private String type = "";
    private String movieId = "";
    public static Context mContext;

    String API_KEY = "82a122b4eb7d23a3e1a17973ea62fcc4";

    String moviesJsonStr = null;

    public FetchMovieTask() {
    }

    public FetchMovieTask(long movieId, String type) {
        this.type = type;
        this.movieId = String.valueOf(movieId);
    }

    public FetchMovieTask(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    protected void onPostExecute(ArrayList<Extra> extras) {
        if (!extras.isEmpty()) {
            if (extras.get(0) instanceof Trailer) {
                DetailsFragment.trailers.clear();
                DetailsFragment.trailers.addAll(extras);
                DetailsFragment.trailerAdapter.notifyDataSetChanged();
            } else if (extras.get(0) instanceof Review) {
                DetailsFragment.reviews.clear();
                DetailsFragment.reviews.addAll(extras);
                DetailsFragment.reviewAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    protected ArrayList<Extra> doInBackground(String... params) {
        final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        Log.v("FetchMovieTask", "params.length = " + params.length);
        if (params.length == 0) {
            params[0] = "@string/movie_sort_default";
        }

        try {
            final String MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String QUERY_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";


            final String CERTAIN_MOVIE_BASE_URL =
                    "http://api.themoviedb.org/3/movie?";
            final String REVIEWS_PARAM = "reviews";
            final String TRAILERS_PARAM = "trailers";

            Uri builtUri;
            URL url;
            switch (type) {
                case "reviews": {
                    Log.v("FetchMovieTask", "reviews : building uri");
                    builtUri = Uri.parse(CERTAIN_MOVIE_BASE_URL).buildUpon()
                            .appendPath(movieId)
                            .appendPath(REVIEWS_PARAM)
                            .appendQueryParameter(KEY_PARAM, API_KEY)
                            .build();
                    url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    break;
                }
                case "trailers": {
                    Log.v("FetchMovieTask", "trailers : building uri");
                    builtUri = Uri.parse(CERTAIN_MOVIE_BASE_URL).buildUpon()
                            .appendPath(movieId)
                            .appendPath(TRAILERS_PARAM)
                            .appendQueryParameter(KEY_PARAM, API_KEY)
                            .build();
                    url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setDoOutput(false);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    break;
                }
                default: {
                    Log.v("FetchMovieTask", "movie : building base uri");
                    builtUri = Uri.parse(MOVIE_BASE_URL).buildUpon()
                            .appendQueryParameter(QUERY_PARAM, params[0])
                            .appendQueryParameter(KEY_PARAM, API_KEY)
                            .build();
                    url = new URL(builtUri.toString());
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    break;
                }
            }


            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                moviesJsonStr = null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                moviesJsonStr = null;
            }
            moviesJsonStr = buffer.toString();

            Log.v("LOG_TAG", moviesJsonStr);

        } catch (IOException e) {
            Log.e("LOG_TAG", "Error ", e);
            moviesJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("LOG_TAG", "Error closing stream", e);
                }
            }
        }
        try {
            switch (type) {
                case "trailers": {
                    Log.v("FetchMovieTask", "trailers : getting Data");
                    return MovieDataParser.getTrailersDataFromJson(moviesJsonStr);
                }
                case "reviews": {
                    Log.v("FetchMovieTask", "reviews : getting data");
                    return MovieDataParser.getReviewsDataFromJson(moviesJsonStr);
                }
                default: {
                    Log.v("FetchMovieTask", "movie : getting data");
                    MovieDataParser.getMovieDataFromJson(moviesJsonStr, params[0]);
                    return new ArrayList<Extra>();
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
