package com.crazyhitty.chdev.ks.popularmovies.reviews;

import android.os.AsyncTask;
import android.text.TextUtils;

import com.crazyhitty.chdev.ks.popularmovies.api.ApiConnection;
import com.crazyhitty.chdev.ks.popularmovies.api.Config;
import com.crazyhitty.chdev.ks.popularmovies.api.OnApiExecuteListener;
import com.crazyhitty.chdev.ks.popularmovies.models.ReviewItem;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Request;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public class ReviewsInteractor implements IReviewsInteractor, OnApiExecuteListener {
    private static final String RESULTS = "results";

    private static final String REVIEWS = "reviews";
    private static final String SEPARATOR = "/";
    private static final String API_KEY_GET_PARAMS = "?api_key=";

    //json params for reviews
    private static final String ID = "id";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";

    private OnReviewsLoadListener mOnReviewsLoadListener;

    public void fetchReviewsFromServer(OnReviewsLoadListener onReviewsLoadListener, int id) {
        mOnReviewsLoadListener = onReviewsLoadListener;

        ApiConnection apiConnection = new ApiConnection(this);

        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(Config.MOVIE_API);
        urlBuilder.append(id);
        urlBuilder.append(SEPARATOR);
        urlBuilder.append(REVIEWS);
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
        mOnReviewsLoadListener.onFailure(message);
    }

    private class ParseJsonAsyncTask extends AsyncTask<Void, Void, String> {
        List<ReviewItem> mReviewItems = new ArrayList<>();
        private JSONObject mJsonObject;

        public ParseJsonAsyncTask(JSONObject mJsonObject) {
            this.mJsonObject = mJsonObject;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mReviewItems.clear();
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                JSONArray jsonArrayResults = mJsonObject.getJSONArray(RESULTS);
                for (int i = 0; i < jsonArrayResults.length(); i++) {
                    JSONObject jsonObjectResult = jsonArrayResults.getJSONObject(i);

                    String id = jsonObjectResult.getString(ID);
                    String author = jsonObjectResult.getString(AUTHOR);
                    String content = jsonObjectResult.getString(CONTENT);
                    String url = jsonObjectResult.getString(URL);

                    ReviewItem reviewItem = new ReviewItem();
                    reviewItem.setId(id);
                    reviewItem.setAuthor(author);
                    reviewItem.setContent(content);
                    reviewItem.setUrl(url);

                    mReviewItems.add(reviewItem);
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
                mOnReviewsLoadListener.onSuccess(mReviewItems);
            } else {
                mOnReviewsLoadListener.onFailure(s);
            }
        }

        private void saveData() {

        }
    }
}
