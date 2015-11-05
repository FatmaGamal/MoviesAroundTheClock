package com.example.android.moviesaroundtheclock;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity  extends ActionBarActivity implements MovieFragment.Callback {

    private boolean mTwoPane;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("MainActivity", "start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.v("MainActivity", "Layout shown");
       if (findViewById(R.id.movie_detail_container) != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.

           Log.v("MainActivity", "TwoPane = true");
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new DetailsFragment())
                        .commit();
            }
        } else {
            mTwoPane = false;

           Log.v("MainActivity", "TwoPane = false");
        }
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

        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), PreferencesActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Uri contentUri, Boolean favBoolean) {
        if (mTwoPane) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            args.putParcelable(DetailsFragment.URI_ARG, contentUri);
            args.putBoolean(DetailsFragment.FAV_ARG, favBoolean);

            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();

            Log.v("MainActivity", "putting args for details fragment");

        } else {
            Intent intent = new Intent(this, DetailsActivity.class).setData(contentUri);
            intent.putExtra(DetailsFragment.FAV_ARG, favBoolean);
            Log.v("MainActivity", "putting args for detailsActivity in intent");
            startActivity(intent);
        }
    }

}
