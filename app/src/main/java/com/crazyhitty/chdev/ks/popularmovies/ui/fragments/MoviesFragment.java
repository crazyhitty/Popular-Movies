package com.crazyhitty.chdev.ks.popularmovies.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;
import com.crazyhitty.chdev.ks.popularmovies.movies.IMoviesView;
import com.crazyhitty.chdev.ks.popularmovies.movies.MoviesPresenter;
import com.crazyhitty.chdev.ks.popularmovies.ui.activities.MovieDetailsActivity;
import com.crazyhitty.chdev.ks.popularmovies.ui.activities.SettingsActivity;
import com.crazyhitty.chdev.ks.popularmovies.ui.adapters.MoviesRecyclerAdapter;
import com.crazyhitty.chdev.ks.popularmovies.utils.DimensionUtil;
import com.crazyhitty.chdev.ks.popularmovies.utils.GridItemSpacingDecorationUtil;
import com.crazyhitty.chdev.ks.popularmovies.utils.NetworkConnectionUtil;
import com.crazyhitty.chdev.ks.popularmovies.utils.RecyclerItemTouchListenerUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhanghai.android.materialprogressbar.MaterialProgressBar;

/**
 * Created by Kartik_ch on 2/8/2016.
 */
public class MoviesFragment extends Fragment implements IMoviesView, SwipeRefreshLayout.OnRefreshListener, RecyclerItemTouchListenerUtil.OnItemClickListener {
    private static final String MOVIES_WIDTH_SMALL = "small";
    private static final String MOVIES_WIDTH_AVG = "average";
    private static final String MOVIES_WIDTH_LARGE = "large";
    private static final String MOVIES_WIDTH_FULL = "full";

    private static final String ARG_TWO_PANE = "two_pane_mode";
    private static boolean IS_PORTRAIT = true;

    @Bind(R.id.relative_layout_movies)
    RelativeLayout relativeLayoutMovies;
    @Bind(R.id.recycler_view_movies)
    RecyclerView recyclerViewMovies;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.linear_layout_movies_unavailable)
    LinearLayout linearLayoutMoviesUnavailable;
    @Bind(R.id.button_load_more)
    Button btnLoadMore;
    @Bind(R.id.progress_loading)
    MaterialProgressBar progressLoading;

    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;
    private GridLayoutManager mLayoutManager;
    private MoviesPresenter mMoviesPresenter;

    private int mPageNumberPopular = 1;
    private int mPageNumberRating = 1;
    private boolean mTwoPaneMode = false;
    private boolean mLoadMore = false;

    private List<MovieItem> mMovieItems;

    public static MoviesFragment newInstance(List<MovieItem> movieItems, boolean isTwoPaneMode) {
        Bundle args = new Bundle();
        args.putBoolean(ARG_TWO_PANE, isTwoPaneMode);
        MoviesFragment fragment = new MoviesFragment();
        fragment.setArguments(args);
        fragment.setMovieItems(movieItems);
        return fragment;
    }

    public List<MovieItem> getMovieItems() {
        return mMovieItems;
    }

    public void setMovieItems(List<MovieItem> movieItems) {
        this.mMovieItems = movieItems;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.fragment_movies, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //initialize Recycler view with its adapter
        initRecyclerViewMovies();

        if (mMoviesPresenter == null) {
            mMoviesPresenter = new MoviesPresenter(this);
        }

        if (mMovieItems != null) {
            mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(mMovieItems);
            recyclerViewMovies.setAdapter(mMoviesRecyclerAdapter);
        } else if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            if (SettingPreferences.SORT_BY_POPULARITY) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                mMoviesPresenter.attemptMoviesLoadingByPopularity(mPageNumberPopular, false);
            } else if (SettingPreferences.SORT_BY_RATING) {
                swipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(true);
                    }
                });
                mMoviesPresenter.attemptMoviesLoadingByUserRating(mPageNumberRating, false);
            } else if (SettingPreferences.SORT_BY_FAVORITES) {
                swipeRefreshLayout.setRefreshing(true);
                mMoviesPresenter.attemptMoviesLoadingByFavorites();
            }
        } else {
            NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
            if (SettingPreferences.SORT_BY_POPULARITY) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
                mMoviesPresenter.attemptMoviesLoadingByPopularity(mPageNumberPopular, true);
            } else if (SettingPreferences.SORT_BY_RATING) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 500);
                mMoviesPresenter.attemptMoviesLoadingByUserRating(mPageNumberRating, true);
            } else if (SettingPreferences.SORT_BY_FAVORITES) {
                swipeRefreshLayout.setRefreshing(true);
                mMoviesPresenter.attemptMoviesLoadingByFavorites();
            }
        }

        //add swipe refresh layout listener
        swipeRefreshLayout.setOnRefreshListener(this);

        //check if two pane mode is enabled
        if (getArguments().getBoolean(ARG_TWO_PANE, false)) {
            mTwoPaneMode = true;
            IS_PORTRAIT = false;
        } else {
            mTwoPaneMode = false;
            IS_PORTRAIT = true;
        }

        Log.e("Two pane layout", "" + getArguments().getBoolean(ARG_TWO_PANE, false));
        supportMultipleScreenResolutions();
    }

    @Override
    public void onRefresh() {
        if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            if (SettingPreferences.SORT_BY_POPULARITY) {
                mPageNumberPopular = 1;
                mMoviesPresenter.attemptMoviesLoadingByPopularity(mPageNumberPopular, false);
            } else if (SettingPreferences.SORT_BY_RATING) {
                mPageNumberRating = 1;
                mMoviesPresenter.attemptMoviesLoadingByUserRating(mPageNumberRating, false);
            } else if (SettingPreferences.SORT_BY_FAVORITES) {
                mMoviesPresenter.attemptMoviesLoadingByFavorites();
            }
        } else if (SettingPreferences.SORT_BY_FAVORITES) {
            mMoviesPresenter.attemptMoviesLoadingByFavorites();
        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(false);
                }
            }, 500);
            NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
        }
    }

    private void initRecyclerViewMovies() {
        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(null);
        recyclerViewMovies.setAdapter(mMoviesRecyclerAdapter);
        recyclerViewMovies.addItemDecoration(new GridItemSpacingDecorationUtil(getActivity(), R.dimen.item_offset));
        recyclerViewMovies.addOnItemTouchListener(new RecyclerItemTouchListenerUtil(getActivity(), this));
        recyclerViewMovies.setNestedScrollingEnabled(false);
    }

    @OnClick(R.id.button_load_more)
    public void onLoadMore() {
        Toast.makeText(getActivity(), "Loading, please wait", Toast.LENGTH_SHORT).show();

        if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            mLoadMore = true;
            if (SettingPreferences.SORT_BY_POPULARITY) {
                mPageNumberPopular++;
                mMoviesPresenter.attemptMoviesLoadingByPopularity(mPageNumberPopular, false);
            } else if (SettingPreferences.SORT_BY_RATING) {
                mPageNumberRating++;
                mMoviesPresenter.attemptMoviesLoadingByUserRating(mPageNumberRating, false);
            }

            btnLoadMore.setEnabled(false);
            btnLoadMore.setVisibility(View.INVISIBLE);
            progressLoading.setVisibility(View.VISIBLE);
        } else {
            NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void moviesLoadedByPopularity(List<MovieItem> movieItems) {
        displayMovies(movieItems);
    }

    @Override
    public void moviesLoadedByUserRating(List<MovieItem> movieItems) {
        displayMovies(movieItems);
    }

    @Override
    public void moviesLoadedByFavorites(List<MovieItem> movieItems) {
        displayMovies(movieItems);
    }

    private void displayMovies(List<MovieItem> movieItems) {
        Log.e("Movies retrieved", "" + movieItems.size());
        swipeRefreshLayout.setRefreshing(false);

        if (!mLoadMore) {
            mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(movieItems);
            recyclerViewMovies.setAdapter(mMoviesRecyclerAdapter);
            if (movieItems.size() != 0) {
                linearLayoutMoviesUnavailable.setVisibility(View.INVISIBLE);
            } else {
                linearLayoutMoviesUnavailable.setVisibility(View.VISIBLE);
            }
            setMovieItems(movieItems);
        } else {
            if (movieItems.size() != 0) {
                mMoviesRecyclerAdapter.addMovieItems(movieItems);
            } else {
                Toast.makeText(getActivity(), "No movies loaded for this page, please try again", Toast.LENGTH_SHORT).show();
            }
            mLoadMore = false;
        }

        if (!SettingPreferences.SORT_BY_FAVORITES) {
            btnLoadMore.setVisibility(View.VISIBLE);
            btnLoadMore.setEnabled(true);
            progressLoading.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void moviesLoadingFailed(String message) {
        swipeRefreshLayout.setRefreshing(false);
        //If user quickly presses back button while the data is still loading, application will crash as
        //Toast class won't be able to find current context.
        if (getActivity() != null) {
            Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_LONG).show();
        }

        if (mLoadMore) {
            if (mPageNumberPopular != 1) {
                mPageNumberPopular--;
            }
            if (mPageNumberRating != 1) {
                mPageNumberRating--;
            }

            btnLoadMore.setVisibility(View.VISIBLE);
            btnLoadMore.setEnabled(true);
            progressLoading.setVisibility(View.INVISIBLE);
            if (SettingPreferences.SORT_BY_FAVORITES) {
                btnLoadMore.setVisibility(View.GONE);
            }
        } else if (mMoviesRecyclerAdapter.getItemCount() == 0) {
            linearLayoutMoviesUnavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movies, menu);

        MenuItem displayTypePhone = menu.findItem(R.id.action_phone);
        MenuItem displayType7InchTab = menu.findItem(R.id.action_7_inch_tablet);
        MenuItem displayType10InchTab = menu.findItem(R.id.action_10_inch_tablet);
        MenuItem sortByPopularity = menu.findItem(R.id.action_sort_by_popularity);
        MenuItem sortByRating = menu.findItem(R.id.action_sort_by_rating);
        MenuItem sortByFavorites = menu.findItem(R.id.action_sort_by_favorites);

        if (SettingPreferences.DISPLAY_TYPE_PHONE) {
            displayTypePhone.setChecked(true);
        } else if (SettingPreferences.DISPLAY_TYPE_7_INCH_TAB) {
            displayType7InchTab.setChecked(true);
        } else if (SettingPreferences.DISPLAY_TYPE_10_INCH_TAB) {
            displayType10InchTab.setChecked(true);
        }

        if (SettingPreferences.SORT_BY_POPULARITY) {
            sortByPopularity.setChecked(true);
        } else if (SettingPreferences.SORT_BY_RATING) {
            sortByRating.setChecked(true);
        } else if (SettingPreferences.SORT_BY_FAVORITES) {
            sortByFavorites.setChecked(true);
        }
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_phone) {
            SettingPreferences.saveDisplayType(getActivity(), SettingPreferences.PHONE);
            item.setChecked(true);
            usePhoneLayout();
            return true;
        }

        if (id == R.id.action_7_inch_tablet) {
            SettingPreferences.saveDisplayType(getActivity(), SettingPreferences.TAB_7_INCH);
            item.setChecked(true);
            useTabletSmallLayout();
            return true;
        }

        if (id == R.id.action_10_inch_tablet) {
            SettingPreferences.saveDisplayType(getActivity(), SettingPreferences.TAB_10_INCH);
            item.setChecked(true);
            useTabletBigLayout();
            return true;
        }

        if (id == R.id.action_sort_by_popularity) {
            item.setChecked(true);
            SettingPreferences.saveSortingType(getActivity(), SettingPreferences.BY_POPULARITY);
            mPageNumberPopular = 1;
            if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
                swipeRefreshLayout.setRefreshing(true);
                mMoviesPresenter.attemptMoviesLoadingByPopularity(mPageNumberPopular, false);
            } else {
                NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
                mMoviesPresenter.attemptMoviesLoadingByPopularity(mPageNumberPopular, true);
            }
            btnLoadMore.setVisibility(View.VISIBLE);

            loadEmptyMoviesDetailsFragment();
            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            item.setChecked(true);
            SettingPreferences.saveSortingType(getActivity(), SettingPreferences.BY_RATING);
            mPageNumberRating = 1;
            if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
                swipeRefreshLayout.setRefreshing(true);
                mMoviesPresenter.attemptMoviesLoadingByUserRating(mPageNumberRating, false);
            } else {
                NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
                mMoviesPresenter.attemptMoviesLoadingByUserRating(mPageNumberRating, true);
            }
            btnLoadMore.setVisibility(View.VISIBLE);

            loadEmptyMoviesDetailsFragment();
            return true;
        }

        if (id == R.id.action_sort_by_favorites) {
            item.setChecked(true);
            SettingPreferences.saveSortingType(getActivity(), SettingPreferences.BY_FAVORITES);
            mMoviesPresenter.attemptMoviesLoadingByFavorites();
            btnLoadMore.setVisibility(View.GONE);

            loadEmptyMoviesDetailsFragment();
            return true;
        }

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadEmptyMoviesDetailsFragment() {
        if (mTwoPaneMode) {
            //Set empty movie details fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame_movie_details,
                    MovieDetailsFragment.newInstance(null, true),
                    MovieDetailsFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        Log.e("NewConfig", newConfig.toString());
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("Orientation", "Landscape");
            IS_PORTRAIT = false;
            supportMultipleScreenResolutions();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("Orientation", "Portrait");
            IS_PORTRAIT = true;
            supportMultipleScreenResolutions();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        SettingPreferences.init(getActivity());
    }

    private void supportMultipleScreenResolutions() {
        if (SettingPreferences.DISPLAY_TYPE_PHONE) {
            usePhoneLayout();
        } else if (SettingPreferences.DISPLAY_TYPE_7_INCH_TAB) {
            useTabletSmallLayout();
        } else if (SettingPreferences.DISPLAY_TYPE_10_INCH_TAB) {
            useTabletBigLayout();
        } else {
            usePhoneLayout();
        }
    }

    private void usePhoneLayout() {
        if (IS_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_size));
        } else {
            if (mTwoPaneMode) {
                mLayoutManager = new GridLayoutManager(getActivity(), 1);
                setMoviesFrameWidth(MOVIES_WIDTH_SMALL);
            } else {
                mLayoutManager = new GridLayoutManager(getActivity(), getResources().getInteger(R.integer.grid_size));
                setMoviesFrameWidth(MOVIES_WIDTH_FULL);
            }
        }
        recyclerViewMovies.setLayoutManager(mLayoutManager);
    }

    private void useTabletSmallLayout() {
        if (IS_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            if (mTwoPaneMode) {
                mLayoutManager = new GridLayoutManager(getActivity(), 2);
                setMoviesFrameWidth(MOVIES_WIDTH_AVG);
            } else {
                mLayoutManager = new GridLayoutManager(getActivity(), 4);
                setMoviesFrameWidth(MOVIES_WIDTH_FULL);
            }
        }
        recyclerViewMovies.setLayoutManager(mLayoutManager);
    }

    private void useTabletBigLayout() {
        if (IS_PORTRAIT) {
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
        } else {
            if (mTwoPaneMode) {
                mLayoutManager = new GridLayoutManager(getActivity(), 3);
                setMoviesFrameWidth(MOVIES_WIDTH_LARGE);
            } else {
                mLayoutManager = new GridLayoutManager(getActivity(), 5);
                setMoviesFrameWidth(MOVIES_WIDTH_FULL);
            }
        }
        recyclerViewMovies.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onItemClick(View view, int position) {
        if (mTwoPaneMode) {
            //Set movie details fragment
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);
            fragmentTransaction.replace(R.id.frame_movie_details,
                    MovieDetailsFragment.newInstance(mMoviesRecyclerAdapter.getMovieItems().get(position), true),
                    MovieDetailsFragment.class.getSimpleName());
            fragmentTransaction.commit();
        } else {
            MovieItem movieItem = mMoviesRecyclerAdapter.getMovieItems().get(position);

            Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
            intent.putExtra(MovieItem.PAGE_KEY, movieItem.getPage());
            intent.putExtra(MovieItem.ADULT_KEY, movieItem.isAdult());
            intent.putExtra(MovieItem.BACKDROP_PATH_KEY, movieItem.getBackdropPath());
            intent.putExtra(MovieItem.ID_KEY, movieItem.getId());
            intent.putExtra(MovieItem.ORIGINAL_LANGUAGE_KEY, movieItem.getOriginalLanguage());
            intent.putExtra(MovieItem.ORIGINAL_TITLE_KEY, movieItem.getOriginalTitle());
            intent.putExtra(MovieItem.OVERVIEW_KEY, movieItem.getOverview());
            intent.putExtra(MovieItem.RELEASE_DATE_KEY, movieItem.getReleaseDate());
            intent.putExtra(MovieItem.POSTER_PATH_KEY, movieItem.getPosterPath());
            intent.putExtra(MovieItem.POPULARITY_KEY, movieItem.getPopularity());
            intent.putExtra(MovieItem.TITLE_KEY, movieItem.getTitle());
            intent.putExtra(MovieItem.VIDEO_KEY, movieItem.isVideo());
            intent.putExtra(MovieItem.VOTE_AVERAGE_KEY, movieItem.getVoteAverage());
            intent.putExtra(MovieItem.VOTE_COUNT_KEY, movieItem.getVoteCount());
            intent.putExtra(MovieItem.TYPE_KEY, movieItem.getType());

            startActivity(intent);
        }
    }

    private void showAboutDialog() {
        MaterialDialog aboutDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.action_about)
                .content(R.string.about_desc)
                .items(R.array.libraries_used)
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View itemView, int which, CharSequence text) {
                        String selectedLibraryLink = getResources().getStringArray(R.array.libraries_used_links)[which];
                        Intent webIntent = new Intent(Intent.ACTION_VIEW);
                        webIntent.setData(Uri.parse(selectedLibraryLink));
                        startActivity(webIntent);
                    }
                })
                .build();
        aboutDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void setMoviesFrameWidth(String type) {
        FrameLayout.LayoutParams layoutParams = null;
        switch (type) {
            case MOVIES_WIDTH_SMALL:
                layoutParams = new FrameLayout.LayoutParams(DimensionUtil.dpToPx(200), ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case MOVIES_WIDTH_AVG:
                layoutParams = new FrameLayout.LayoutParams(DimensionUtil.dpToPx(400), ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case MOVIES_WIDTH_LARGE:
                layoutParams = new FrameLayout.LayoutParams(DimensionUtil.dpToPx(600), ViewGroup.LayoutParams.MATCH_PARENT);
                break;
            case MOVIES_WIDTH_FULL:
                layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                break;
        }
        relativeLayoutMovies.setLayoutParams(layoutParams);
    }
}
