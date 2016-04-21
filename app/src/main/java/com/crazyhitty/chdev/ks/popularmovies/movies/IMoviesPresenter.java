package com.crazyhitty.chdev.ks.popularmovies.movies;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public interface IMoviesPresenter {
    void attemptMoviesLoadingByPopularity(int page, boolean isOffline);

    void attemptMoviesLoadingByUserRating(int page, boolean isOffline);

    void attemptMoviesLoadingByFavorites();
}
