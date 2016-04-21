package com.crazyhitty.chdev.ks.popularmovies.videos;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.popularmovies.api.ApiConnection;
import com.crazyhitty.chdev.ks.popularmovies.api.Config;
import com.crazyhitty.chdev.ks.popularmovies.api.OnApiExecuteListener;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;
import com.crazyhitty.chdev.ks.popularmovies.models.VideoItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public class VideosInteractor implements IVideosInteractor, OnApiExecuteListener {
    private static final String RESULTS = "results";

    private static final String VIDEOS = "videos";
    private static final String SEPARATOR = "/";
    private static final String API_KEY_GET_PARAMS = "?api_key=";

    //json params for videos
    private static final String ID = "id";
    private static final String ISO = "iso_639_1";
    private static final String KEY = "key";
    private static final String NAME = "name";
    private static final String SITE = "site";
    private static final String SIZE = "size";
    private static final String TYPE = "type";

    private OnVideosLoadListener mOnVideosLoadListener;

    public void fetchVideoListingFromServer(OnVideosLoadListener onVideosLoadListener, int id) {
        mOnVideosLoadListener = onVideosLoadListener;

        ApiConnection apiConnection = new ApiConnection(this);

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(Config.MOVIE_API);
        urlBuilder.append(id);
        urlBuilder.append(SEPARATOR);
        urlBuilder.append(VIDEOS);
        urlBuilder.append(API_KEY_GET_PARAMS);
        urlBuilder.append(SettingPreferences.API_KEY);

        Request request = new Request.Builder()
                .url(urlBuilder.toString())
                .build();

        apiConnection.connect(request);
    }

    @Override
    public void onSuccess(JSONObject jsonObject) {
        new ParseJsonAsyncTask(jsonObject).execute();
    }

    @Override
    public void onFailure(String message) {
        mOnVideosLoadListener.onFailure(message);
    }

    private class ParseJsonAsyncTask extends AsyncTask<Void, Void, String> {
        List<VideoItem> mVideoItems = new ArrayList<>();
        private JSONObject mJsonObject;

        public ParseJsonAsyncTask(JSONObject mJsonObject) {
            this.mJsonObject = mJsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mVideoItems.clear();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                JSONArray jsonArrayResults = mJsonObject.getJSONArray(RESULTS);
                for (int i = 0; i < jsonArrayResults.length(); i++) {
                    JSONObject jsonObjectResult = jsonArrayResults.getJSONObject(i);

                    String id = jsonObjectResult.getString(ID);
                    String language = jsonObjectResult.getString(ISO);
                    String key = jsonObjectResult.getString(KEY);
                    String name = jsonObjectResult.getString(NAME);
                    String site = jsonObjectResult.getString(SITE);
                    int size = jsonObjectResult.getInt(SIZE);
                    String type = jsonObjectResult.getString(TYPE);

                    VideoItem videoItem = new VideoItem();
                    videoItem.setId(id);
                    videoItem.setLanguage(language);
                    videoItem.setYoutubeKey(key);
                    videoItem.setName(name);
                    videoItem.setSiteName(site);
                    videoItem.setSize(size);
                    videoItem.setType(type);

                    mVideoItems.add(videoItem);
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
                //saveData();
                mOnVideosLoadListener.onSuccess(mVideoItems);
            } else {
                mOnVideosLoadListener.onFailure(s);
            }
        }

        private void saveData() {

        }
    }
}
