package com.example.android.moviesaroundtheclock;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.moviesaroundtheclock.AdaptersAndParser.FetchMovieTask;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.MainAdapter;
import com.example.android.moviesaroundtheclock.Data.MoviesContract;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static public MainAdapter mMovieAdapter;
    private static final int MOVIES_LOADER = 0;
    FetchMovieTask asyncTask;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";



    public static String sortby ;
    private static final String[] POPULAR_MOVIE_COLUMNS = {
            MoviesContract.PopularMovieEntry.TABLE_NAME + "." + MoviesContract.PopularMovieEntry._ID,
            MoviesContract.PopularMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID};


    private static final String[] MOVIE_RATINGS_COLUMNS = {
            MoviesContract.MovieRatingsEntry.TABLE_NAME + "." + MoviesContract.MovieRatingsEntry._ID,
            MoviesContract.MovieRatingsEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID};

    private static final String[] FAVOURITE_MOVIES_COLUMNS = {
            MoviesContract.FavMovieEntry.TABLE_NAME + "." + MoviesContract.FavMovieEntry._ID,
            MoviesContract.FavMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID};


    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_MOVIE_ID = 6;

    GridView movieList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("MovieFragment", "before inflation of layout");
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mMovieAdapter = new MainAdapter(getActivity(), null, 0);

        movieList = (GridView) rootView.findViewById(R.id.movies_gridview);
        Log.v("MovieFragment", "Before update");

        sortby = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_default));


        updateMovieList();
        movieList.setAdapter(mMovieAdapter);
        Log.v("MovieFragment", "movieAdapter set to gridLayout");
        movieList.setSelection(0);
        Log.v("MovieFragment", "selection of item set to 0");

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    switch (sortby) {
                        case MoviesContract.POPULARITY: {
                            Boolean fav = false;
                            int movieId = cursor.getColumnIndex(MoviesContract.PopularMovieEntry._ID);
                            Log.v("MovieFragment", "Popular : movieId to get uri of = " + String.valueOf(movieId));
                            Log.v("MovieFragment", "Popular : url to be sent = " + MoviesContract.PopularMovieEntry.buildCertainMovieUri(cursor.getInt(movieId)).toString());
                            ((Callback) getActivity()).onItemSelected(MoviesContract.PopularMovieEntry.buildCertainMovieUri(cursor.getInt(movieId)), fav);
                            break;
                        }
                        case MoviesContract.HIGHEST_RATED: {
                            Boolean fav = false;
                            int movieId = cursor.getColumnIndex(MoviesContract.MovieRatingsEntry._ID);
                            Log.v("MovieFragment", "Ratings : movieId to get uri of = " + String.valueOf(movieId));
                            Log.v("MovieFragment", "Ratings : url to be sent = " + MoviesContract.MovieRatingsEntry.buildCertainMovieUri(cursor.getInt(movieId)).toString());
                            ((Callback) getActivity()).onItemSelected(MoviesContract.MovieRatingsEntry.buildCertainMovieUri(cursor.getInt(movieId)), fav);
                            break;
                        }
                        case MoviesContract.FAVORITE: {
                            Boolean fav = true;
                            int movieId = cursor.getColumnIndex(MoviesContract.FavMovieEntry._ID);
                            Log.v("MovieFragment", "Favourites : movieId to get uri of = " + String.valueOf(movieId));
                            Log.v("MovieFragment", "Favourites : url to be sent = " + MoviesContract.FavMovieEntry.buildCertainMovieUri(cursor.getInt(movieId)).toString());
                            ((Callback) getActivity()).onItemSelected(MoviesContract.FavMovieEntry.buildCertainMovieUri(cursor.getInt(movieId)), fav);
                            break;
                        }
                    }
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }


        return rootView;
    }

    public interface Callback {
        public void onItemSelected(Uri movieUri, Boolean favBoolean);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieList();   }

    private void updateMovieList() {
        if (sortby != MoviesContract.FAVORITE) {
            asyncTask = new FetchMovieTask(getActivity());
            Log.v("MovieFragment", "before asyncTask executes");
            asyncTask.execute(sortby);
        }
            Log.v("MovieFragment", "after asyncTask executes");
            movieList.setAdapter(mMovieAdapter);
            Log.v("MovieFragment", "after mMovieAdapter was updated by FetchMovieTask");

        if (getLoaderManager().getLoader(MOVIES_LOADER) != null) {
            if (getLoaderManager().getLoader(MOVIES_LOADER).isStarted())
                getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
            Log.v("MovieFragment", "after loader was restarted");
        }

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent i = new Intent(getActivity(), PreferencesActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.v("MovieFragment", "before loader is initialized");
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        Log.v("MovieFragment", "after loader is initialized");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch (sortby) {
            case MoviesContract.POPULARITY: {
                Uri moviesUri = MoviesContract.PopularMovieEntry.buildMoviesUri();
                Log.v("MovieFragment", "Popular : buildMoviesUri = " + moviesUri.toString());
                return new CursorLoader(getActivity(), moviesUri, POPULAR_MOVIE_COLUMNS, null, null, null);
            }
            case MoviesContract.HIGHEST_RATED: {
                Uri moviesUri = MoviesContract.MovieRatingsEntry.buildMoviesUri();
                Log.v("MovieFragment", "Ratings : buildMoviesUri = " + moviesUri.toString());
                return new CursorLoader(getActivity(), moviesUri, MOVIE_RATINGS_COLUMNS, null, null, null);
            }
            case MoviesContract.FAVORITE: {
                Uri moviesUri = MoviesContract.FavMovieEntry.buildMoviesUri();
                Log.v("MovieFragment", "Favourite : buildMoviesUri = " + moviesUri.toString());
                return new CursorLoader(getActivity(), moviesUri, FAVOURITE_MOVIES_COLUMNS, null, null, null);
            }
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mMovieAdapter.swapCursor(data);
        if (mPosition != GridView.INVALID_POSITION) {
            movieList.smoothScrollToPosition(mPosition);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mMovieAdapter.swapCursor(null);
    }
}
