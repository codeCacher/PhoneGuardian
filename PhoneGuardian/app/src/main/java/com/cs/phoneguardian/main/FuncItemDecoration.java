package com.cs.phoneguardian.main;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cs.phoneguardian.R;

/**
 * Created by Administrator on 2017/7/10.
 * 主页面功能列表分割线
 */

public class FuncItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint mDividerPaint;
    private int mDecorationSize = 1;

    public FuncItemDecoration(Context context, int mDecorationSize) {
        this.mDecorationSize = mDecorationSize;
        mDividerPaint = new Paint();
        mDividerPaint.setColor(context.getResources().getColor(R.color.colorGrayBlue));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        int spanIndex = layoutParams.getSpanIndex();
        if(spanIndex<3){
            outRect.bottom = mDecorationSize;
        }
        if(spanIndex!=2&&spanIndex!=5){
            outRect.right = mDecorationSize;
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();

        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if(i<3){
                int left = view.getLeft();
                int top = view.getBottom();
                int right = view.getRight();
                int bottom = top + mDecorationSize;

                c.drawRect(left, top, right, bottom, mDividerPaint);
            }
            if(i!=2&&i!=5){
                int left = view.getRight();
                int top = view.getTop();
                int right = left+mDecorationSize;
                int bottom = view.getBottom() + mDecorationSize;

                c.drawRect(left, top, right, bottom, mDividerPaint);
            }

        }
    }
}
