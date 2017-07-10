package com.cs.phoneguardian.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.cs.phoneguardian.R;

/**
 * 仿iphone带进度的进度条，线程安全的View，可直接在线程中更新进度
 *
 * @author xiaanming
 *         http://blog.csdn.net/xiaanming/article/details/10298163
 */
public class RoundProgressBar extends View {
    /**
     * 画笔对象的引用
     */
    private Paint paint;

    /**
     * 圆环的颜色
     */
    private int roundColor;

    /**
     * 圆环进度的颜色
     */
    private int roundProgressColor;

    /**
     * 圆环的宽度
     */
    private float roundWidth;

    /**
     * 最大进度
     */
    private int max;

    /**
     * 当前进度
     */
    private int progress;

    /**
     * 进度的风格，实心或者空心
     */
    private int style;

    /**
     * 开始的角度
     */
    private int startAngle;
    /**
     * 当为间隔模式时，为间隔的数量
     */
    private int intervalCount;

    public static final int STROKE = 0;
    public static final int FILL = 1;
    public static final int INTERVAL = 2;

    private RectF mOval;


    public RoundProgressBar(Context context) {
        this(context, null);
    }

    public RoundProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        paint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.RoundProgressBar);

        //获取自定义属性和默认值
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.RED);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.GREEN);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        progress = mTypedArray.getInteger(R.styleable.RoundProgressBar_progress, 0);
        startAngle = mTypedArray.getInteger(R.styleable.RoundProgressBar_startAngle, 90);
        intervalCount = mTypedArray.getInteger(R.styleable.RoundProgressBar_intervalCount, 100);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, FILL);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        float centre = getWidth() / 2f; //获取圆心的x坐标
        float radius = (centre - roundWidth / 2f); //圆环的半径
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        switch (style) {
            case FILL:
            case STROKE:
                canvas.drawCircle(centre, centre, radius, paint); //画出圆环
                break;
            case INTERVAL:
                //用于定义的圆弧的形状和大小的界限
                mOval = new RectF(centre - radius, centre - radius, centre + radius, centre + radius);
                for (int i = 0; i < intervalCount; i++) {
                    canvas.drawArc(mOval, i * 360f / intervalCount, 180f / intervalCount, false, paint);
                }
                break;
        }

        /**
         * 画圆弧 ，画圆环的进度
         */

        //设置进度是实心还是空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centre - radius, centre - radius, centre
                + radius, centre + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval,  startAngle, 360 * progress / max, false, paint);  //根据进度画圆弧
                break;
            case FILL:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (progress != 0)
                    canvas.drawArc(oval, startAngle, 360 * progress / max, true, paint);  //根据进度画圆弧
                break;
            case INTERVAL:
                paint.setStyle(Paint.Style.STROKE);
                for (int i = 0; i < 1f * progress * intervalCount / max; i++) {
                    canvas.drawArc(mOval, i * 360f / intervalCount + startAngle, 180f / intervalCount, false, paint);
                }
                break;
        }

    }


    public synchronized int getMax() {
        return max;
    }

    /**
     * 设置进度的最大值
     *
     * @param max
     */
    public synchronized void setMax(int max) {
        if (max < 0) {
            throw new IllegalArgumentException("max not less than 0");
        }
        this.max = max;
    }

    /**
     * 获取进度.需要同步
     *
     * @return
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress
     */
    public synchronized void setProgress(int progress) {
        if (progress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (progress > max) {
            progress = max;
        }
        if (progress <= max) {
            this.progress = progress;
            postInvalidate();
        }

    }

    public int getRoundColor() {
        return roundColor;
    }

    public void setRoundColor(int roundColor) {
        this.roundColor = roundColor;
    }

    public int getRoundProgressColor() {
        return roundProgressColor;
    }

    public void setRoundProgressColor(int roundProgressColor) {
        this.roundProgressColor = roundProgressColor;
    }

    public float getRoundWidth() {
        return roundWidth;
    }

    public void setRoundWidth(float roundWidth) {
        this.roundWidth = roundWidth;
    }

    public int getStyle() {
        return style;
    }

    public void setStyle(int style) {
        this.style = style;
    }

    public int getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(int startAngle) {
        this.startAngle = startAngle;
    }

    public int getIntervalCount() {
        return intervalCount;
    }

    public void setIntervalCount(int intervalCount) {
        this.intervalCount = intervalCount;
    }
}
//    final int finalMaxProgress = maxProgress;
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//@Override
//public void call(Subscriber<? super Integer> subscriber) {
//        for (int i = 1; i <= finalMaxProgress; i++) {
//        SystemClock.sleep(20);
//        subscriber.onNext(i);
//        }
//        subscriber.onCompleted();
//        }
//        })
//        .subscribeOn(Schedulers.newThread())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Action1<Integer>() {
//@Override
//public void call(Integer i) {
//        rpbProgress.setProgress(i);
//        }
//        });