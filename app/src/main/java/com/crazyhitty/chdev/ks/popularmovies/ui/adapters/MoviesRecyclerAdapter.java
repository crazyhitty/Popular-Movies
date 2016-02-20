package com.crazyhitty.chdev.ks.popularmovies.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.crazyhitty.chdev.ks.popularmovies.R;
import com.crazyhitty.chdev.ks.popularmovies.models.MovieItem;
import com.crazyhitty.chdev.ks.popularmovies.models.SettingPreferences;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Kartik_ch on 2/7/2016.
 */
public class MoviesRecyclerAdapter extends RecyclerView.Adapter<MoviesRecyclerAdapter.MovieViewHolder> {
    private List<MovieItem> mMovieItems;

    public MoviesRecyclerAdapter(List<MovieItem> movieItems) {
        this.mMovieItems = movieItems;
    }

    public List<MovieItem> getMovieItems() {
        return mMovieItems;
    }

    @Override
    public MoviesRecyclerAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_movie, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(final MoviesRecyclerAdapter.MovieViewHolder holder, int position) {
        holder.txtMovieTitle.setText(mMovieItems.get(position).getTitle());
        holder.txtMovieRank.setText("#" + (position + 1));
        Picasso.with(holder.itemView.getContext())
                .load(SettingPreferences.POSTER_THUMBNAIL_IMAGE_PATH + mMovieItems.get(position).getPosterPath())
                .placeholder(R.drawable.light_black_bg)
                .error(R.drawable.ic_error_48dp)
                .into(holder.imgPoster, new Callback() {
                    @Override
                    public void onSuccess() {
                        holder.imgPoster.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    }

                    @Override
                    public void onError() {
                        holder.imgPoster.setScaleType(ImageView.ScaleType.CENTER);
                    }
                });
    }

    @Override
    public int getItemCount() {
        if (mMovieItems == null) {
            return 0;
        }
        return mMovieItems.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.text_view_title)
        TextView txtMovieTitle;
        @Bind(R.id.text_view_rank)
        TextView txtMovieRank;
        @Bind(R.id.image_view_poster)
        ImageView imgPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
