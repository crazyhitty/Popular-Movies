package com.crazyhitty.chdev.ks.popularmovies.movies;

import android.content.Context;

import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;

import java.util.List;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public class MoviesPresenter implements IMoviesPresenter, OnMoviesLoadListener {
    private IMoviesView mView;
    private MoviesInteractor mMoviesInteractor;

    public MoviesPresenter(IMoviesView mView) {
        this.mView = mView;
    }

    public void attemptMoviesLoadingByPopularity(Context context, boolean isOffline) {
        mMoviesInteractor = new MoviesInteractor();
        if (isOffline) {
            mMoviesInteractor.fetchDataByPopularityOffline(MoviesPresenter.this, context);
        } else {
            mMoviesInteractor.fetchDataByPopularity(MoviesPresenter.this, context);
        }
    }

    public void attemptMoviesLoadingByUserRating(Context context, boolean isOffline) {
        mMoviesInteractor = new MoviesInteractor();
        if (isOffline) {
            mMoviesInteractor.fetchDataByUserRatingOffline(MoviesPresenter.this, context);
        } else {
            mMoviesInteractor.fetchDataByUserRating(MoviesPresenter.this, context);
        }
    }

    @Override
    public void onMoviesLoadedByPopularity(List<MovieItem> movieItems) {
        mView.moviesLoadedByPopularity(movieItems);
    }

    @Override
    public void onMoviesLoadedByUserRating(List<MovieItem> movieItems) {
        mView.moviesLoadedByUserRating(movieItems);
    }

    @Override
    public void onFailure(String message) {
        mView.moviesLoadingFailed(message);
    }
}
