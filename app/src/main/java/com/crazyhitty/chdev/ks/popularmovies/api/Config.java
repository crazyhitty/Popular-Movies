package com.crazyhitty.chdev.ks.popularmovies.api;

/**
 * Created by Kartik_ch on 2/8/2016.
 */
public class Config {

    //this key will be used as long as user don't add his own API key using Settings menu in the application itself.
    //So, just add your API key here or manually add it in the application itself.
    public static final String THE_MOVIE_DB_API_KEY = "ENTER_YOUR_API_KEY_HERE";

    //DONOT MODIFY THIS
    public static final String DISCOVER_MOVIES_API_POPULARITY = "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&page=1&api_key=";

    //DONOT MODIFY THIS
    public static final String DISCOVER_MOVIES_API_USER_RATING = "http://api.themoviedb.org/3/discover/movie?sort_by=vote_average.desc&page=1&api_key=";

    //DONOT MODIFY THIS
    public static final String IMAGE_PATH_LOW = "http://image.tmdb.org/t/p/w185/";

    //DONOT MODIFY THIS
    public static final String IMAGE_PATH_MEDIUM = "http://image.tmdb.org/t/p/w342/";

    //DONOT MODIFY THIS
    public static final String IMAGE_PATH_HIGH = "http://image.tmdb.org/t/p/w500/";

    //DONOT MODIFY THIS
    public static final String IMAGE_PATH_ULTRA = "http://image.tmdb.org/t/p/w780/";

    //DONOT MODIFY THIS
    //not recommended, very high size
    public static final String IMAGE_PATH_ORIGINAL = "http://image.tmdb.org/t/p/original/";
}