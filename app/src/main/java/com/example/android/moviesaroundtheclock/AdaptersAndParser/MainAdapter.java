package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.moviesaroundtheclock.DetailsFragment;
import com.example.android.moviesaroundtheclock.MovieFragment;
import com.example.android.moviesaroundtheclock.R;
import com.squareup.picasso.Picasso;

/**
 * Created by FATMA on 22-Aug-15.
 */
public class MainAdapter extends CursorAdapter {

    public Context context;
/*
    private static final int TYPE_HEADER_REVIEW = 0;
    private static final int TYPE_HEADER_TRAILER = 1;
    private static final int TYPE_ITEM_REVIEW = 2;
    private static final int TYPE_ITEM_TRAILER = 3;
    private static final int TYPE_ITEM_DETAILS = 4;
    private static final int TYPE_MAIN = 5;

    @Override
    public int getViewTypeCount() {
        return 6;
    }

    @Override
    public int getItemViewType(int position) {
        // 0 -> first item -> movie details like overview
        if (MovieFragment.count == 0)
            return 4;
        // 1 -> second item -> header of the trailers
        else if (MovieFragment.count == 1)
            return 1;
        // 2:trailers.size -> items of trailers. Added the two for it to be like the 1st and 2nd item in list
        else if (MovieFragment.count > 1 && MovieFragment.count < trailers.size() + 2)
            return 3;
        // >trailers.size+2 -> header of reviews
        else if (MovieFragment.count == trailers.size() + 3)
            return 0;
        // items of reviews -> from just above the trailer header to listSize-1 which will be the main page posters
        else if (MovieFragment.count > trailers.size() + 3 && MovieFragment.count < listSize - 1)
            return 2;
        //main page -> gridview
        else return 5;
    }
*/

    public MainAdapter(Context context, Cursor c, int flags/*, ArrayList<Trailer> Ts, ArrayList<Review> Rs*/) {
        super(context, c, flags/*, Ts, Rs*/);/*
        trailers = Ts;
        reviews = Rs;
        listSize = trailers.size() + reviews.size() + 3;*/
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        //int type = getItemViewType(cursor.getPosition());

        ViewHolderItem holder;
        int layoutId;

//
//        switch (type) {
//            case 0: {   //Reviews header
//                layoutId = R.layout.header;
//                View recycledView = LayoutInflater.from(context).inflate(layoutId, parent, false);
//                TextView header = (TextView) recycledView.findViewById(R.id.header_textview);
//                header.setText("Reviews : ");
//                MovieFragment.count++;
//                return recycledView;
//            }
//            case 1: { //Trailers header
//                layoutId = R.layout.header;
//                View recycledView = LayoutInflater.from(context).inflate(layoutId, parent, false);
//                TextView header = (TextView) recycledView.findViewById(R.id.header_textview);
//                header.setText("Trailers : ");
//                MovieFragment.count++;
//                return recycledView;
//            }
//            case 2: {   //Review Item
//                break;
//            }
//            case 3: {   //Trailer Item
//                break;
//            }
//            case 4: {   //Movie Details

/*                layoutId = R.layout.movieitem_listview_details;
                View recycledView = LayoutInflater.from(context).inflate(layoutId, parent, false);
                holder = new ViewHolderItem(recycledView);
*//*                holder.title = (TextView) recycledView.findViewById(R.id.movie_name);
                holder.overview = (TextView) recycledView.findViewById(R.id.movie_overview);
                holder.release = (TextView) recycledView.findViewById(R.id.movie_release);
                holder.vote = (TextView) recycledView.findViewById(R.id.movie_vote);
                holder.path = (ImageView) recycledView.findViewById(R.id.movieitem_imageview);*//*
                recycledView.setTag(holder);
                MovieFragment.count++;
                return recycledView;
            }*/
        //           case 5: {   //Movie in GridView
        layoutId = R.layout.movieitem_grid;
        View recycledView = LayoutInflater.from(context).inflate(layoutId, parent, false);
        holder = new ViewHolderItem(recycledView);
        holder.img = (ImageView) recycledView.findViewById(R.id.movieitem_imageview);
        recycledView.setTag(holder);
        //MovieFragment.count++;
        return recycledView;
        //         }
        //  }
        //     return null;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolderItem holder = (ViewHolderItem) view.getTag();
 /*       int type = getItemViewType(cursor.getPosition());


        switch (type) {
            case 4: {   //Movie Details

                String overview = cursor.getString(DetailsFragment.COL_OVERVIEW);
                String name = cursor.getString(DetailsFragment.COL_TITLE);
                String poster = cursor.getString(DetailsFragment.COL_POSTER_PATH);
                Double vote = cursor.getDouble(DetailsFragment.COL_AVERAGE_VOTE);
                String date = cursor.getString(DetailsFragment.COL_RELEASE_DATE);


                Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + poster).into(holder.path);
                holder.title.setText(name);
                holder.overview.setText(overview);
                holder.vote.setText(String.valueOf(vote));
                holder.release.setText(date);

                MovieFragment.count++;
                break;
            }

            case 5: {   //Movie in GridView
*/
        String poster = cursor.getString(DetailsFragment.COL_POSTER_PATH);
        Picasso.with(context).load("http://image.tmdb.org/t/p/w185/" + poster).into(holder.img);

       //MovieFragment.count++;
        //break;
        //}
    }


    public static class ViewHolderItem {
        ImageView img;/*
        TextView title;
        TextView vote;
        ImageView path;
        TextView overview;
        TextView release;*/

        public ViewHolderItem(View view) {/*
            title = (TextView) view.findViewById(R.id.movie_name);
            overview = (TextView) view.findViewById(R.id.movie_overview);
            release = (TextView) view.findViewById(R.id.movie_release);
            vote = (TextView) view.findViewById(R.id.movie_vote);
            path = (ImageView) view.findViewById(R.id.movieitem_imageview);*/
            img = (ImageView) view.findViewById(R.id.movieitem_imageview);
        }
    }


}