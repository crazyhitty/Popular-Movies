package com.crazyhitty.chdev.ks.popularmovies.videos;

import com.crazyhitty.chdev.ks.popularmovies.models.VideoItem;

import java.util.List;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public interface OnVideosLoadListener {
    void onSuccess(List<VideoItem> videoItems);

    void onFailure(String message);
}
