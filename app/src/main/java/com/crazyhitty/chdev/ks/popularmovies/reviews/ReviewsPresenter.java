package com.crazyhitty.chdev.ks.popularmovies.reviews;

import com.crazyhitty.chdev.ks.popularmovies.models.ReviewItem;

import java.util.List;

/**
 * Created by Kartik_ch on 3/8/2016.
 */
public class ReviewsPresenter implements IReviewsPresenter, OnReviewsLoadListener {
    private IReviewsView mView;
    private ReviewsInteractor mReviewsInteractor;

    public ReviewsPresenter(IReviewsView mView) {
        this.mView = mView;
    }

    public void attemptLoadingReviews(int id) {
        mReviewsInteractor = new ReviewsInteractor();
        mReviewsInteractor.fetchReviewsFromServer(this, id);
    }

    @Override
    public void onSuccess(List<ReviewItem> reviewItems) {
        mView.reviewsLoaded(reviewItems);
    }

    @Override
    public void onFailure(String message) {
        mView.reviewsLoadingFailure(message);
    }
}
