package com.cs.phoneguardian.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ScrollViewChild extends NestedScrollView implements NestedScrollingChild {
    public ScrollViewChild(Context context) {
        super(context);
    }

    public ScrollViewChild(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollViewChild(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
