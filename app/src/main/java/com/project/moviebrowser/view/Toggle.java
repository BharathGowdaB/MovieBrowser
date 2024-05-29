package com.project.moviebrowser.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.Nullable;

import com.project.moviebrowser.R;

/**
 * TODO: document your custom view class.
 */
public class Toggle extends androidx.appcompat.widget.AppCompatCheckBox {
    private int viewId;

    public Toggle(Context context) {
        super(context);
        init(context, null, 0);
    }

    public Toggle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public Toggle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs, defStyle);
    }

    private void init(Context context, AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.Toggle, defStyle, 0);

        if (a.hasValue(R.styleable.Toggle_viewId)) {
            viewId = a.getResourceId(R.styleable.Toggle_viewId, -1);

        }


        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                View toggleView = ((Activity) context).findViewById(viewId);
                if(toggleView != null)
                    toggleView.setVisibility(toggleView.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);

            }
        });

        a.recycle();


    }


}