package com.crazyhitty.chdev.ks.popularmovies.movies;

import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;

import java.util.List;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public interface IMoviesView {
    void moviesLoadedByPopularity(List<MovieItem> movieItems);

    void moviesLoadedByUserRating(List<MovieItem> movieItems);

    void moviesLoadedByFavorites(List<MovieItem> movieItems);

    void moviesLoadingFailed(String message);
}
