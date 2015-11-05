package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Extra;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Review;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.R;

import java.util.ArrayList;

/**
 * Created by FATMA on 22-Aug-15.
 */
public class ReviewsAdapter extends ArrayAdapter<Extra> {

    public Context context;
    public ArrayList<Extra> reviews;


    public ReviewsAdapter(Context context, int viewResourceId, ArrayList<Extra> reviews) {
        super(context, viewResourceId, reviews);
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Review getItem(int position) {
        return (Review)reviews.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        View recycledView = convertView;
        ViewHolderItem holder;

        if (recycledView != null) {
            holder = (ViewHolderItem) recycledView.getTag();
        } else {
            recycledView = View.inflate(getContext(), R.layout.movieitem_listview_reviews, null);
            holder = new ViewHolderItem();
            holder.author = (TextView)recycledView.findViewById(R.id.review_author_textview);
            holder.review = (TextView)recycledView.findViewById(R.id.review_body_textview);
            holder.url = (TextView)recycledView.findViewById(R.id.review_url_textview);
            recycledView.setTag(holder);
        }

        Review r = getItem(position);
        if (r != null) {
            holder.author.setText(r.getrAuthor());
            holder.review.setText(r.getrBody());
            holder.url.setText(r.getrUrl());

        }

        return recycledView;
    }


    public static class ViewHolderItem {
        TextView author;
        TextView review;
        TextView url;

    }


}