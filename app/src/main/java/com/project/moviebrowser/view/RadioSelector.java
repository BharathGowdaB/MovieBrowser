package com.project.moviebrowser.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import androidx.annotation.Nullable;

import com.project.moviebrowser.R;

public class RadioSelector extends androidx.appcompat.widget.AppCompatRadioButton {
    private int selectedColor, unSelectedColor;
    public RadioSelector(Context context) {
        super(context);
        init(null, 0);
    }

    public RadioSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RadioSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.RadioSelector, defStyle, 0);

        unSelectedColor = getCurrentTextColor();
        selectedColor = a.getColor(R.styleable.RadioSelector_checkedStateColor, unSelectedColor);

        setTextColor(isChecked() ? selectedColor : unSelectedColor);

        a.recycle();

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                setTextColor(b ? selectedColor : unSelectedColor);
            }
        });
    }

}
