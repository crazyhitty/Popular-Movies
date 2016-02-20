package com.crazyhitty.chdev.ks.popularmovies.api;

import org.json.JSONObject;

/**
 * Created by Kartik_ch on 2/8/2016.
 */
public interface OnApiExecuteListener {
    void onSuccess(JSONObject jsonObject);

    void onFailure(String message);
}
