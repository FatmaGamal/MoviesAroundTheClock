package com.example.android.moviesaroundtheclock;

import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    static public CustomMovieAdapter arrAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        GridView moviesList = (GridView)findViewById(R.id.movies_gridview);
        ArrayList<Movie> movieList = new ArrayList<Movie>();
        Movie m = new Movie();
        movieList.add(m);

        arrAdapter= new CustomMovieAdapter(getApplicationContext(),R.id.movieitem_imageview, movieList);
        moviesList.setAdapter(arrAdapter);
        updateMovieList();

        moviesList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                intent.putExtra("Title", arrAdapter.getItem(position).getmTitle());
                intent.putExtra("Overview", arrAdapter.getItem(position).getmOverview());
                intent.putExtra("Release", arrAdapter.getItem(position).getmReleaseDate());
                intent.putExtra("Average", arrAdapter.getItem(position).getmVoteAverage());
                intent.putExtra("Poster", arrAdapter.getItem(position).getmPoster());
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovieList();    }

    private void updateMovieList() {
        String sortby = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getString(getString(R.string.movie_sort_key), getString(R.string.movie_sort_default));
        FetchMovieTask asyncTask = new FetchMovieTask() ;
        asyncTask.execute(sortby);
        return ;
    }

}
