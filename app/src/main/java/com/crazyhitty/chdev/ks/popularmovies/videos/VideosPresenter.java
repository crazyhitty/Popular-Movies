package com.crazyhitty.chdev.ks.popularmovies.videos;

import com.crazyhitty.chdev.ks.popularmovies.models.VideoItem;

import java.util.List;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public class VideosPresenter implements IVideosPresenter, OnVideosLoadListener {
    private IVideosView mView;
    private VideosInteractor mVideosInteractor;

    public VideosPresenter(IVideosView mView) {
        this.mView = mView;
    }

    public void attemptLoadingVideos(int id) {
        mVideosInteractor = new VideosInteractor();
        mVideosInteractor.fetchVideoListingFromServer(this, id);
    }

    @Override
    public void onSuccess(List<VideoItem> videoItems) {
        mView.videosLoaded(videoItems);
    }

    @Override
    public void onFailure(String message) {
        mView.videosLoadingFailure(message);
    }
}
