package com.crazyhitty.chdev.ks.popularmovies.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MoviesActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.coordinator_layout_movies)
    CoordinatorLayout coordinatorLayoutMovies;

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
    }
}
