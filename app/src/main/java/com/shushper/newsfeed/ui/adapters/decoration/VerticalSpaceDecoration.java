package com.shushper.newsfeed.ui.adapters.decoration;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shushper.newsfeed.R;


public class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {

    private Context mContext;

    public VerticalSpaceDecoration(Context context) {
        mContext = context;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.right = (int) mContext.getResources().getDimension(R.dimen.grid_step);
    }
}
