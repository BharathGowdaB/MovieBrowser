package com.project.moviebrowser.services;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.webkit.WebView;

public class AspectRatioWebView extends WebView {

    private static final double ASPECT_RATIO = 16.0 / 9.0;

    public AspectRatioWebView(Context context) {
        super(context);
    }

    public AspectRatioWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AspectRatioWebView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) (width / ASPECT_RATIO);

        // Get the height of the device screen
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenHeight = displayMetrics.heightPixels;

        // If the calculated height is greater than the screen height, use the screen height instead
        if (height > screenHeight) {
            height = screenHeight;
        }

        int newHeightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, newHeightMeasureSpec);
    }
}

