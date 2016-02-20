package com.crazyhitty.chdev.ks.popularmovies.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public class MovieItem extends RealmObject {
    public static final String PAGE_KEY = "page_key";
    public static final String ADULT_KEY = "adult_key";
    public static final String BACKDROP_PATH_KEY = "backdrop_path_key";
    public static final String ID_KEY = "id_key";
    public static final String ORIGINAL_LANGUAGE_KEY = "original_language_key";
    public static final String ORIGINAL_TITLE_KEY = "original_title_key";
    public static final String OVERVIEW_KEY = "overview_key";
    public static final String RELEASE_DATE_KEY = "release_date_key";
    public static final String POSTER_PATH_KEY = "poster_path_key";
    public static final String POPULARITY_KEY = "popularity_key";
    public static final String TITLE_KEY = "title_key";
    public static final String VIDEO_KEY = "video_key";
    public static final String VOTE_AVERAGE_KEY = "vote_average_key";
    public static final String VOTE_COUNT_KEY = "vote_count_key";
    public static final String TYPE_KEY = "type_key";

    private int page;
    private boolean adult;
    private String backdropPath;

    //private int[] genreIds;

    @PrimaryKey
    private int id;

    private String originalLanguage;
    private String originalTitle;
    private String overview;
    private String releaseDate;
    private String posterPath;
    private double popularity;
    private String title;
    private boolean video;
    private double voteAverage;
    private int voteCount;

    private String type;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public boolean isAdult() {
        return adult;
    }

    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    /*public int[] getGenreIds() {
        return genreIds;
    }

    public void setGenreIds(int[] genreIds) {
        this.genreIds = genreIds;
    }*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getPopularity() {
        return popularity;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isVideo() {
        return video;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
