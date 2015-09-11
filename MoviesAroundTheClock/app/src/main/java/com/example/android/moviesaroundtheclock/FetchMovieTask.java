package com.example.android.moviesaroundtheclock;

import android.annotation.TargetApi;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

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
public class FetchMovieTask extends AsyncTask<String, Void, ArrayList<Movie>>{
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;

    String moviesJsonStr = null;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onPostExecute(final ArrayList<Movie> strings) {
        super.onPostExecute(strings);

        MainActivity.arrAdapter.clear();
        MainActivity.arrAdapter.addAll(strings);

        MainActivity.arrAdapter.notifyDataSetChanged();
    }

    @Override
    protected ArrayList<Movie> doInBackground(String... params) {
        final String LOG_TAG = FetchMovieTask.class.getSimpleName();

        if (params.length == 0) {
            params[0] = "@string/movie_sort_default" ;
        }

        try {
            final String FORECAST_BASE_URL =
                    "http://api.themoviedb.org/3/discover/movie?";
            final String QUERY_PARAM = "sort_by";
            final String KEY_PARAM = "api_key";

            Uri builtUri = Uri.parse(FORECAST_BASE_URL).buildUpon()
                    .appendQueryParameter(QUERY_PARAM, params[0])
                    .appendQueryParameter(KEY_PARAM, API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

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
                return MovieDataParser.getMovieDataFromJson(moviesJsonStr);
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        return null;

    }

}
