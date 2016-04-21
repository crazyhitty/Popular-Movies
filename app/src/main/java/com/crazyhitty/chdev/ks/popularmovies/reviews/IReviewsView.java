package com.crazyhitty.chdev.ks.popularmovies.reviews;

import com.crazyhitty.chdev.ks.popularmovies.models.ReviewItem;

import java.util.List;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public interface IReviewsView {
    void reviewsLoaded(List<ReviewItem> reviewItems);

    void reviewsLoadingFailure(String message);
}
