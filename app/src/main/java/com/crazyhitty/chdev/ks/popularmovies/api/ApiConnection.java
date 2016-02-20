package com.crazyhitty.chdev.ks.popularmovies.api;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Kartik_ch on 2/8/2016.
 */
public class ApiConnection implements Callback {
    private static final int UNAUTHORIZED = 401;

    private OkHttpClient mOkHttpClient;
    private Call mOkHttpCall;
    private OnApiExecuteListener mOnApiExecuteListener;

    public ApiConnection(OnApiExecuteListener mOnApiExecuteListener) {
        this.mOnApiExecuteListener = mOnApiExecuteListener;
    }

    public void connect(Request request) {
        mOkHttpClient = new OkHttpClient();
        mOkHttpCall = mOkHttpClient.newCall(request);
        mOkHttpCall.enqueue(this);
    }

    @Override
    public void onFailure(Call call, final IOException e) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mOnApiExecuteListener.onFailure(e.getMessage());
            }
        });
    }

    @Override
    public void onResponse(Call call, final Response response) throws IOException {
        String errorMsg = null;
        if (response.isSuccessful()) {
            try {
                String responseBody = response.body().string();
                final JSONObject jsonObjectResponse = new JSONObject(responseBody);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mOnApiExecuteListener.onSuccess(jsonObjectResponse);
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
                errorMsg = e.getMessage();
            } catch (JSONException e) {
                e.printStackTrace();
                errorMsg = e.getMessage();
            }
        } else {
            //errorMsg=response.toString();
            int responseCode = response.code();
            if (responseCode == UNAUTHORIZED) {
                errorMsg = "Unauthorized access, please review your API key";
            } else {
                errorMsg = "Unable to contact server, please try again.";
            }
        }
        if (errorMsg != null) {
            final String finalErrorMsg = errorMsg;
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mOnApiExecuteListener.onFailure(finalErrorMsg);
                }
            });
        }

    }

    //got help from https://gist.github.com/petitviolet/b27b61972a6a18ca7b9e
    //inorder to run okhttp3 callbacks on main thread
    private void runOnUiThread(Runnable task) {
        new Handler(Looper.getMainLooper()).post(task);
    }
}
