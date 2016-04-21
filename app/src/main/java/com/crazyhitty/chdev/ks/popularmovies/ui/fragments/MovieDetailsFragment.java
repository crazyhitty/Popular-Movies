package com.crazyhitty.chdev.ks.popularmovies.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.api.Config;
import com.crazyhitty.chdev.ks.popularmovies.database.RealmDb;
import com.crazyhitty.chdev.ks.popularmovies.models.FavoriteMovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.ReviewItem;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;
import com.crazyhitty.chdev.ks.popularmovies.models.VideoItem;
import com.crazyhitty.chdev.ks.popularmovies.reviews.IReviewsView;
import com.crazyhitty.chdev.ks.popularmovies.reviews.ReviewsPresenter;
import com.crazyhitty.chdev.ks.popularmovies.utils.NetworkConnectionUtil;
import com.crazyhitty.chdev.ks.popularmovies.videos.IVideosView;
import com.crazyhitty.chdev.ks.popularmovies.videos.VideosPresenter;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Kartik_ch on 2/13/2016.
 */
public class MovieDetailsFragment extends Fragment implements IVideosView, IReviewsView {
    private static final String UNAVAILABLE = "Unavailable";
    private static final String ARG_TWO_PANE = "two_pane_mode";

    @Bind(R.id.text_view_favorite)
    TextView txtFavorite;
    @Bind(R.id.text_view_title)
    TextView txtTitle;
    @Bind(R.id.text_view_rating)
    TextView txtRating;
    @Bind(R.id.text_view_release_date)
    TextView txtReleaseDate;
    @Bind(R.id.text_view_overview)
    TextView txtOverview;
    @Bind(R.id.linear_layout_movie_details)
    LinearLayout linearLayoutMovieDetails;
    @Bind(R.id.linear_layout_no_movies_selected)
    LinearLayout linearLayoutNoMoviesSelected;
    @Bind(R.id.relative_layout_movies_backdrop)
    RelativeLayout relativeLayoutMoviesBackdrop;
    @Bind(R.id.image_view_backdrop)
    ImageView imgBackdrop;

    private MovieItem mMovieItem;
    private VideosPresenter mVideosPresenter;
    private ReviewsPresenter mReviewsPresenter;

    private MaterialDialog mVideosDialog, mReviewsDialog, mReviewExpandDialog;

    public static MovieDetailsFragment newInstance(MovieItem movieItem, boolean isTwoPane) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_TWO_PANE, isTwoPane);
        MovieDetailsFragment movieDetailsFragment = new MovieDetailsFragment();
        movieDetailsFragment.setMovieItem(movieItem);
        movieDetailsFragment.setArguments(args);
        return movieDetailsFragment;
    }

    private void setMovieItem(MovieItem movieItem) {
        mMovieItem = movieItem;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (mMovieItem != null) {
            init();
            linearLayoutMovieDetails.setVisibility(View.VISIBLE);
            linearLayoutNoMoviesSelected.setVisibility(View.INVISIBLE);
            if (getArguments().getBoolean(ARG_TWO_PANE, false)) {
                relativeLayoutMoviesBackdrop.setVisibility(View.VISIBLE);
                setBackDropImage();
            }
        } else {
            linearLayoutMovieDetails.setVisibility(View.GONE);
            linearLayoutNoMoviesSelected.setVisibility(View.VISIBLE);
        }
    }

    private void init() {
        if (mVideosPresenter == null) {
            mVideosPresenter = new VideosPresenter(this);
        }

        if (mReviewsPresenter == null) {
            mReviewsPresenter = new ReviewsPresenter(this);
        }

        if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            mVideosPresenter.attemptLoadingVideos(mMovieItem.getId());
            mReviewsPresenter.attemptLoadingReviews(mMovieItem.getId());
        }

        txtTitle.setText(mMovieItem.getTitle());

        String rating = mMovieItem.getVoteAverage() + " out of 10";
        txtRating.setText(rating);

        if (!TextUtils.isEmpty(mMovieItem.getReleaseDate())) {
            txtReleaseDate.setText(mMovieItem.getReleaseDate());
        } else {
            txtReleaseDate.setText(UNAVAILABLE);
        }

        if (!TextUtils.isEmpty(mMovieItem.getOverview())) {
            txtOverview.setText(mMovieItem.getOverview());
        } else {
            txtOverview.setText(UNAVAILABLE);
        }

        if (isFavorite()) {
            txtFavorite.setText(R.string.unfavorite);
            txtFavorite.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
        }

        if (!getArguments().getBoolean(ARG_TWO_PANE)) {
            txtFavorite.setVisibility(View.GONE);
        }
    }

    private void setBackDropImage() {
        //set backdrop image
        Picasso.with(getActivity())
                .load(SettingPreferences.BACKDROP_IMAGE_PATH + mMovieItem.getBackdropPath())
                .placeholder(R.drawable.light_black_bg)
                .error(R.drawable.ic_image_error_48dp)
                .into(imgBackdrop, new Callback() {
                    @Override
                    public void onSuccess() {
                        imgBackdrop.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    @Override
                    public void onError() {
                        imgBackdrop.setScaleType(ImageView.ScaleType.CENTER);
                    }
                });
    }

    @OnClick(R.id.button_watch_videos)
    public void onWatchVideos() {
        if (mVideosDialog != null) {
            mVideosDialog.show();
        } else {
            Toast.makeText(getActivity(), "Still loading...", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.button_read_reviews)
    public void onReadReviews() {
        if (mReviewsDialog != null) {
            mReviewsDialog.show();
        } else {
            Toast.makeText(getActivity(), "Still loading...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void videosLoaded(List<VideoItem> videoItems) {
        initVideosDialog(videoItems);
    }

    @Override
    public void videosLoadingFailure(String message) {
        Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reviewsLoaded(List<ReviewItem> reviewItems) {
        initReviewsDialog(reviewItems);
    }

    @Override
    public void reviewsLoadingFailure(String message) {
        Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_SHORT).show();
    }

    private void initVideosDialog(final List<VideoItem> videoItems) {
        CharSequence[] videoTitles = new CharSequence[videoItems.size()];

        for (int i = 0; i < videoItems.size(); i++) {
            videoTitles[i] = videoItems.get(i).getName();
        }

        mVideosDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.watch_videos)
                .content(R.string.videos_desc)
                .items(videoTitles)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        String videoUrl = Config.YOUTUBE_VIDEO_URL + videoItems.get(which).getYoutubeKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(videoUrl));
                        startActivity(intent);
                    }
                })
                .negativeText(R.string.dismiss)
                .build();
    }

    private void initReviewsDialog(final List<ReviewItem> reviewItems) {
        CharSequence[] reviewsDesc = new CharSequence[reviewItems.size()];

        for (int i = 0; i < reviewItems.size(); i++) {
            reviewsDesc[i] = "Review by: " + reviewItems.get(i).getAuthor();
        }

        mReviewsDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.read_reviews)
                .content(R.string.reviews_desc)
                .items(reviewsDesc)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        String author = text.toString();
                        String content = reviewItems.get(which).getContent();
                        String url = reviewItems.get(which).getUrl();
                        initReviewExpandDialog(author, content, url);
                        mReviewExpandDialog.show();
                    }
                })
                .negativeText(R.string.dismiss)
                .build();
    }

    private void initReviewExpandDialog(String author, String reviewContent, final String url) {
        mReviewExpandDialog = new MaterialDialog.Builder(getActivity())
                .title(author)
                .content(reviewContent)
                .negativeText(R.string.dismiss)
                .neutralText(R.string.review_link)
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(url));
                        startActivity(intent);
                    }
                })
                .build();
    }

    @OnClick(R.id.text_view_favorite)
    public void onFavorite() {
        if (mMovieItem != null) {
            if (isFavorite()) {
                unFavoriteMovie();
                txtFavorite.setText(R.string.favorite);
                txtFavorite.setTextColor(ContextCompat.getColor(getActivity(), R.color.md_grey_900));
            } else {
                favoriteMovie();
                txtFavorite.setText(R.string.unfavorite);
                txtFavorite.setTextColor(ContextCompat.getColor(getActivity(), R.color.primary));
            }
        } else {
            Toast.makeText(getActivity(), getString(R.string.no_movie_available_to_fav), Toast.LENGTH_SHORT).show();
        }
    }

    public void favoriteMovie() {
        if (mMovieItem != null) {
            RealmDb.addFavoriteMovie(getFavoriteMovieItem(mMovieItem));
            Toast.makeText(getActivity(), "Added to favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public void unFavoriteMovie() {
        if (mMovieItem != null) {
            RealmDb.removeFavoriteMovie(getFavoriteMovieItem(mMovieItem));
            Toast.makeText(getActivity(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isFavorite() {
        if (mMovieItem != null) {
            return RealmDb.isMovieFavorite(mMovieItem);
        }
        return false;
    }

    private FavoriteMovieItem getFavoriteMovieItem(MovieItem movieItem) {
        FavoriteMovieItem favoriteMovieItem = new FavoriteMovieItem();
        favoriteMovieItem.setPage(movieItem.getPage());
        favoriteMovieItem.setBackdropPath(movieItem.getBackdropPath());
        favoriteMovieItem.setId(movieItem.getId());
        favoriteMovieItem.setOriginalLanguage(movieItem.getOriginalLanguage());
        favoriteMovieItem.setOriginalTitle(movieItem.getOriginalTitle());
        favoriteMovieItem.setOverview(movieItem.getOverview());
        favoriteMovieItem.setReleaseDate(movieItem.getReleaseDate());
        favoriteMovieItem.setPosterPath(movieItem.getPosterPath());
        favoriteMovieItem.setPopularity(movieItem.getPopularity());
        favoriteMovieItem.setTitle(movieItem.getTitle());
        favoriteMovieItem.setVideo(movieItem.isVideo());
        favoriteMovieItem.setVoteAverage(movieItem.getVoteAverage());
        favoriteMovieItem.setVoteCount(movieItem.getVoteCount());
        favoriteMovieItem.setType(movieItem.getType());
        return favoriteMovieItem;
    }
}
