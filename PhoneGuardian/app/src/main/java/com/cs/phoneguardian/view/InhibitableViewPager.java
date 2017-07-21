package com.cs.phoneguardian.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 可以禁止滑动的
 */
public class InhibitableViewPager extends ViewPager {
    private boolean ScrollEnable = true;

    public InhibitableViewPager(Context context) {
        super(context);
    }

    public InhibitableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent arg0) {
        if(ScrollEnable){
            return super.onTouchEvent(arg0);
        }else {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        if(ScrollEnable){
            return super.onInterceptTouchEvent(arg0);
        }else {
            return false;
        }
    }

    public boolean isScrollEnable() {
        return ScrollEnable;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.ScrollEnable = scrollEnable;
    }
}
