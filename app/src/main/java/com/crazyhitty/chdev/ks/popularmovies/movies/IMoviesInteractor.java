package com.crazyhitty.chdev.ks.popularmovies.movies;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public interface IMoviesInteractor {
    void fetchDataByPopularity(OnMoviesLoadListener onMoviesLoadListener, int page);

    void fetchDataByUserRating(OnMoviesLoadListener onMoviesLoadListener, int page);

    void fetchDataByPopularityOffline(OnMoviesLoadListener onMoviesLoadListener);

    void fetchDataByUserRatingOffline(OnMoviesLoadListener onMoviesLoadListener);

    void fetchDataByFavorites(OnMoviesLoadListener onMoviesLoadListener);
}
