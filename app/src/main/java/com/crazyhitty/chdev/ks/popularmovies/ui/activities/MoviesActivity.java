package com.crazyhitty.chdev.ks.popularmovies.ui.activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;
import com.crazyhitty.chdev.ks.popularmovies.ui.fragments.MovieDetailsFragment;
import com.crazyhitty.chdev.ks.popularmovies.ui.fragments.MoviesFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinator_layout_movies)
    CoordinatorLayout coordinatorLayoutMovies;

    private MoviesFragment mRetainMoviesFragment;

    /**
     * Whether or not the activity is in two-pane mode, i.e. running in landscape mode
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //initialize some pre defined settings
        SettingPreferences.init(this);

        setContentView(R.layout.activity_movies);

        //bind views
        ButterKnife.bind(this);

        //set toolbar
        setSupportActionBar(toolbar);

        //Initialize other components
        init();
    }

    private void init() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        mRetainMoviesFragment = (MoviesFragment) getSupportFragmentManager().findFragmentByTag(MoviesFragment.class.getSimpleName());

        if (findViewById(R.id.linear_layout_movies) != null) {
            // The detail container view will be present only in the
            // landscape layouts (res/values-land).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;

            //check if retain movies fragment is already available or not
            if (mRetainMoviesFragment == null) {
                mRetainMoviesFragment = MoviesFragment.newInstance(null, true);
            } else {
                //re initialize the retain movies fragment with old data
                //this is done so that other properties of the fragment change meanwhile the dataset remains the same
                mRetainMoviesFragment = MoviesFragment.newInstance(mRetainMoviesFragment.getMovieItems(), true);
            }

            //set movies fragment on frame layout movies
            fragmentTransaction.replace(R.id.frame_movies,
                    mRetainMoviesFragment,
                    MoviesFragment.class.getSimpleName());

            fragmentTransaction.replace(R.id.frame_movie_details,
                    MovieDetailsFragment.newInstance(null, true),
                    MovieDetailsFragment.class.getSimpleName());

            fragmentTransaction.commit();
        } else {
            //check if retain movies fragment is already available or not
            if (mRetainMoviesFragment == null) {
                mRetainMoviesFragment = MoviesFragment.newInstance(null, false);
            } else {
                //re initialize the retain movies fragment with old data
                //this is done so that other properties of the fragment change meanwhile the dataset remains the same
                mRetainMoviesFragment = MoviesFragment.newInstance(mRetainMoviesFragment.getMovieItems(), false);
            }

            //set movies fragment on frame layout movies
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame_movies,
                    mRetainMoviesFragment,
                    MoviesFragment.class.getSimpleName()).commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //initialize some pre defined settings
        SettingPreferences.init(this);

        setContentView(R.layout.activity_movies);

        //bind views
        ButterKnife.bind(this);

        //set toolbar
        setSupportActionBar(toolbar);

        //Initialize other components
        init();
    }
}
