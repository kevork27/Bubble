package com.bubblestudios.bubble;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

//custom viewpager that disables swiping and requires programmatic (button) page changes
public class TouchDisabledViewPager extends ViewPager {
    public TouchDisabledViewPager(@NonNull Context context) {
        super(context);
    }

    public TouchDisabledViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
