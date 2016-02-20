package com.crazyhitty.chdev.ks.popularmovies.movies;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.popularmovies.api.ApiConnection;
import com.crazyhitty.chdev.ks.popularmovies.api.OnApiExecuteListener;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import okhttp3.Request;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public class MoviesInteractor implements IMoviesInteractor {
    //json params for movies
    private static final String PAGE = "page";
    private static final String RESULTS = "results";
    private static final String ADULT = "adult";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String GENRE_IDS = "genre_ids";
    private static final String ID = "id";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String POSTER_PATH = "poster_path";
    private static final String POPULARITY = "popularity";
    private static final String TITLE = "title";
    private static final String VIDEO = "video";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";

    private OnMoviesLoadListener mOnMoviesLoadListener;
    private Context mContext;

    public void fetchDataByPopularity(final OnMoviesLoadListener onMoviesLoadListener, Context context) {
        this.mOnMoviesLoadListener = onMoviesLoadListener;
        this.mContext = context;

        Request request = new Request.Builder()
                .url(SettingPreferences.API_SORT_BY_POPULARITY)
                .build();

        new ApiConnection(new OnApiExecuteListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                new ParseJsonAsyncTask(jsonObject, "by_popularity").execute();
            }

            @Override
            public void onFailure(String message) {
                onMoviesLoadListener.onFailure(message);
            }
        }).connect(request);
    }

    public void fetchDataByUserRating(final OnMoviesLoadListener onMoviesLoadListener, Context context) {
        this.mOnMoviesLoadListener = onMoviesLoadListener;
        this.mContext = context;

        Request request = new Request.Builder()
                .url(SettingPreferences.API_SORT_BY_RATING)
                .build();

        new ApiConnection(new OnApiExecuteListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                new ParseJsonAsyncTask(jsonObject, "by_rating").execute();
            }

            @Override
            public void onFailure(String message) {
                onMoviesLoadListener.onFailure(message);
            }
        }).connect(request);
    }

    public void fetchDataByPopularityOffline(OnMoviesLoadListener onMoviesLoadListener, Context context) {
        List<MovieItem> movieItems = getMovieItemsFromRealm(context, "by_popularity");
        if (!movieItems.isEmpty()) {
            onMoviesLoadListener.onMoviesLoadedByPopularity(movieItems);
        } else {
            onMoviesLoadListener.onFailure("none available");
        }
    }

    public void fetchDataByUserRatingOffline(OnMoviesLoadListener onMoviesLoadListener, Context context) {
        List<MovieItem> movieItems = getMovieItemsFromRealm(context, "by_rating");
        if (!movieItems.isEmpty()) {
            onMoviesLoadListener.onMoviesLoadedByUserRating(movieItems);
        } else {
            onMoviesLoadListener.onFailure("none available");
        }
    }

    private List<MovieItem> getMovieItemsFromRealm(Context context, String type) {
        Realm realm = Realm.getInstance(context);
        RealmResults<MovieItem> results = realm.where(MovieItem.class).findAll();
        return results;
    }

    private class ParseJsonAsyncTask extends AsyncTask<Void, Void, String> {
        List<MovieItem> mMovieItems = new ArrayList<>();
        private JSONObject mJsonObject;
        private String mType;

        public ParseJsonAsyncTask(JSONObject mJsonObject, String mType) {
            this.mJsonObject = mJsonObject;
            this.mType = mType;
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                int page = mJsonObject.getInt(PAGE);
                JSONArray jsonArrayResults = mJsonObject.getJSONArray(RESULTS);
                for (int i = 0; i < jsonArrayResults.length(); i++) {
                    MovieItem movieItem = new MovieItem();
                    JSONObject jsonObjectResult = jsonArrayResults.getJSONObject(i);

                    boolean adult = jsonObjectResult.getBoolean(ADULT);
                    String backdropPath = jsonObjectResult.getString(BACKDROP_PATH);

                    JSONArray jsonArrayGenreIds = jsonObjectResult.getJSONArray(GENRE_IDS);
                    int[] genreIds = new int[jsonArrayGenreIds.length()];
                    for (int j = 0; j < jsonArrayGenreIds.length(); j++) {
                        genreIds[j] = jsonArrayGenreIds.getInt(j);
                    }

                    int id = jsonObjectResult.getInt(ID);
                    String originalLanguage = jsonObjectResult.getString(ORIGINAL_LANGUAGE);
                    String originalTitle = jsonObjectResult.getString(ORIGINAL_TITLE);
                    String overview = jsonObjectResult.getString(OVERVIEW);
                    String releaseDate = jsonObjectResult.getString(RELEASE_DATE);
                    String posterPath = jsonObjectResult.getString(POSTER_PATH);
                    double popularity = jsonObjectResult.getDouble(POPULARITY);
                    String title = jsonObjectResult.getString(TITLE);
                    boolean video = jsonObjectResult.getBoolean(VIDEO);
                    double voteAverage = jsonObjectResult.getDouble(VOTE_AVERAGE);
                    int voteCount = jsonObjectResult.getInt(VOTE_COUNT);

                    movieItem.setPage(page);
                    movieItem.setAdult(adult);
                    movieItem.setBackdropPath(backdropPath);
                    //movieItem.setGenreIds(genreIds);
                    movieItem.setId(id);
                    movieItem.setOriginalLanguage(originalLanguage);
                    movieItem.setOriginalTitle(originalTitle);
                    movieItem.setOverview(overview);
                    movieItem.setReleaseDate(releaseDate);
                    movieItem.setPosterPath(posterPath);
                    movieItem.setPopularity(popularity);
                    movieItem.setTitle(title);
                    movieItem.setVideo(video);
                    movieItem.setVoteAverage(voteAverage);
                    movieItem.setVoteCount(voteCount);

                    mMovieItems.add(movieItem);
                }

                return "success";
            } catch (JSONException e) {
                e.printStackTrace();
                return e.getMessage();
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (TextUtils.equals(s, "success")) {
                saveData();
                switch (mType) {
                    case "by_popularity":
                        mOnMoviesLoadListener.onMoviesLoadedByPopularity(mMovieItems);
                        break;
                    case "by_rating":
                        mOnMoviesLoadListener.onMoviesLoadedByUserRating(mMovieItems);
                        break;
                }
            } else {
                mOnMoviesLoadListener.onFailure(s);
            }
        }

        private void saveData() {
            for (int i = 0; i < mMovieItems.size(); i++) {
                mMovieItems.get(i).setType(mType);
            }

            Realm realm = Realm.getInstance(mContext);
            realm.beginTransaction();
            realm.clear(MovieItem.class);
            realm.copyToRealmOrUpdate(mMovieItems);
            realm.commitTransaction();
        }
    }
}
