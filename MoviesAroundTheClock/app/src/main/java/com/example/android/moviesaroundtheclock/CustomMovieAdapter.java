package com.example.android.moviesaroundtheclock;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.zip.Inflater;

/**
 * Created by FATMA on 22-Aug-15.
 */
public class CustomMovieAdapter extends ArrayAdapter<Movie> {

    public Context context;
    public ArrayList<Movie> movies;

    public CustomMovieAdapter(Context context, int viewResourceId, ArrayList<Movie> movies) {
        super(context, viewResourceId,movies);
        this.context = context;
        this.movies = movies;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    View recycledView = convertView;
    ViewHolderItem holder;

    if (recycledView != null){
        holder = (ViewHolderItem) recycledView.getTag();

    }else{

        recycledView = View.inflate(getContext(), R.layout.movieitem_grid, null);
        holder = new ViewHolderItem();
        holder.img = (ImageView)recycledView.findViewById(R.id.movieitem_imageview);
        recycledView.setTag(holder);
    }


    Movie m = getItem(position);
        if(m != null) {
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/"+ m.getmPoster()).into(holder.img);
    }

    return recycledView;
}

    public static class ViewHolderItem{
        ImageView img;
    }


}