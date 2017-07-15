package com.cs.phoneguardian.accelerate;

import android.os.Handler;
import android.util.Log;

import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.modle.PhoneStateDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by SEELE on 2017/7/13.
 */

public class AccPresenter implements AccContract.Presenter {
    AppInfoDataSource mAppInfoDataSource;
    PhoneStateDataSource mPhoneStateDataSource;
    AccContract.View mAccView;

    private AccPresenter(AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, AccContract.View accView) {
        this.mAppInfoDataSource = appInfoDataSource;
        this.mPhoneStateDataSource = phoneStateDataSource;
        this.mAccView = accView;

        accView.setPresenter(this);
    }

    public static AccPresenter getInstance(AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, AccContract.View accView){
        return new AccPresenter(appInfoDataSource,phoneStateDataSource,accView);
    }

    @Override
    public void start() {
        mAppInfoDataSource.getRunningAppList(new Subscriber<List<AppInfo>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(List<AppInfo> list) {

                mAccView.initNestedScrollView();

                List<AppInfo> runningUserAppList = new ArrayList<>();
                List<AppInfo> runningSysAppList = new ArrayList<>();
                for (AppInfo appInfo : list) {
                    if(appInfo.isSystem()){
                        runningSysAppList.add(appInfo);
                    }else {
                        runningUserAppList.add(appInfo);
                    }
                }
                mAccView.upDateAppList(runningUserAppList,runningSysAppList);

                final long totleRAMSize = mPhoneStateDataSource.getTotleRAMSize();
                final long usedRAMSize = mPhoneStateDataSource.getUsedRAMSize();
                final int percent = (int) (1f*usedRAMSize/totleRAMSize*100);
                //TODO 如何在布局显示之后启动动画
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("haha", System.currentTimeMillis()+"");

                        mAccView.showResumeAnimation(percent);
                        mAccView.showMemorySize(usedRAMSize,totleRAMSize);
                        mAccView.showMemoryPercent(percent);
                        mAccView.showState(percent);
                    }
                },1000);

            }
        });

    }
}
