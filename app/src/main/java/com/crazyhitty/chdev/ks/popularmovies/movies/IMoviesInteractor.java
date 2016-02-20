package com.crazyhitty.chdev.ks.popularmovies.movies;

import android.content.Context;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public interface IMoviesInteractor {
    void fetchDataByPopularity(OnMoviesLoadListener onMoviesLoadListener, Context context);

    void fetchDataByUserRating(OnMoviesLoadListener onMoviesLoadListener, Context context);

    void fetchDataByPopularityOffline(OnMoviesLoadListener onMoviesLoadListener, Context context);

    void fetchDataByUserRatingOffline(OnMoviesLoadListener onMoviesLoadListener, Context context);
}
