package com.crazyhitty.chdev.ks.popularmovies.ui.activities;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;
import com.crazyhitty.chdev.ks.popularmovies.ui.fragments.MovieDetailsFragment;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.image_view_backdrop)
    ImageView imgBackdrop;

    private MovieItem mMovieItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        mMovieItem = getMovieItem();

        initToolbar();

        //set backdrop image
        Picasso.with(this)
                .load(SettingPreferences.BACKDROP_IMAGE_PATH + mMovieItem.getBackdropPath())
                .placeholder(R.drawable.light_black_bg)
                .error(R.drawable.ic_error_48dp)
                .into(imgBackdrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        imgBackdrop.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    @Override
                    public void onError() {
                        imgBackdrop.setScaleType(ImageView.ScaleType.CENTER);
                    }
                });

        //set details fragment
        MovieDetailsFragment movieDetailsFragment = MovieDetailsFragment.newInstance(mMovieItem);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_movie_details, movieDetailsFragment).commit();
    }

    private void initToolbar() {
        toolbar.setTitle(mMovieItem.getTitle());
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private MovieItem getMovieItem() {
        MovieItem movieItem = new MovieItem();
        Bundle bundle = getIntent().getExtras();
        movieItem.setPage(bundle.getInt(MovieItem.PAGE_KEY));
        movieItem.setAdult(bundle.getBoolean(MovieItem.ADULT_KEY));
        movieItem.setBackdropPath(bundle.getString(MovieItem.BACKDROP_PATH_KEY));
        movieItem.setId(bundle.getInt(MovieItem.ID_KEY));
        movieItem.setOriginalLanguage(bundle.getString(MovieItem.ORIGINAL_LANGUAGE_KEY));
        movieItem.setOriginalTitle(bundle.getString(MovieItem.ORIGINAL_LANGUAGE_KEY));
        movieItem.setOverview(bundle.getString(MovieItem.OVERVIEW_KEY));
        movieItem.setReleaseDate(bundle.getString(MovieItem.RELEASE_DATE_KEY));
        movieItem.setPosterPath(bundle.getString(MovieItem.POSTER_PATH_KEY));
        movieItem.setPopularity(bundle.getDouble(MovieItem.POPULARITY_KEY));
        movieItem.setTitle(bundle.getString(MovieItem.TITLE_KEY));
        movieItem.setVideo(bundle.getBoolean(MovieItem.VIDEO_KEY));
        movieItem.setVoteAverage(bundle.getDouble(MovieItem.VOTE_AVERAGE_KEY));
        movieItem.setVoteCount(bundle.getInt(MovieItem.VOTE_COUNT_KEY));
        movieItem.setType(bundle.getString(MovieItem.TYPE_KEY));
        return movieItem;
    }
}
