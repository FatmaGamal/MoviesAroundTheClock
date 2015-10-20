package com.example.android.moviesaroundtheclock;

import android.annotation.TargetApi;
import android.app.Activity;/*
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;*/
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.commonsware.cwac.merge.MergeAdapter;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.MovieAdapter;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.ReviewsAdapter;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.TrailersAdapter;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.Data.MoviesProvider;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static ArrayList<Trailer> trailers;
    public static ArrayList<Review> reviews;

    private static final int MOVIES_LOADER = 0;
    Cursor cursor;
    private Uri mUri;
    static public String sortby;

    private static final String[] POPULAR_MOVIE_COLUMNS = {
            MoviesContract.PopularMovieEntry.TABLE_NAME + "." + MoviesContract.PopularMovieEntry._ID,
            MoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.PopularMovieEntry.COLUMN_TITLE,
            MoviesContract.PopularMovieEntry.COLUMN_OVERVIEW,
            MoviesContract.PopularMovieEntry.COLUMN_AVERAGE_VOTE,
            MoviesContract.PopularMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieRatingsEntry.COLUMN_REVIEWS,
            MoviesContract.MovieRatingsEntry.COLUMN_TRAILERS,
            MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID};


    private static final String[] MOVIE_RATINGS_COLUMNS = {
            MoviesContract.MovieRatingsEntry.TABLE_NAME + "." + MoviesContract.MovieRatingsEntry._ID,
            MoviesContract.MovieRatingsEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieRatingsEntry.COLUMN_TITLE,
            MoviesContract.MovieRatingsEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieRatingsEntry.COLUMN_AVERAGE_VOTE,
            MoviesContract.MovieRatingsEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieRatingsEntry.COLUMN_REVIEWS,
            MoviesContract.MovieRatingsEntry.COLUMN_TRAILERS,
            MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID};

    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_RELEASE_DATE = 1;
    public static final int COL_TITLE = 2;
    public static final int COL_OVERVIEW = 3;
    public static final int COL_AVERAGE_VOTE = 4;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_MOVIE_ID = 6;


    TextView titleTV;
    TextView overviewTV;
    TextView releaseTV;
    TextView averageTV;
    ImageView posterView;
    ListView detailsList;
    //ListView reviewList;
    TrailersAdapter trailerAdapter;
    ReviewsAdapter reviewAdapter;
    //MovieAdapter movieAdapter;


    public DetailsFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable("URI");
        }

        String movieId="";
        //String movieKey = MovieProvider.getMovieKeyFromUri(getActivity(), mUri, isFavorite);
        long movieKey=0;
        sortby = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_default));
        switch (sortby) {
            case "Most Popular": {
                movieId = String.valueOf(MoviesContract.PopularMovieEntry.getMovieIDFromUri(mUri));
                cursor = getActivity().getContentResolver().query(MoviesContract.PopularMovieEntry.CONTENT_URI,
                        null,
                        MoviesContract.PopularMovieEntry._ID + " = ?",
                        new String[]{movieId}, null);
                // TODO
                movieKey = MoviesProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId));
                break;
            }
            case "Highest-Rated": {
                movieId = String.valueOf(MoviesContract.MovieRatingsEntry.getMovieIDFromUri(mUri));
                cursor = getActivity().getContentResolver().query(MoviesContract.MovieRatingsEntry.CONTENT_URI,
                        null,
                        MoviesContract.MovieRatingsEntry._ID + " = ?",
                        new String[]{movieId}, null);
                movieKey = MoviesProvider.getMovieKeyFromUri(getActivity(), Long.parseLong(movieId));
                break;
            }
        }
        sortby = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_default));

        trailers = new ArrayList<Trailer>();
        reviews = new ArrayList<Review>();

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);
        detailsList = (ListView) rootView.findViewById(R.id.movie_layout);
        //reviewList = (ListView) rootView.findViewById(R.id.review_listview);

        trailerAdapter = new TrailersAdapter(getActivity(), R.id.movieitem_imageview, trailers);
        reviewAdapter = new ReviewsAdapter(getActivity(), R.id.movieitem_imageview, reviews);

        //trailerList.setAdapter(trailerAdapter);
        //reviewList.setAdapter(reviewAdapter);

        titleTV = (TextView) rootView.findViewById(R.id.movie_name);
        overviewTV = (TextView) rootView.findViewById(R.id.movie_overview);
        releaseTV = (TextView) rootView.findViewById(R.id.movie_release);
        averageTV = (TextView) rootView.findViewById(R.id.movie_vote);
        posterView = (ImageView) rootView.findViewById(R.id.movie_poster);

        MergeAdapter mergeAdapter = new MergeAdapter();
        mergeAdapter.addAdapter(new MovieAdapter(getActivity(), cursor, 0));
        mergeAdapter.addAdapter(trailerAdapter);
        mergeAdapter.addAdapter(reviewAdapter);
        detailsList.setAdapter(mergeAdapter);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundle) {
        if (null != mUri) {
            switch (sortby) {
                case "Most Popular": {
                    Uri moviesUri = MoviesContract.PopularMovieEntry.buildMoviesUri();
                    return new CursorLoader(getActivity(), mUri, POPULAR_MOVIE_COLUMNS, null, null, null);
                }
                case "Highest-Rated": {
                    Uri moviesUri = MoviesContract.MovieRatingsEntry.buildMoviesUri();
                    return new CursorLoader(getActivity(), mUri, MOVIE_RATINGS_COLUMNS, null, null, null);
                }
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data != null && data.moveToFirst()) {

            String overview = data.getString(COL_OVERVIEW);
            String title = data.getString(COL_TITLE);
            String poster = data.getString(COL_POSTER_PATH);
            Double vote = data.getDouble(COL_AVERAGE_VOTE);
            String date = data.getString(COL_RELEASE_DATE);

            overviewTV.setText(overview);
            titleTV.setText(title);
            averageTV.setText(String.valueOf(vote));
            releaseTV.setText(date);
            Picasso.with(getActivity()).load("http://image.tmdb.org/t/p/w185/" + poster).into(posterView);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }


}
