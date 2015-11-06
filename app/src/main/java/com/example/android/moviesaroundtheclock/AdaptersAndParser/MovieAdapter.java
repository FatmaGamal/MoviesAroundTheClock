package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.Data.MoviesProvider;
import com.example.android.moviesaroundtheclock.DetailsFragment;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Movie;
import com.example.android.moviesaroundtheclock.MovieFragment;
import com.example.android.moviesaroundtheclock.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import static com.example.android.moviesaroundtheclock.Data.MoviesProvider.*;

/**
 * Created by FATMA on 22-Aug-15.
 */
public class MovieAdapter extends CursorAdapter {

    public Context context;


    public MovieAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        ViewHolderItem holder;
        int layoutId;

        Log.v("MovieAdapter", "entered in newVew");
        layoutId = R.layout.movieitem_listview_details;
        View recycledView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        holder = new ViewHolderItem(recycledView);
        holder.title = (TextView) recycledView.findViewById(R.id.movie_name);
        holder.overview = (TextView) recycledView.findViewById(R.id.movie_overview);
        holder.release = (TextView) recycledView.findViewById(R.id.movie_release);
        holder.vote = (TextView) recycledView.findViewById(R.id.movie_vote);
        holder.path = (ImageView) recycledView.findViewById(R.id.movieitem_imageview);
        holder.favBtn = (Button) recycledView.findViewById(R.id.favBtn);

        recycledView.setTag(holder);
        Log.v("MovieAdapter", "finished newVew");
        return recycledView;
    }


    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        Log.v("MovieAdapter", "entered in bindView");
        final ViewHolderItem holder = (ViewHolderItem) view.getTag();


        if (cursor != null && cursor.moveToFirst()) {
            final String movieId = cursor.getString(DetailsFragment.COL_MOVIE_ID);
            final String overview = cursor.getString(DetailsFragment.COL_OVERVIEW);
            final String name = cursor.getString(DetailsFragment.COL_TITLE);
            final String poster = cursor.getString(DetailsFragment.COL_POSTER_PATH);
            final Double vote = cursor.getDouble(DetailsFragment.COL_AVERAGE_VOTE);
            final String date = cursor.getString(DetailsFragment.COL_RELEASE_DATE);

            holder.favBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (checkFav(movieId)) {
                        Log.d("grid", "already in favorites, so remove");
                        removeFav(movieId);
                        holder.favBtn.setText(R.string.add_to_favourites);
                    } else {
                        Log.d("grid", "not in favorites, so add");
                        addFav(movieId, name, overview, poster, vote, date);
                        holder.favBtn.setText(R.string.remove_from_favourites);
                    }
                }
            });

            Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + poster).into(holder.path);
            holder.title.setText(name);
            holder.overview.setText(overview);
            holder.vote.setText(String.valueOf(vote));
            holder.release.setText(date);

            if (checkFav(movieId))
                holder.favBtn.setText(R.string.remove_from_favourites);
            else holder.favBtn.setText(R.string.add_to_favourites);
        }
        Log.v("MovieAdapter", "finished bindView");


    }

    public boolean checkFav(String movieKey) {
        return MoviesProvider.isFav(mContext, movieKey);
    }

    public void removeFav(String movieKey) {
        int rowsDeleted = mContext.getContentResolver().delete(
                MoviesContract.FavMovieEntry.CONTENT_URI,
                MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieKey});


    }

    public void addFav(String movieKey, String title, String overview, String posterPath, double voteAverage, String releaseDate) {
        //check if already exists
        Cursor favMovieCursor = mContext.getContentResolver().query(
                MoviesContract.FavMovieEntry.CONTENT_URI,
                new String[]{MoviesContract.FavMovieEntry._ID},
                MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID + " = ?",
                new String[]{movieKey}, null
        );
        if (!favMovieCursor.moveToFirst()) {
            //inserting
            ContentValues favMovieValues = new ContentValues();
            favMovieValues.put(MoviesContract.FavMovieEntry.COLUMN_MOVIE_ID, movieKey);
            favMovieValues.put(MoviesContract.FavMovieEntry.COLUMN_TITLE, title);
            favMovieValues.put(MoviesContract.FavMovieEntry.COLUMN_OVERVIEW, overview);
            favMovieValues.put(MoviesContract.FavMovieEntry.COLUMN_POSTER_PATH, posterPath);
            favMovieValues.put(MoviesContract.FavMovieEntry.COLUMN_AVERAGE_VOTE, voteAverage);
            favMovieValues.put(MoviesContract.FavMovieEntry.COLUMN_RELEASE_DATE, releaseDate);

            Uri insertedUri = mContext.getContentResolver().insert(MoviesContract.FavMovieEntry.CONTENT_URI, favMovieValues);
        }
        favMovieCursor.close();
    }

    public static class ViewHolderItem {
        TextView title;
        TextView vote;
        ImageView path;
        TextView overview;
        TextView release;
        Button favBtn;

        public ViewHolderItem(View view) {
            title = (TextView) view.findViewById(R.id.movie_name);
            overview = (TextView) view.findViewById(R.id.movie_overview);
            release = (TextView) view.findViewById(R.id.movie_release);
            vote = (TextView) view.findViewById(R.id.movie_vote);
            path = (ImageView) view.findViewById(R.id.movieitem_imageview);
            favBtn = (Button) view.findViewById(R.id.favBtn);
        }
    }


}