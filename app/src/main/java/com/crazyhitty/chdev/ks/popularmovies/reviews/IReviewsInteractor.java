package com.crazyhitty.chdev.ks.popularmovies.reviews;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public interface IReviewsInteractor {
    void fetchReviewsFromServer(OnReviewsLoadListener onReviewsLoadListener, int id);
}
