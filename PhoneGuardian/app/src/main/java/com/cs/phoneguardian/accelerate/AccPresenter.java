package com.cs.phoneguardian.accelerate;

import android.content.pm.ApplicationInfo;
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
    private List<AppInfo> runningUserAppList;
    private List<AppInfo> runningSysAppList;
    private long mTotleRAMSize;
    private long mUsedRAMSize;
    private List<AppInfo> mAccLockAppList;

    private AccPresenter(AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, AccContract.View accView) {
        this.mAppInfoDataSource = appInfoDataSource;
        this.mPhoneStateDataSource = phoneStateDataSource;
        this.mAccView = accView;

        accView.setPresenter(this);
    }

    public static AccPresenter getInstance(AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, AccContract.View accView) {
        return new AccPresenter(appInfoDataSource, phoneStateDataSource, accView);
    }

    @Override
    public void start() {
        mAppInfoDataSource.getRunningAppList(new Subscriber<List<AppInfo>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(List<AppInfo> list) {
                //初始化应用列表
                runningUserAppList = new ArrayList<>();
                runningSysAppList = new ArrayList<>();
                for (AppInfo appInfo : list) {
                    if (appInfo.isSystem()) {
                        runningSysAppList.add(appInfo);
                    } else {
                        runningUserAppList.add(appInfo);
                    }
                }
                mAccView.upDateAppList(runningUserAppList, runningSysAppList);

                //初始化应用数目头
                mAccView.showCountTitle(list.size(), runningUserAppList.size(), runningSysAppList.size());
                mAccView.initCountTitle();

                //初始化数据
                mTotleRAMSize = mPhoneStateDataSource.getTotleRAMSize();
                mUsedRAMSize = mPhoneStateDataSource.getUsedRAMSize();
                mAccLockAppList = mAppInfoDataSource.getAccLockAppList();
                final int percent = (int) (1f * mUsedRAMSize / mTotleRAMSize * 100);
                //TODO 如何在布局显示之后启动动画
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("haha", System.currentTimeMillis() + "");

                        mAccView.showResumeAnimation(percent);
                        mAccView.showMemorySize(mUsedRAMSize, mTotleRAMSize);
                        mAccView.showMemoryPercent(percent);
                        mAccView.showState(percent);
                    }
                }, 1000);
            }
        });

    }

    @Override
    public void setEndBtnState() {
        int selectedCount = 0;
        for (AppInfo info : runningUserAppList) {
            if (info.isSeleced()) {
                selectedCount++;
            }
        }
        if (selectedCount > 0) {
            mAccView.showEndBtnEnable(selectedCount);
        } else {
            mAccView.showEndBtnDisable();
        }
    }

    @Override
    public void selectAll() {
        int lockAppCount = 0;
        for (AppInfo info : runningUserAppList) {
            if(info.isLock()){
                lockAppCount++;
            }else {
                info.setSeleced(true);
            }
        }
        mAccView.showSelectAllBtnEnable();
        mAccView.showEndBtnEnable(runningUserAppList.size()-lockAppCount);
        mAccView.upDateAppList(runningUserAppList,runningSysAppList);
    }

    @Override
    public void cacelSelectAll() {
        for (AppInfo info : runningUserAppList) {
            info.setSeleced(false);
        }
        mAccView.showSelectAllBtnDisalbe();
        mAccView.showEndBtnDisable();
        mAccView.upDateAppList(runningUserAppList,runningSysAppList);
    }

    @Override
    public void killSelectedProcess() {
        int appCount = 0;
        long totalDirtyMem = 0;
        List<AppInfo> killProcessList = new ArrayList<>();
        for (AppInfo info : runningUserAppList) {
            if (info.isSeleced()) {
                appCount++;
                totalDirtyMem += info.getMemSize();
                killProcessList.add(info);
            }
        }
        for (AppInfo info : killProcessList) {
            runningUserAppList.remove(info);
        }

        mUsedRAMSize-=totalDirtyMem;
        int percent = (int) (mUsedRAMSize * 100 / mTotleRAMSize);
        mAccView.upDateAppList(runningUserAppList,runningSysAppList);
        mAccView.showMemoryPercent(percent);
        mAccView.showMemorySize(mUsedRAMSize,mTotleRAMSize);
        mAccView.showState(percent);

        mAccView.showEndBtnDisable();
        mAccView.showSelectAllBtnDisalbe();

        mAccView.showToastTotalClearMemory(appCount,totalDirtyMem);
        mAppInfoDataSource.killProcess(killProcessList);
    }
}
