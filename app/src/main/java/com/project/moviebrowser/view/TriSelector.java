package com.project.moviebrowser.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.project.moviebrowser.R;

public class TriSelector extends androidx.appcompat.widget.AppCompatRadioButton {
    private int[] colors = new int[3];
    private Drawable[] drawables = new Drawable[3];
    private volatile int currentSelected;
    public TriSelector(Context context) {
        super(context);
        init(null, 0);
    }

    public TriSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public TriSelector(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes

        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.TriSelector, defStyle, 0);

        colors[0] = a.getColor(R.styleable.TriSelector_InitialColor, -1);
        colors[1] = a.getColor(R.styleable.TriSelector_ActivatedColor, -1);
        colors[2] = a.getColor(R.styleable.TriSelector_SelectedColor, -1);

        drawables[0] = a.getDrawable(R.styleable.TriSelector_Initial);
        drawables[1] = a.getDrawable(R.styleable.TriSelector_Activated);
        drawables[2] = a.getDrawable(R.styleable.TriSelector_Selected);

        a.recycle();

        setState(currentSelected);

        setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!b){
                    currentSelected = 0;
                    setState(currentSelected);
                }
            }
        });
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                currentSelected = ++currentSelected % 3;
                setState(currentSelected);
            }
        });
    }

    private void setState(int state){
        setTextColor(colors[state]);
        setBackground(drawables[state]);

        if(state == 2) {
            setChecked(true);
            setActivated(false);
            setSelected(true);
        } else if(state == 1){
            setChecked(true);
            setActivated(true);
            setSelected(false);
        } else {
            setChecked(false);
            setActivated(false);
            setSelected(false);
        }
    }

    public int getCurrentSelected(){
        System.out.println(currentSelected);
        return currentSelected;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}