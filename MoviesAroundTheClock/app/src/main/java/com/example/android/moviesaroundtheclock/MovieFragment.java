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
import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.AdaptersAndParser.MovieAdapter;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MovieFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static public MovieAdapter mMovieAdapter;
    public static int count = 0;
    private static final int MOVIES_LOADER = 0;
    FetchMovieTask asyncTask;
    private int mPosition = GridView.INVALID_POSITION;
    private static final String SELECTED_KEY = "selected_position";

    private String sortby ;
    private static final String[] POPULAR_MOVIE_COLUMNS = {
            MoviesContract.PopularMovieEntry.TABLE_NAME + "." + MoviesContract.PopularMovieEntry._ID,
/*            MoviesContract.PopularMovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.PopularMovieEntry.COLUMN_TITLE,
            MoviesContract.PopularMovieEntry.COLUMN_OVERVIEW,
            MoviesContract.PopularMovieEntry.COLUMN_AVERAGE_VOTE,*/
            MoviesContract.PopularMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.PopularMovieEntry.COLUMN_MOVIE_ID};


    private static final String[] MOVIE_RATINGS_COLUMNS = {
            MoviesContract.MovieRatingsEntry.TABLE_NAME + "." + MoviesContract.MovieRatingsEntry._ID,
/*            MoviesContract.MovieRatingsEntry.COLUMN_RELEASE_DATE,
            MoviesContract.MovieRatingsEntry.COLUMN_TITLE,
            MoviesContract.MovieRatingsEntry.COLUMN_OVERVIEW,
            MoviesContract.MovieRatingsEntry.COLUMN_AVERAGE_VOTE, */
            MoviesContract.MovieRatingsEntry.COLUMN_POSTER_PATH,
            MoviesContract.MovieRatingsEntry.COLUMN_MOVIE_ID};

    private static final String[] FAVOURITE_MOVIES_COLUMNS = {
            MoviesContract.FavMovieEntry.TABLE_NAME + "." + MoviesContract.FavMovieEntry._ID,
/*            MoviesContract.FavMovieEntry.COLUMN_RELEASE_DATE,
            MoviesContract.FavMovieEntry.COLUMN_TITLE,
            MoviesContract.FavMovieEntry.COLUMN_OVERVIEW,
            MoviesContract.FavMovieEntry.COLUMN_AVERAGE_VOTE,*/
            MoviesContract.FavMovieEntry.COLUMN_POSTER_PATH,
            MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID};


    // These indices are tied to MOVIE_COLUMNS.  If MOVIE_COLUMNS changes, these
    // must change.
    public static final int COL_ID = 0;
    //public static final int COL_RELEASE_DATE = 1;
    //public static final int COL_TITLE = 2;
    //public static final int COL_OVERVIEW = 3;
    //public static final int COL_AVERAGE_VOTE = 4;
    public static final int COL_POSTER_PATH = 5;
    public static final int COL_MOVIE_ID = 6;
    //public static final int COL_IS_FAVOURITE = 7;

    ImageView posterView;
    GridView movieList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_movie, container, false);
        mMovieAdapter = new MovieAdapter(getActivity(), null, 0);

        movieList = (GridView) rootView.findViewById(R.id.movies_gridview);
        movieList.setAdapter(mMovieAdapter);
        movieList.setSelection(0);

        movieList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    switch (sortby) {
                        case "Most Popular": {
                            ((Callback) getActivity()).onItemSelected(MoviesContract.PopularMovieEntry.buildCertainMovieUri(position));
                        }
                        case "Hightes-Rated": {
                            ((Callback) getActivity()).onItemSelected(MoviesContract.MovieRatingsEntry.buildCertainMovieUri(position));
                        }
                        case "Favourites": {
                            ((Callback) getActivity()).onItemSelected(MoviesContract.FavMovieEntry.buildCertainMovieUri(position));
                        }
                    }
                }
                mPosition = position;
            }
        });

        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }

        sortby = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_default));
        updateMovieList();

        return rootView;
    }

    public interface Callback {
        public void onItemSelected(Uri dateUri); //** TODO : don't understand it **/
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovieList();   }

    private void updateMovieList() {
        if(asyncTask != null){
            asyncTask.cancel(true);
        }
        if (sortby != "Favourites"){
        asyncTask = new FetchMovieTask() ;
        asyncTask.execute(sortby);
        }
        movieList.setAdapter(mMovieAdapter);
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
        return ;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_movie_details, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
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
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
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
            case "Most Popular": {
                Uri moviesUri = MoviesContract.PopularMovieEntry.buildMoviesUri();
                return new CursorLoader(getActivity(), moviesUri, POPULAR_MOVIE_COLUMNS, null, null, null);
            }
            case "Highest-Rated": {
                Uri moviesUri = MoviesContract.MovieRatingsEntry.buildMoviesUri();
                return new CursorLoader(getActivity(), moviesUri, MOVIE_RATINGS_COLUMNS, null, null, null);
            }
            case "Favourites": {
                Uri moviesUri = MoviesContract.FavMovieEntry.buildMoviesUri();
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
