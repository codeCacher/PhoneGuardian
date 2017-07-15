package com.cs.phoneguardian.view;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Interpolator;
import android.graphics.Paint;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationSet;
import android.view.animation.BaseInterpolator;
import android.view.animation.RotateAnimation;

import com.cs.phoneguardian.R;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

import static android.view.animation.Animation.INFINITE;

/**
 * Created by SEELE on 2017/7/11.
 */

public class WaitDot extends View {

    private Paint mPaint;
    private int circleColor;
    private int dotColor;
    private float dotWidth;
    private int angle;
    private float circleThick;
    private boolean circleVisible;
    private float centre;
    private float radius;

    public WaitDot(Context context) {
        this(context, null);
    }

    public WaitDot(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WaitDot(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();


        TypedArray mTypedArray = context.obtainStyledAttributes(attrs,
                R.styleable.WaitDot);

        //获取自定义属性和默认值
        circleColor = mTypedArray.getColor(R.styleable.WaitDot_circleColor, Color.WHITE);
        dotColor = mTypedArray.getColor(R.styleable.WaitDot_dotColor, Color.WHITE);
        circleThick = mTypedArray.getDimension(R.styleable.WaitDot_circleThick, 1);
        dotWidth = mTypedArray.getDimension(R.styleable.WaitDot_dotWidth, 20);
        circleVisible = mTypedArray.getBoolean(R.styleable.WaitDot_circleVisible, true);
        angle = mTypedArray.getInteger(R.styleable.WaitDot_angle, 0);

        mTypedArray.recycle();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //获取圆心的x坐标
        centre = getWidth() / 2f;
        //圆环的半径
        radius = (centre - circleThick / 2f - dotWidth / 2f);

        if (circleVisible) {
            mPaint.setColor(circleColor); //设置圆环的颜色
            mPaint.setStyle(Paint.Style.STROKE); //设置空心
            mPaint.setStrokeWidth(circleThick); //设置圆环的宽度
            mPaint.setAntiAlias(true);  //消除锯齿
            canvas.drawCircle(centre, centre, radius, mPaint);
        }

        double x = radius - radius * Math.sin(angle/180f*Math.PI) + dotWidth / 2f;
        double y = radius + radius * Math.cos(angle/180f*Math.PI) + dotWidth / 2f;
        mPaint.setColor(dotColor); //设置圆点的颜色
        mPaint.setStyle(Paint.Style.FILL); //设置实心
        mPaint.setAntiAlias(true);  //消除锯齿
        canvas.drawCircle((float) x, (float) y, dotWidth / 2, mPaint);
    }

    public synchronized void setAngle(int angle) {
        if (angle < 0) {
            throw new IllegalArgumentException("angle should not less than 0");
        } else if (angle >= 360) {
            angle = angle % 360;
        }
        this.angle = angle;
        postInvalidate();
    }

    public void swip(final int endAngle, int duration){
        final int intervalTime = duration / endAngle;

        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                for (int i = 1; i <= endAngle; i++) {
                    SystemClock.sleep(intervalTime);
                    subscriber.onNext(i);
                }
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer i) {
                        WaitDot.this.setAngle(i);
                    }
                });
//        final int refreshTime = endAngle*100/360;
//        final int intervalTime = duration / refreshTime;
//        final float intervalAngle = 3.6f;
//
//        Observable.create(new Observable.OnSubscribe<Integer>() {
//            @Override
//            public void call(Subscriber<? super Integer> subscriber) {
//                for (int i = 1; i <= refreshTime; i++) {
//                    SystemClock.sleep(intervalTime);
//                    subscriber.onNext(i);
//                }
//                subscriber.onCompleted();
//            }
//        })
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<Integer>() {
//                    @Override
//                    public void call(Integer i) {
//                        WaitDot.this.setAngle((int) (i*intervalAngle));
//                    }
//                });
    }

    public void startRotate(int duaration){
        RotateAnimation rotateAnimation = new RotateAnimation(0,360,centre,centre);
        rotateAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        rotateAnimation.setRepeatCount(INFINITE);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setDuration(duaration);
        this.startAnimation(rotateAnimation);
    }

    public void stopRotate(){
        this.clearAnimation();
    }
}
