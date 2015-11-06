package com.example.android.moviesaroundtheclock.AdaptersAndParser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Extra;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Movie;
import com.example.android.moviesaroundtheclock.BasicClassesDefinition.Trailer;
import com.example.android.moviesaroundtheclock.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by FATMA on 22-Aug-15.
 */
public class TrailersAdapter extends ArrayAdapter<Extra> {

    public Context context;
    public ArrayList<Extra> trailers;


    public TrailersAdapter(Context context, int viewResourceId, ArrayList<Extra> trailers) {
        super(context, viewResourceId, trailers);
        this.context = context;
        this.trailers = trailers;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Trailer getItem(int position) {
        return (Trailer)trailers.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View recycledView = convertView;
        ViewHolderItem holder;

        if (recycledView != null) {
            holder = (ViewHolderItem) recycledView.getTag();
        } else {
            recycledView = View.inflate(getContext(), R.layout.movieitem_listview_trailers, null);
            holder = new ViewHolderItem();
            holder.name = (TextView) recycledView.findViewById(R.id.trailer_name_textview);
            //holder.key = (TextView) recycledView.findViewById(R.id.trailer_key_textview);

            recycledView.setTag(holder);
        }

        final Trailer t = getItem(position);
        if (t != null) {

            holder.name.setText(t.gettName());
            final String key = t.gettKey();

            recycledView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + key));
                    context.startActivity(intent);
                }
            });
        }

        return recycledView;
    }


    public static class ViewHolderItem {
        TextView name;
        //TextView key;
    }


}