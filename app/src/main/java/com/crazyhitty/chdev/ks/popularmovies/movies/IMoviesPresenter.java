package com.crazyhitty.chdev.ks.popularmovies.movies;

import android.content.Context;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public interface IMoviesPresenter {
    void attemptMoviesLoadingByPopularity(Context context, boolean isOffline);

    void attemptMoviesLoadingByUserRating(Context context, boolean isOffline);
}
