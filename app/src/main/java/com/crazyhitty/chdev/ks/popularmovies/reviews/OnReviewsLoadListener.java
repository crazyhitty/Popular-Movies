package com.crazyhitty.chdev.ks.popularmovies.reviews;

import com.crazyhitty.chdev.ks.popularmovies.models.ReviewItem;

import java.util.List;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public interface OnReviewsLoadListener {
    void onSuccess(List<ReviewItem> reviewItems);

    void onFailure(String message);
}
