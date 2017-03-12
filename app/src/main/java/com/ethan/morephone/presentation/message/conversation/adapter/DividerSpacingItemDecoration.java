package com.ethan.morephone.presentation.message.conversation.adapter;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Ethan on 12/20/16.
 */

public class DividerSpacingItemDecoration extends RecyclerView.ItemDecoration {
    private int mItemOffset;

    public DividerSpacingItemDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public DividerSpacingItemDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(0, mItemOffset, 0, mItemOffset);
    }
}
