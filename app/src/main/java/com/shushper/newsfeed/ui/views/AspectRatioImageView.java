package com.shushper.newsfeed.ui.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.shushper.newsfeed.R;

/**
 * ImageView with permanent aspect ratio.
 */
public class AspectRatioImageView extends ImageView {

    private static final int WIDTH  = 0;
    private static final int HEIGHT = 1;

    private double mAspectRatio;
    private int    mFixedSide;


    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView, 0, 0);

        try {
            mAspectRatio = a.getFloat(R.styleable.AspectRatioImageView_aspectRatio, 1.78f);
            mFixedSide = a.getInt(R.styleable.AspectRatioImageView_fixedSide, WIDTH);
        } finally {
            a.recycle();
        }
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);

        switch (mFixedSide) {
            case HEIGHT:
                width = (int) (height * mAspectRatio);
                break;

            case WIDTH:
                height = (int) (width / mAspectRatio);
                break;
        }

        setMeasuredDimension(width, height);
    }
}
