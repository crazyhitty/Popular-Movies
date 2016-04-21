package com.crazyhitty.chdev.ks.popularmovies.movies;

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

    public void attemptMoviesLoadingByPopularity(int page, boolean isOffline) {
        mMoviesInteractor = new MoviesInteractor();
        if (isOffline) {
            mMoviesInteractor.fetchDataByPopularityOffline(MoviesPresenter.this);
        } else {
            mMoviesInteractor.fetchDataByPopularity(MoviesPresenter.this, page);
        }
    }

    public void attemptMoviesLoadingByUserRating(int page, boolean isOffline) {
        mMoviesInteractor = new MoviesInteractor();
        if (isOffline) {
            mMoviesInteractor.fetchDataByUserRatingOffline(MoviesPresenter.this);
        } else {
            mMoviesInteractor.fetchDataByUserRating(MoviesPresenter.this, page);
        }
    }

    @Override
    public void attemptMoviesLoadingByFavorites() {
        mMoviesInteractor = new MoviesInteractor();
        mMoviesInteractor.fetchDataByFavorites(this);
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
    public void onMoviesLoadedByFavorites(List<MovieItem> movieItems) {
        mView.moviesLoadedByFavorites(movieItems);
    }

    @Override
    public void onFailure(String message) {
        mView.moviesLoadingFailed(message);
    }
}
