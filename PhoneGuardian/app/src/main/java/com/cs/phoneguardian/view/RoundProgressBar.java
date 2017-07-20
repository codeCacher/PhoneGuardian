package com.cs.phoneguardian.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;

import com.cs.phoneguardian.R;

import javax.annotation.CheckForNull;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 圆形进度条
 */
public class RoundProgressBar extends View {
    /**
     * 画笔
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
     * 进度的风格
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

    /**
     * 间隔模式时是否有进度指示点
     */
    private boolean progressDotVisible;

    /**
     * 进度指示点的颜色
     */
    private int progressDotColor;

    /**
     * 进度指示点宽度
     */
    private float progressDotWidth;

    /**
     * 进度条外圆半径
     */
    private float radiusOut;

    /**
     * 旋转标志，用于中断旋转
     */
    private boolean mTurn;

    /**
     * swip标志，用于中断swip
     */
    private boolean mSwip;

    /**
     * 第二层进度
     */
    private int secondProgress;

    /**
     * 第二层进度颜色
     */
    private int secondRoundProgressColor;

    /**
     * 第二层进度点颜色
     */
    private int secondProgressDotColor;

    /**
     * 第二层进度点是否可见
     */
    private boolean secondProgressDotVisible;

    public static final int STROKE = 0;
    public static final int FILL = 1;
    public static final int INTERVAL = 2;

    private RectF mOval;
    private Observable<Integer> mObservable;
    private Observable<Integer> mSecondObserver;
    private Subscriber<Integer> mSubscriber;
    private Subscriber<Integer> mSecondSubscriber;

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
        roundColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundColor, Color.WHITE);
        roundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_roundProgressColor, Color.WHITE);
        roundWidth = mTypedArray.getDimension(R.styleable.RoundProgressBar_roundWidth, 5);
        radiusOut = mTypedArray.getDimension(R.styleable.RoundProgressBar_radius, 100);
        max = mTypedArray.getInteger(R.styleable.RoundProgressBar_max, 100);
        progress = mTypedArray.getInteger(R.styleable.RoundProgressBar_progress, 0);
        startAngle = mTypedArray.getInteger(R.styleable.RoundProgressBar_startAngle, 90);
        intervalCount = mTypedArray.getInteger(R.styleable.RoundProgressBar_intervalCount, 100);
        style = mTypedArray.getInt(R.styleable.RoundProgressBar_style, FILL);
        progressDotVisible = mTypedArray.getBoolean(R.styleable.RoundProgressBar_progressDotVisible, false);
        progressDotColor = mTypedArray.getColor(R.styleable.RoundProgressBar_progressDotColor, Color.WHITE);
        progressDotWidth = mTypedArray.getFloat(R.styleable.RoundProgressBar_progressDotWidth, 20);

        secondRoundProgressColor = mTypedArray.getColor(R.styleable.RoundProgressBar_secondRoundProgressColor, Color.WHITE);
        secondProgress = mTypedArray.getInteger(R.styleable.RoundProgressBar_secondProgress, 0);
        secondProgressDotColor = mTypedArray.getColor(R.styleable.RoundProgressBar_secondProgressDotColor, Color.WHITE);
        secondProgressDotVisible = mTypedArray.getBoolean(R.styleable.RoundProgressBar_secondProgressDotVisible, false);

        mTypedArray.recycle();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         * 画最外层的大圆环
         */
        float centreX = getWidth() / 2f; //获取圆心的x坐标
        float centreY = getHeight() / 2f; //获取圆心的y坐标
        float radius = radiusOut - roundWidth / 2f;
        paint.setColor(roundColor); //设置圆环的颜色
        paint.setStyle(Paint.Style.STROKE); //设置空心
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setAntiAlias(true);  //消除锯齿
        switch (style) {
            case FILL:
            case STROKE:
                canvas.drawCircle(centreX, centreY, radius, paint); //画出圆环
                break;
            case INTERVAL:
                //用于定义的圆弧的形状和大小的界限
                mOval = new RectF(centreX - radius, centreY - radius, centreX + radius, centreY + radius);
                for (int i = 0; i < intervalCount; i++) {
                    canvas.drawArc(mOval, i * 360f / intervalCount, 180f / intervalCount, false, paint);
                }
                break;
        }

        /**
         * 画圆弧 ，画圆环的进度
         */
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(roundProgressColor);  //设置进度的颜色
        RectF oval = new RectF(centreX - radius, centreY - radius, centreX
                + radius, centreY + radius);  //用于定义的圆弧的形状和大小的界限

        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, startAngle, 360 * progress / max, false, paint);  //根据进度画圆弧
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
                    //画进度点
                    if (progressDotVisible && i == 1f * progress * intervalCount / max - 1) {
                        float a = i * 360f / intervalCount + 90f / intervalCount;
                        float r = radius - roundWidth / 2f - progressDotWidth;

                        double x = centreX - r * Math.sin(a / 180f * Math.PI);
                        double y = centreY + r * Math.cos(a / 180f * Math.PI);
                        paint.setColor(progressDotColor); //设置圆点的颜色
                        paint.setStyle(Paint.Style.FILL); //设置实心
                        paint.setAntiAlias(true);  //消除锯齿
                        canvas.drawCircle((float) x, (float) y, progressDotWidth / 2, paint);
                    }
                }

                break;
        }

        /**
         * 画圆弧 ，画第二层圆环的进度
         */
        paint.setStrokeWidth(roundWidth); //设置圆环的宽度
        paint.setColor(secondRoundProgressColor);  //设置进度的颜色
        switch (style) {
            case STROKE:
                paint.setStyle(Paint.Style.STROKE);
                canvas.drawArc(oval, startAngle, 360 * secondProgress / max, false, paint);  //根据进度画圆弧
                break;
            case FILL:
                paint.setStyle(Paint.Style.FILL_AND_STROKE);
                if (secondProgress != 0)
                    canvas.drawArc(oval, startAngle, 360 * secondProgress / max, true, paint);  //根据进度画圆弧
                break;
            case INTERVAL:
                paint.setStyle(Paint.Style.STROKE);
                for (int i = 0; i < 1f * secondProgress * intervalCount / max; i++) {
                    canvas.drawArc(mOval, i * 360f / intervalCount + startAngle, 180f / intervalCount, false, paint);
                    //画进度点
                    if (secondProgressDotVisible && i == 1f * secondProgress * intervalCount / max - 1) {
                        float a = i * 360f / intervalCount + 90f / intervalCount;
                        float r = radius - roundWidth / 2f - progressDotWidth;

                        double x = centreX - r * Math.sin(a / 180f * Math.PI);
                        double y = centreY + r * Math.cos(a / 180f * Math.PI);
                        paint.setColor(secondProgressDotColor); //设置圆点的颜色
                        paint.setStyle(Paint.Style.FILL); //设置实心
                        paint.setAntiAlias(true);  //消除锯齿
                        canvas.drawCircle((float) x, (float) y, progressDotWidth / 2, paint);
                    }
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
     * @param max 进度最大值
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
     * @return 当前进度
     */
    public synchronized int getProgress() {
        return progress;
    }

    /**
     * 设置进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param progress 设置的进度
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

    /**
     * 获取第二层进度.需要同步
     *
     * @return 当前进度
     */
    public synchronized int getSecondProgress() {
        return secondProgress;
    }

    /**
     * 设置第二层进度，此为线程安全控件，由于考虑多线的问题，需要同步
     * 刷新界面调用postInvalidate()能在非UI线程刷新
     *
     * @param secondProgress 设置的进度
     */
    public synchronized void setSecondProgress(int secondProgress) {
        if (secondProgress < 0) {
            throw new IllegalArgumentException("progress not less than 0");
        }
        if (secondProgress > max) {
            secondProgress = max;
        }
        if (secondProgress <= max) {
            this.secondProgress = progress;
            postInvalidate();
        }

    }

    public interface OnProgressChangeListener {
        void OnProgressChange(int progress);
    }

    /**
     * 从开始的百分比扫到结束百分比动画
     *
     * @param startPercent 起始的百分比
     * @param endPercent   结束的百分比
     * @param duration     时间
     * @param listener     进度监听
     */
    public void swip(final int index, final int startPercent, final int endPercent, int duration, final OnProgressChangeListener listener) {
        stopAnimation();
        mSwip = true;
        final int swipPercent = Math.max(Math.abs(endPercent - startPercent), 1);
        final int interval = duration / swipPercent;
        Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                if (endPercent >= startPercent) {
                    for (int i = startPercent; i <= endPercent; i++) {
                        SystemClock.sleep(interval);
                        subscriber.onNext(i);
                    }
                } else {
                    for (int i = startPercent; i >= endPercent; i--) {
                        SystemClock.sleep(interval);
                        subscriber.onNext(i);
                    }
                }
                subscriber.onCompleted();
            }
        });
        Subscriber<Integer> subscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer integer) {
                if(!mSwip){
                    return;
                }
                switch (index) {
                    case 0:
                        RoundProgressBar.this.setProgress(integer);
                        break;
                    case 1:
                        RoundProgressBar.this.setSecondProgress(integer);
                        break;
                }
                if (listener != null) {
                    listener.OnProgressChange(integer);
                }
            }
        };
        //增加一个引用，防止垃圾回收
        switch (index) {
            case 0:
                mObservable = observable;
                mSubscriber = subscriber;
                break;

            case 1:
                mSecondObserver = observable;
                mSecondSubscriber = subscriber;
                break;
        }
        observable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    /**
     * 进度点旋转
     * @param duration 转一圈的时间
     * @param dotColor 进度点颜色
     */
    public void turn(int duration, int dotColor) {
        stopAnimation();
        mTurn = true;
        final int interval = duration / 100;
        progressDotColor = dotColor;
        roundProgressColor = roundColor;
        mObservable = Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                while (mTurn) {
                    for (int i = 0; i <= 100; i++) {
                        SystemClock.sleep(interval);
                        subscriber.onNext(i);
                    }
                }
                subscriber.onCompleted();
            }
        });

        mSubscriber = new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(Integer integer) {
                if (mTurn) {
                    RoundProgressBar.this.setProgress(integer);
                }
            }
        };

        mObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mSubscriber);
    }

    /**
     * 停止动画
     */
    public void stopAnimation(){
        mTurn = false;
        mSwip =false;
        if(mSubscriber!=null){
            mSubscriber.unsubscribe();
        }
        if(mSecondSubscriber!=null){
            mSecondSubscriber.unsubscribe();
        }
    }

    /**
     * 初始化，停止旋转，停止swip，并将两个进度归零
     */
    public void init(){
        stopAnimation();
        RoundProgressBar.this.setProgressDotVisible(false);
        RoundProgressBar.this.setSecondProgressDotVisible(false);
        RoundProgressBar.this.setProgress(0);
        RoundProgressBar.this.setSecondProgress(0);
    }

    public void setProgressDotColor(int progressDotColor) {
        this.progressDotColor = progressDotColor;
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

    public boolean isProgressDotVisible() {
        return progressDotVisible;
    }

    public void setProgressDotVisible(boolean progressDotVisible) {
        this.progressDotVisible = progressDotVisible;
    }

    public int getProgressDotColor() {
        return progressDotColor;
    }

    public float getProgressDotWidth() {
        return progressDotWidth;
    }

    public void setProgressDotWidth(float progressDotWidth) {
        this.progressDotWidth = progressDotWidth;
    }

    public int getSecondRoundProgressColor() {
        return secondRoundProgressColor;
    }

    public int getSecondProgressDotColor() {
        return secondProgressDotColor;
    }

    public boolean isSecondProgressDotVisible() {
        return secondProgressDotVisible;
    }

    public void setSecondRoundProgressColor(int secondRoundProgressColor) {
        this.secondRoundProgressColor = secondRoundProgressColor;
    }

    public void setSecondProgressDotColor(int secondProgressDotColor) {
        this.secondProgressDotColor = secondProgressDotColor;
    }

    public void setSecondProgressDotVisible(boolean secondProgressDotVisible) {
        this.secondProgressDotVisible = secondProgressDotVisible;
    }
}
