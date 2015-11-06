package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviesaroundtheclock.Data.MoviesContract;
import com.example.android.moviesaroundtheclock.DetailsFragment;
import com.example.android.moviesaroundtheclock.MovieFragment;
import com.example.android.moviesaroundtheclock.R;
import com.squareup.picasso.Picasso;

/**
 * Created by FATMA on 22-Aug-15.
 */
public class MainAdapter extends CursorAdapter {

    public Context context;

    public MainAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //int type = getItemViewType(cursor.getPosition());
        Log.v("MainAdapter", "entered in newVew");
        ViewHolderItem holder;
        int layoutId;
        layoutId = R.layout.movieitem_grid;
        View recycledView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        holder = new ViewHolderItem(recycledView);
        holder.img = (ImageView) recycledView.findViewById(R.id.movieitem_imageview);
        recycledView.setTag(holder);
        Log.v("MainAdapter", "holder set, finished newView");
        return recycledView;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Log.v("MainAdapter", "entered in bindView");
        ViewHolderItem holder = (ViewHolderItem) view.getTag();

        int index=5;
        switch (MovieFragment.sortby){
            case MoviesContract.POPULARITY :
                index = cursor.getColumnIndex(MoviesContract.PopularMovieEntry.COLUMN_POSTER_PATH);
            case MoviesContract.HIGHEST_RATED :
                index = cursor.getColumnIndex(MoviesContract.MovieRatingsEntry.COLUMN_POSTER_PATH);
            case MoviesContract.FAVORITE :
                index = cursor.getColumnIndex(MoviesContract.FavMovieEntry.COLUMN_POSTER_PATH);
        }
        String poster = cursor.getString(index);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + poster).into(holder.img);
        Log.v("MainAdapter", "finished loading");
    }


    public static class ViewHolderItem {
        ImageView img;

        public ViewHolderItem(View view) {
            img = (ImageView) view.findViewById(R.id.movieitem_imageview);
        }
    }


}