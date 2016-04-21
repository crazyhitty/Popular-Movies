package com.crazyhitty.chdev.ks.popularmovies.videos;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public interface IVideosInteractor {
    void fetchVideoListingFromServer(OnVideosLoadListener onVideosLoadListener, int id);
}
