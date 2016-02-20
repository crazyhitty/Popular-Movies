package com.crazyhitty.chdev.ks.popularmovies.utils;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by https://gist.github.com/yqritc/ccca77dc42f2364777e1
 */
public class GridItemSpacingDecorationUtil extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public GridItemSpacingDecorationUtil(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public GridItemSpacingDecorationUtil(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
