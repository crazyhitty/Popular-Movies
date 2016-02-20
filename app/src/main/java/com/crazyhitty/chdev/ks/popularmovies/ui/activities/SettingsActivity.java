package com.crazyhitty.chdev.ks.popularmovies.ui.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.ui.fragments.SettingsFragment;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set settings fragment
        SettingsFragment settingsFragment = new SettingsFragment();

        //don't use v4 FragmentTransaction because it won't work with preference fragment
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_settings, settingsFragment).commit();
    }
}
