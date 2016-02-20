package com.crazyhitty.chdev.ks.popularmovies.ui.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kartik_ch on 2/13/2016.
 */
public class MovieDetailsFragment extends Fragment {

    private static final String UNAVAILABLE = "Unavailable";
    @Bind(R.id.text_view_rating)
    TextView txtRating;
    @Bind(R.id.text_view_release_date)
    TextView txtReleaseDate;
    @Bind(R.id.text_view_overview)
    TextView txtOverview;
    private MovieItem mMovieItem;

    public static MovieDetailsFragment newInstance(MovieItem movieItem) {
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putInt(MovieItem.PAGE_KEY, movieItem.getPage());
        args.putBoolean(MovieItem.ADULT_KEY, movieItem.isAdult());
        args.putString(MovieItem.BACKDROP_PATH_KEY, movieItem.getBackdropPath());
        args.putInt(MovieItem.ID_KEY, movieItem.getId());
        args.putString(MovieItem.ORIGINAL_LANGUAGE_KEY, movieItem.getOriginalLanguage());
        args.putString(MovieItem.ORIGINAL_TITLE_KEY, movieItem.getOriginalTitle());
        args.putString(MovieItem.OVERVIEW_KEY, movieItem.getOverview());
        args.putString(MovieItem.RELEASE_DATE_KEY, movieItem.getReleaseDate());
        args.putString(MovieItem.POSTER_PATH_KEY, movieItem.getPosterPath());
        args.putDouble(MovieItem.POPULARITY_KEY, movieItem.getPopularity());
        args.putString(MovieItem.TITLE_KEY, movieItem.getTitle());
        args.putBoolean(MovieItem.VIDEO_KEY, movieItem.isVideo());
        args.putDouble(MovieItem.VOTE_AVERAGE_KEY, movieItem.getVoteAverage());
        args.putInt(MovieItem.VOTE_COUNT_KEY, movieItem.getVoteCount());
        args.putString(MovieItem.TYPE_KEY, movieItem.getType());
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMovieItem = getMovieItem();

        String rating = mMovieItem.getVoteAverage() + " out of 10";
        txtRating.setText(rating);

        if (!TextUtils.isEmpty(mMovieItem.getReleaseDate())) {
            txtReleaseDate.setText(mMovieItem.getReleaseDate());
        } else {
            txtReleaseDate.setText(UNAVAILABLE);
        }

        if (!TextUtils.isEmpty(mMovieItem.getOverview())) {
            txtOverview.setText(mMovieItem.getOverview());
        } else {
            txtOverview.setText(UNAVAILABLE);
        }
    }

    private MovieItem getMovieItem() {
        MovieItem movieItem = new MovieItem();
        Bundle bundle = getArguments();
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
