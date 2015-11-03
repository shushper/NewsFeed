package com.shushper.newsfeed.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.Image;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * ImageView with permanent aspect ratio.
 */
public class NewsImage extends ImageView {

    private static final double ASPECT_RATIO = 1.78; //16/9

    public NewsImage(Context context) {
        super(context);
    }

    public NewsImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NewsImage(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public NewsImage(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int)(width / ASPECT_RATIO);

        setMeasuredDimension(width, height);
    }
}
