package com.crazyhitty.chdev.ks.popularmovies.movies;

import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;

import java.util.List;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public interface OnMoviesLoadListener {
    void onMoviesLoadedByPopularity(List<MovieItem> movieItems);

    void onMoviesLoadedByUserRating(List<MovieItem> movieItems);

    void onMoviesLoadedByFavorites(List<MovieItem> movieItems);

    void onFailure(String message);
}
