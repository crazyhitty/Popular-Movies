package com.crazyhitty.chdev.ks.popularmovies.ui.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import android.widget.LinearLayout;
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
import com.crazyhitty.chdev.ks.popularmovies.utils.GridItemSpacingDecorationUtil;
import com.crazyhitty.chdev.ks.popularmovies.utils.NetworkConnectionUtil;
import com.crazyhitty.chdev.ks.popularmovies.utils.RecyclerItemTouchListenerUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kartik_ch on 2/8/2016.
 */
public class MoviesFragment extends Fragment implements IMoviesView, SwipeRefreshLayout.OnRefreshListener, RecyclerItemTouchListenerUtil.OnItemClickListener {
    private static boolean isPortrait = true;
    @Bind(R.id.recycler_view_movies)
    RecyclerView recyclerViewMovies;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.linear_layout_movies_unavailable)
    LinearLayout linearLayoutMoviesUnavailable;
    private MoviesRecyclerAdapter mMoviesRecyclerAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private MoviesPresenter mMoviesPresenter;
    private RecyclerView.OnScrollListener mOnScrollListener;

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

        if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            if (SettingPreferences.SORT_BY_POPULARITY) {
                mMoviesPresenter.attemptMoviesLoadingByPopularity(getActivity(), false);
            } else if (SettingPreferences.SORT_BY_RATING) {
                mMoviesPresenter.attemptMoviesLoadingByUserRating(getActivity(), false);
            }
        } else {
            NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
            if (SettingPreferences.SORT_BY_POPULARITY) {
                mMoviesPresenter.attemptMoviesLoadingByPopularity(getActivity(), true);
            } else if (SettingPreferences.SORT_BY_RATING) {
                mMoviesPresenter.attemptMoviesLoadingByUserRating(getActivity(), true);
            }
        }

        //add swipe refresh layout listener
        swipeRefreshLayout.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        if (NetworkConnectionUtil.isNetworkAvailable(getActivity())) {
            if (SettingPreferences.SORT_BY_POPULARITY) {
                mMoviesPresenter.attemptMoviesLoadingByPopularity(getActivity(), false);
            } else if (SettingPreferences.SORT_BY_RATING) {
                mMoviesPresenter.attemptMoviesLoadingByUserRating(getActivity(), false);
            }
        } else {
            NetworkConnectionUtil.showNetworkUnavailableDialog(getActivity());
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    private void initRecyclerViewMovies() {
        initRecyclerViewScrollListener();
        recyclerViewMovies.addOnScrollListener(mOnScrollListener);
        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(null);
        recyclerViewMovies.setAdapter(mMoviesRecyclerAdapter);
        recyclerViewMovies.addItemDecoration(new GridItemSpacingDecorationUtil(getActivity(), R.dimen.item_offset));
        recyclerViewMovies.addOnItemTouchListener(new RecyclerItemTouchListenerUtil(getActivity(), this));

        supportMultipleScreenResolutions();
    }

    private void initRecyclerViewScrollListener() {
        mOnScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Log.e("Scroll Position", "x:" + dx + "," + "y:" + dy);
            }
        };
    }

    @Override
    public void moviesLoadedByPopularity(List<MovieItem> movieItems) {
        Log.e("Movies retrieved", "" + movieItems.size());
        swipeRefreshLayout.setRefreshing(false);
        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(movieItems);
        recyclerViewMovies.setAdapter(mMoviesRecyclerAdapter);
        if (movieItems.size() != 0) {
            linearLayoutMoviesUnavailable.setVisibility(View.INVISIBLE);
        } else {
            linearLayoutMoviesUnavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void moviesLoadedByUserRating(List<MovieItem> movieItems) {
        swipeRefreshLayout.setRefreshing(false);
        mMoviesRecyclerAdapter = new MoviesRecyclerAdapter(movieItems);
        recyclerViewMovies.setAdapter(mMoviesRecyclerAdapter);
        if (movieItems.size() != 0) {
            linearLayoutMoviesUnavailable.setVisibility(View.INVISIBLE);
        } else {
            linearLayoutMoviesUnavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void moviesLoadingFailed(String message) {
        swipeRefreshLayout.setRefreshing(false);
        //If user quickly presses back button while the data is still loading, application will crash as
        //Toast class won't be able to find current context.
        try {
            Toast.makeText(getActivity(), "Error:\n" + message, Toast.LENGTH_LONG).show();
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
        linearLayoutMoviesUnavailable.setVisibility(View.VISIBLE);
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
            SettingPreferences.saveSortingType(getActivity(), SettingPreferences.BY_POPULARITY);
            item.setChecked(true);
            swipeRefreshLayout.setRefreshing(true);
            mMoviesPresenter.attemptMoviesLoadingByPopularity(getActivity(), false);
            return true;
        }

        if (id == R.id.action_sort_by_rating) {
            SettingPreferences.saveSortingType(getActivity(), SettingPreferences.BY_RATING);
            item.setChecked(true);
            swipeRefreshLayout.setRefreshing(true);
            mMoviesPresenter.attemptMoviesLoadingByUserRating(getActivity(), false);
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

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.e("Orientation", "Landscape");
            isPortrait = false;
            supportMultipleScreenResolutions();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.e("Orientation", "Portrait");
            isPortrait = true;
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
        if (isPortrait) {
            mLayoutManager = new GridLayoutManager(getActivity(), 2);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        }
        recyclerViewMovies.setLayoutManager(mLayoutManager);
    }

    private void useTabletSmallLayout() {
        if (isPortrait) {
            mLayoutManager = new GridLayoutManager(getActivity(), 3);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
        }
        recyclerViewMovies.setLayoutManager(mLayoutManager);
    }

    private void useTabletBigLayout() {
        if (isPortrait) {
            mLayoutManager = new GridLayoutManager(getActivity(), 4);
        } else {
            mLayoutManager = new GridLayoutManager(getActivity(), 5);
        }
        recyclerViewMovies.setLayoutManager(mLayoutManager);
    }

    @Override
    public void onItemClick(View view, int position) {
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
}
