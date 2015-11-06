package com.example.android.moviesaroundtheclock;

import android.annotation.TargetApi;
import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.commonsware.cwac.merge.MergeAdapter;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.FetchMovieTask;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.MovieAdapter;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.ReviewsAdapter;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.TrailersAdapter;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Extra;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.Data.MoviesProvider;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailsFragment extends Fragment {

    public static ArrayList<Extra> trailers;
    public static ArrayList<Extra> reviews;

    Cursor cursor;
    private Uri mUri;
    private Boolean isFav = false;
    static public String sortby;

    static final String FAV_ARG = "fav";
    static final String URI_ARG = "URI";

    private static final String[] POPULAR_MOVIE_COLUMNS = {
            MoviesContract.PopularMovieEntry.TABLE_NAME + "." + MoviesContract.PopularMovieEntry._ID,
            MoviesContract.PopularMovieEntry.COLUMN_TITLE,
            MoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.PopularMovieEntry.COLUMN_OVERVIEW,
            MoviesContract.PopularMovieEntry.COLUMN_AVERAGE_VOTE,
            MoviesContract.PopularMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID};


    private static final String[] MOVIE_RATINGS_COLUMNS = {
            MoviesContract.MovieRatingsEntry.TABLE_NAME + "." + MoviesContract.MovieRatingsEntry._ID,
            MoviesContract.MovieRatingsEntry.COLUMN_TITLE,
            MoviesContract.MovieRatingsEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieRatingsEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieRatingsEntry.COLUMN_AVERAGE_VOTE,
            MoviesContract.MovieRatingsEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID};

    private static final String[] FAVOURITE_MOVIES_COLUMNS = {
            MoviesContract.FavMovieEntry.TABLE_NAME + "." + MoviesContract.FavMovieEntry._ID,
            MoviesContract.FavMovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.FavMovieEntry.COLUMN_TITLE,
            MoviesContract.FavMovieEntry.COLUMN_OVERVIEW,
            MoviesContract.FavMovieEntry.COLUMN_AVERAGE_VOTE,
            MoviesContract.FavMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID};

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_RELEASE_DATE = 2;
    public static final int COL_TITLE = 1;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_AVERAGE_VOTE = 4;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_MOVIE_ID = 6;

    ListView detailsList;
    public static TrailersAdapter trailerAdapter;
    public static ReviewsAdapter reviewAdapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(URI_ARG);
            isFav = arguments.getBoolean(FAV_ARG);
        }


        Log.v("DetailsFragment", "got arguments");
        String movieId = "";
        long movieKey = 0;
        sortby = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_default));
        if (isFav) {
            movieId = String.valueOf(MoviesContract.FavMovieEntry.getMovieIDFromUri(mUri));
            cursor = getActivity().getContentResolver().query(MoviesContract.FavMovieEntry.CONTENT_URI,
                    null,
                    MoviesContract.FavMovieEntry._ID + " = ?",
                    new String[]{movieId}, null);
            movieKey = MoviesProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId), isFav);
            Log.d("grid", "fav movieKey: " + movieKey);
        } else {
            switch (sortby) {
                case MoviesContract.POPULARITY: {
                    movieId = String.valueOf(MoviesContract.PopularMovieEntry.getMovieIDFromUri(mUri));
                    Log.v("DetailsFragment", "Popular : movieId to get the row id from = " + movieId);
                    cursor = getActivity().getContentResolver().query(MoviesContract.PopularMovieEntry.CONTENT_URI,
                            null,
                            MoviesContract.PopularMovieEntry._ID + " = ?",
                            new String[]{movieId}, null);

                    movieKey = MoviesProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId), isFav);
                    Log.v("DetailsFragment", "Popular : moviekey which is row id of the movie to get data from = " + String.valueOf(movieKey));
                    break;
                }
                case MoviesContract.HIGHEST_RATED: {
                    movieId = String.valueOf(MoviesContract.MovieRatingsEntry.getMovieIDFromUri(mUri));
                    Log.v("DetailsFragment", "Ratings : movieId to get the row id from = " + movieId);
                    cursor = getActivity().getContentResolver().query(MoviesContract.MovieRatingsEntry.CONTENT_URI,
                            null,
                            MoviesContract.MovieRatingsEntry._ID + " = ?",
                            new String[]{movieId}, null);
                    movieKey = MoviesProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId), isFav);
                    Log.v("DetailsFragment", "Ratings : moviekey which is row id of the movie to get data from = " + String.valueOf(movieKey));
                    break;
                }
            }
        }

        try {
            FetchMovieTask task = new FetchMovieTask(movieKey, "trailers");
            task.execute(sortby);
            task = new FetchMovieTask(movieKey, "reviews");
            task.execute(sortby);
        } catch (RuntimeException ex) {
            Log.v("MovieDataParser", "runtime exception, error in logic");
        } catch (Exception ex) {
            Log.v("MovieDataParser", "error in task execution");
        }
        trailers = new ArrayList<Extra>();
        reviews = new ArrayList<Extra>();

        detailsList = (ListView) rootView.findViewById(R.id.details_listview);

        trailerAdapter = new TrailersAdapter(getActivity(), R.id.movieitem_imageview, trailers);
        reviewAdapter = new ReviewsAdapter(getActivity(), R.id.movieitem_imageview, reviews);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(new MovieAdapter(getActivity(), cursor, 0));
        mergeAdapter.addView(LayoutInflater.from(getActivity()).inflate(R.layout.movieitem_listview_headers_trailers, container, false));
        mergeAdapter.addAdapter(trailerAdapter);
        mergeAdapter.addView(LayoutInflater.from(getActivity()).inflate(R.layout.movieitem_listview_headers_reviews, container, false));
        mergeAdapter.addAdapter(reviewAdapter);
        detailsList.setAdapter(mergeAdapter);


        Log.v("DetailsFragment", "Merge Adapter set");

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
