package com.cs.phoneguardian.view;

import android.content.Context;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.OverScroller;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.utils.DensityUtil;
import com.cs.phoneguardian.utils.DisplayUtils;


public class NestScrollLayout extends LinearLayout implements NestedScrollingParent {
    private View mChild;
    private View mMask;
    private View mTopView;

    private OverScroller mScroller;

    private int mMinTopHeight;
    private int mDefaultTopHeight;
    private int mBottomHeight;

    private OnScrollListener mOnScrollListener;

    public NestScrollLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOrientation(VERTICAL);

        mScroller = new OverScroller(context);

        //设置默认值
        mMinTopHeight = DensityUtil.dp2px(context, 50);
        mDefaultTopHeight = DensityUtil.dp2px(context, 350);
        mBottomHeight = DensityUtil.dp2px(context, 50);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        mScroller.abortAnimation();
        return true;
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        boolean hiddenTop = dy > 0 && getScrollY() < mDefaultTopHeight - mMinTopHeight;
        boolean showTop = dy < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);

        if (hiddenTop || showTop) {
            scrollBy(0, dy);
            consumed[1] = dy;
        }
    }

    @Override
    public void onStopNestedScroll(View child) {
        int scrollY = getScrollY();
        if (scrollY < mDefaultTopHeight / 2) {
            fling(scrollY, -2000);
        } else {
            fling(scrollY, 2000);
        }
    }

    @Override
    public boolean onNestedPreFling(final View target, float velocityX, float velocityY) {
        boolean hiddenTop = velocityY > 0 && getScrollY() < mDefaultTopHeight - mMinTopHeight;
        boolean showTop = velocityY < 0 && getScrollY() > 0 && !ViewCompat.canScrollVertically(target, -1);
        return hiddenTop || showTop;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        mChild = findViewById(R.id.child_id);
        mMask = findViewById(R.id.mask_id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        DisplayUtils displayUtils = DisplayUtils.getInstance(getContext());
        int statusBarHeight = displayUtils.getStatusBarHeight();
        int displayHeight = displayUtils.getDisplayHeight();
        ViewGroup.LayoutParams layoutParams = mChild.getLayoutParams();
        layoutParams.height = displayHeight - statusBarHeight - mMinTopHeight - mBottomHeight;
        mChild.setLayoutParams(layoutParams);
        setMeasuredDimension(getMeasuredWidth(), mDefaultTopHeight + mChild.getLayoutParams().height);
    }

    @Override
    public void scrollTo(int x, int y) {
        if (y < 0) {
            y = 0;
        }
        if (y > mDefaultTopHeight - mMinTopHeight) {
            y = mDefaultTopHeight - mMinTopHeight;
        }

        //滚动Top
        if (mTopView != null) {
            mTopView.layout(mTopView.getLeft(), -y / 2, mTopView.getRight(), mDefaultTopHeight- y / 2);
        }

        //设置mask阴影
        mMask.setAlpha(1f * y / mDefaultTopHeight);

        if (mOnScrollListener != null) {
            mOnScrollListener.OnScroll(y);
        }

        super.scrollTo(x, y);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(0, mScroller.getCurrY());
            postInvalidate();
        }
    }


    private void fling(int scrollY, int velocityY) {
        mScroller.fling(0, scrollY, 0, velocityY, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        invalidate();
    }

    public interface OnScrollListener {
        void OnScroll(int y);
    }

    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }

    public void init(int minTopHeight, int defaultTopHeight,int bottomHeight, View topView) {
        this.mMinTopHeight = minTopHeight;
        this.mDefaultTopHeight = defaultTopHeight;
        this.mBottomHeight = bottomHeight;
        this.mTopView = topView;
    }
}
