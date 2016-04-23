package com.crazyhitty.chdev.ks.popularmovies.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.api.Config;
import com.crazyhitty.chdev.ks.popularmovies.utils.SharedPrefUtil;

/**
 * Created by Kartik_ch on 2/13/2016.
 */
public class SettingPreferences {
    public static final String PHONE = "phone";
    public static final String TAB_7_INCH = "tab_7_inch";
    public static final String TAB_10_INCH = "tab_10_inch";
    public static final String BY_POPULARITY = "by_popularity";
    public static final String BY_RATING = "by_rating";
    public static final String BY_FAVORITES = "by_favorites";

    private static final String DISPLAY_TYPE = "display_type";
    private static final String SORTING_TYPE = "sorting_type";

    private static final String FONT_SIZE = "font_size";

    public static String POSTER_THUMBNAIL_IMAGE_PATH = null;
    public static String BACKDROP_IMAGE_PATH = null;

    /**
     * <p>DON'T MODIFY THIS KEY MANUALLY</p>
     * <p>If you want to modify current API key just go to {@link Config} in api package and change the key there.</p>
     */
    public static String API_KEY = null;
    public static String API_SORT_BY_POPULARITY = null;
    public static String API_SORT_BY_RATING = null;

    public static boolean SORT_BY_POPULARITY = true;
    public static boolean SORT_BY_RATING = false;
    public static boolean SORT_BY_FAVORITES = false;
    public static boolean DISPLAY_TYPE_PHONE = true;
    public static boolean DISPLAY_TYPE_7_INCH_TAB = false;
    public static boolean DISPLAY_TYPE_10_INCH_TAB = false;

    public static int FONT_SIZE_VALUE = 10;

    public static void init(Context context) {
        initDisplayType(context);
        initSortingType(context);
        initPosterThumbnailQuality(context);
        initBackdropQuality(context);
        initApi(context);
        initFontSize(context);
    }

    public static void saveDisplayType(Context context, String value) {
        new SharedPrefUtil(context).saveString(DISPLAY_TYPE, value);
        initDisplayType(context);
    }

    private static void initDisplayType(Context context) {
        String displayType = new SharedPrefUtil(context).getString(DISPLAY_TYPE);
        if (displayType != null) {
            switch (displayType) {
                case PHONE:
                    DISPLAY_TYPE_PHONE = true;
                    DISPLAY_TYPE_7_INCH_TAB = false;
                    DISPLAY_TYPE_10_INCH_TAB = false;
                    break;
                case TAB_7_INCH:
                    DISPLAY_TYPE_PHONE = false;
                    DISPLAY_TYPE_7_INCH_TAB = true;
                    DISPLAY_TYPE_10_INCH_TAB = false;
                    break;
                case TAB_10_INCH:
                    DISPLAY_TYPE_PHONE = false;
                    DISPLAY_TYPE_7_INCH_TAB = false;
                    DISPLAY_TYPE_10_INCH_TAB = true;
                    break;
            }
        } else {
            DISPLAY_TYPE_PHONE = true;
            DISPLAY_TYPE_7_INCH_TAB = false;
            DISPLAY_TYPE_10_INCH_TAB = false;
        }
    }

    public static void saveSortingType(Context context, String value) {
        new SharedPrefUtil(context).saveString(SORTING_TYPE, value);
        initSortingType(context);
    }

    private static void initSortingType(Context context) {
        String sortingType = new SharedPrefUtil(context).getString(SORTING_TYPE);
        if (sortingType != null) {
            switch (sortingType) {
                case BY_POPULARITY:
                    SORT_BY_POPULARITY = true;
                    SORT_BY_RATING = false;
                    SORT_BY_FAVORITES = false;
                    break;
                case BY_RATING:
                    SORT_BY_POPULARITY = false;
                    SORT_BY_RATING = true;
                    SORT_BY_FAVORITES = false;
                    break;
                case BY_FAVORITES:
                    SORT_BY_POPULARITY = false;
                    SORT_BY_RATING = false;
                    SORT_BY_FAVORITES = true;
                    break;
            }
        } else {
            SORT_BY_POPULARITY = true;
            SORT_BY_RATING = false;
            SORT_BY_FAVORITES = false;
        }
    }

    private static void initPosterThumbnailQuality(Context context) {
        String posterThumbnailQualityKey = context.getString(R.string.poster_thumbnail_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String quality = sharedPreferences.getString(posterThumbnailQualityKey, context.getString(R.string.poster_thumbnail_default_value));

        switch (quality) {
            case "low":
                POSTER_THUMBNAIL_IMAGE_PATH = Config.IMAGE_PATH_LOW;
                break;
            case "medium":
                POSTER_THUMBNAIL_IMAGE_PATH = Config.IMAGE_PATH_MEDIUM;
                break;
            case "high":
                POSTER_THUMBNAIL_IMAGE_PATH = Config.IMAGE_PATH_HIGH;
                break;
            case "ultra":
                POSTER_THUMBNAIL_IMAGE_PATH = Config.IMAGE_PATH_ULTRA;
                break;
            case "original":
                POSTER_THUMBNAIL_IMAGE_PATH = Config.IMAGE_PATH_ORIGINAL;
                break;
        }
    }

    private static void initBackdropQuality(Context context) {
        String backdropQualityKey = context.getString(R.string.backdrop_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String quality = sharedPreferences.getString(backdropQualityKey, context.getString(R.string.backdrop_default_value));

        switch (quality) {
            case "low":
                BACKDROP_IMAGE_PATH = Config.IMAGE_PATH_LOW;
                break;
            case "medium":
                BACKDROP_IMAGE_PATH = Config.IMAGE_PATH_MEDIUM;
                break;
            case "high":
                BACKDROP_IMAGE_PATH = Config.IMAGE_PATH_HIGH;
                break;
            case "ultra":
                BACKDROP_IMAGE_PATH = Config.IMAGE_PATH_ULTRA;
                break;
            case "original":
                BACKDROP_IMAGE_PATH = Config.IMAGE_PATH_ORIGINAL;
                break;
        }
    }

    private static void initApi(Context context) {
        String apiKey = context.getString(R.string.modify_api_key_pref_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String api = sharedPreferences.getString(apiKey, null);

        if (api == null) {
            API_KEY = Config.THE_MOVIE_DB_API_KEY;
        } else {
            API_KEY = api;
        }

        API_SORT_BY_POPULARITY = Config.DISCOVER_MOVIES_API_POPULARITY + API_KEY;
        API_SORT_BY_RATING = Config.DISCOVER_MOVIES_API_USER_RATING + API_KEY;
    }

    private static void initFontSize(Context context) {
        String fontSizeKey = context.getString(R.string.font_size_key);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String fontSize = sharedPreferences.getString(fontSizeKey, context.getString(R.string.font_size_default_value));
        FONT_SIZE_VALUE = Integer.parseInt(fontSize);
    }
}
