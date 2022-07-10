package com.project.moviebrowser.utils;

import android.view.View;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.gauravk.bubblenavigation.BubbleNavigationLinearView;

public class BottomBarBehavior extends CoordinatorLayout.Behavior<BubbleNavigationLinearView> {

    private int height;

    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, BubbleNavigationLinearView child, int layoutDirection) {
        height = child.getHeight();
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(CoordinatorLayout coordinatorLayout,
                                       BubbleNavigationLinearView child, View directTargetChild,
                                       View target, int nestedScrollAxes) {
        return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(CoordinatorLayout coordinatorLayout,
                               BubbleNavigationLinearView child, View target,
                               int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        if (dyConsumed > 0) {
            slideDown(child);
        } else if (dyConsumed < 0) {
            slideUp(child);
        }
    }

    private void slideUp(BubbleNavigationLinearView child) {
        child.clearAnimation();
        child.animate().translationY(0).setDuration(200);

    }

    private void slideDown(BubbleNavigationLinearView child) {
        child.clearAnimation();
        child.animate().translationY(height).setDuration(200);

    }

}