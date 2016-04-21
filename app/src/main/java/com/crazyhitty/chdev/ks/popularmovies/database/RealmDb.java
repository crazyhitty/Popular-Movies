package com.crazyhitty.chdev.ks.popularmovies.database;

import com.crazyhitty.chdev.ks.popularmovies.models.FavoriteMovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Kartik_ch on 3/24/2016.
 */
public class RealmDb {
    private static final String ID = "id";
    private static final String TYPE = "type";

    public static void addFavoriteMovie(FavoriteMovieItem favoriteMovieItem) {
        // Obtain a Realm instance
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        //... add or update objects here ...
        realm.copyToRealmOrUpdate(favoriteMovieItem);
        realm.commitTransaction();
    }

    public static void removeFavoriteMovie(final FavoriteMovieItem favoriteMovieItem) {
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<FavoriteMovieItem> favoriteMovieItems = realm.where(FavoriteMovieItem.class).equalTo(ID, favoriteMovieItem.getId()).findAll();
                favoriteMovieItems.clear();
            }
        });
    }

    public static List<FavoriteMovieItem> getAllFavoriteMovies() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FavoriteMovieItem> favoriteMovieItems = realm.where(FavoriteMovieItem.class)
                .findAll();
        return favoriteMovieItems;
    }

    public static List<MovieItem> getAllFavoriteMoviesAlternate() {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FavoriteMovieItem> favoriteMovieItems = realm.where(FavoriteMovieItem.class)
                .findAll();

        List<MovieItem> movieItems = new ArrayList<>();
        for (FavoriteMovieItem favoriteMovieItem : favoriteMovieItems) {
            MovieItem movieItem = new MovieItem();
            movieItem.setPage(favoriteMovieItem.getPage());
            movieItem.setBackdropPath(favoriteMovieItem.getBackdropPath());
            movieItem.setId(favoriteMovieItem.getId());
            movieItem.setOriginalLanguage(favoriteMovieItem.getOriginalLanguage());
            movieItem.setOriginalTitle(favoriteMovieItem.getOriginalTitle());
            movieItem.setOverview(favoriteMovieItem.getOverview());
            movieItem.setReleaseDate(favoriteMovieItem.getReleaseDate());
            movieItem.setPosterPath(favoriteMovieItem.getPosterPath());
            movieItem.setPopularity(favoriteMovieItem.getPopularity());
            movieItem.setTitle(favoriteMovieItem.getTitle());
            movieItem.setVideo(favoriteMovieItem.isVideo());
            movieItem.setVoteAverage(favoriteMovieItem.getVoteAverage());
            movieItem.setVoteCount(favoriteMovieItem.getVoteCount());
            movieItem.setType(favoriteMovieItem.getType());

            movieItems.add(movieItem);
        }

        return movieItems;
    }

    public static boolean isMovieFavorite(MovieItem movieItem) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<FavoriteMovieItem> movieItemRealmResults = realm.where(FavoriteMovieItem.class).equalTo(ID, movieItem.getId()).findAll();
        return !movieItemRealmResults.isEmpty();
    }

    public static void saveMovies(List<MovieItem> movieItems, String type) {
        for (int i = 0; i < movieItems.size(); i++) {
            movieItems.get(i).setType(type);
        }

        Realm realm = Realm.getDefaultInstance();

        //find old movies
        RealmResults<MovieItem> movieItemRealmResults = realm.where(MovieItem.class)
                .equalTo(TYPE, type)
                .findAll();

        realm.beginTransaction();

        //clear old results
        movieItemRealmResults.clear();

        realm.copyToRealmOrUpdate(movieItems);
        realm.commitTransaction();
    }

    public static List<MovieItem> getMovieItemsFromRealm(String type) {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<MovieItem> movieItemRealmResults = realm.where(MovieItem.class)
                .equalTo(TYPE, type)
                .findAll();
        return movieItemRealmResults;
    }
}
