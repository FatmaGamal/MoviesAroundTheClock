package com.example.android.moviesaroundtheclock;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);


        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("Title")) {

            String mTitle = intent.getStringExtra("Title");
            String mOverview = intent.getStringExtra("Overview");
            String mRelease = intent.getStringExtra("Release");
            String mPoster = intent.getStringExtra("Poster");
            String mAverage = intent.getStringExtra("Average");
            TextView titleTV = (TextView) findViewById(R.id.movie_name);
            TextView overviewTV = (TextView) findViewById(R.id.movie_overview);
            TextView releaseTV = (TextView) findViewById(R.id.movie_release);
            TextView averageTV = (TextView) findViewById(R.id.movie_vote);
            ImageView posterView = (ImageView) findViewById(R.id.movie_poster);

            titleTV.setText(mTitle);
            overviewTV.setText(mOverview);
            releaseTV.setText(mRelease);
            averageTV.setText(mAverage);
            Picasso.with(getApplicationContext()).load("http://image.tmdb.org/t/p/w185/" + mPoster).into(posterView);
            posterView.setAdjustViewBounds(true);

        }
    }

        @Override
        public boolean onCreateOptionsMenu (Menu menu){
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_movie_details, menu);

            return true;
        }

        @Override
        public boolean onOptionsItemSelected (MenuItem item){
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_settings) {
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }
