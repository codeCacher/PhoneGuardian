package com.cs.phoneguardian.accelerate;

import android.content.Context;
import android.os.Handler;

import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.modle.PhoneStateDataSource;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;

/**
 * Created by SEELE on 2017/7/13.
 * 加速页面presenter
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

    static AccPresenter getInstance(AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, AccContract.View accView) {
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
                //区分系统应用和用户应用
                for (AppInfo appInfo : list) {
                    appInfo.setSeleced(true);
                    if (appInfo.isSystem()) {
                        runningSysAppList.add(appInfo);
                    } else {
                        runningUserAppList.add(appInfo);
                    }
                }
                //区分是否有加速锁
                int defaultSelectAppCount = runningUserAppList.size();
                mAccLockAppList = mAppInfoDataSource.getAccLockAppList();
                for (AppInfo lock : mAccLockAppList) {
                    for (int i = 0; i < runningUserAppList.size(); i++) {
                        if (lock.getPackageName().equals(runningUserAppList.get(i).getPackageName())) {
                            runningUserAppList.get(i).setLock(true);
                            runningUserAppList.get(i).setSeleced(false);
                            defaultSelectAppCount--;
                        }
                    }
                }

                //初始化底部按钮
                initBottomBtn(defaultSelectAppCount);
                //更新列表
                mAccView.upDateAppList(runningUserAppList, runningSysAppList);

                //初始化应用数目头
                mAccView.showCountTitle(list.size(), runningUserAppList.size(), runningSysAppList.size());
                mAccView.initCountTitle();

                //初始化进度条数据
                mTotleRAMSize = mPhoneStateDataSource.getTotleRAMSize();
                mUsedRAMSize = mPhoneStateDataSource.getUsedRAMSize();
                final int percent = (int) (1f * mUsedRAMSize / mTotleRAMSize * 100);
                //TODO 如何在布局显示之后启动动画
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAccView.showResumeAnimation(percent);
                        mAccView.showMemorySize(mUsedRAMSize, mTotleRAMSize);
                        mAccView.showMemoryPercent(percent);
                        mAccView.showState(percent);
                    }
                }, 1000);
            }
        });

    }

    private void initBottomBtn(int defaultSelectAppCount) {
        if (defaultSelectAppCount == runningUserAppList.size() && defaultSelectAppCount != 0) {
            mAccView.showSelectAllBtnEnable();
        } else {
            mAccView.showSelectAllBtnDisalbe();
        }

        if (defaultSelectAppCount == 0) {
            mAccView.showEndBtnDisable();
        } else {
            mAccView.showEndBtnEnable(defaultSelectAppCount);
        }
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
        if (runningUserAppList == null) {
            return;
        }
        int lockAppCount = 0;
        for (AppInfo info : runningUserAppList) {
            if (info.isLock()) {
                lockAppCount++;
            } else {
                info.setSeleced(true);
            }
        }
        mAccView.showSelectAllBtnEnable();
        mAccView.showEndBtnEnable(runningUserAppList.size() - lockAppCount);
        mAccView.upDateAppList(runningUserAppList, runningSysAppList);
    }

    @Override
    public void cacelSelectAll() {
        if (runningUserAppList == null) {
            return;
        }
        for (AppInfo info : runningUserAppList) {
            info.setSeleced(false);
        }
        mAccView.showSelectAllBtnDisalbe();
        mAccView.showEndBtnDisable();
        mAccView.upDateAppList(runningUserAppList, runningSysAppList);
    }

    @Override
    public void killSelectedProcess() {
        int appCount = 0;
        long totalDirtyMem = 0;
        if (runningUserAppList == null) {
            return;
        }
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

        mUsedRAMSize -= totalDirtyMem;
        int percent = (int) (mUsedRAMSize * 100 / mTotleRAMSize);
        mAccView.upDateAppList(runningUserAppList, runningSysAppList);
        mAccView.showMemoryPercent(percent);
        mAccView.showMemorySize(mUsedRAMSize, mTotleRAMSize);
        mAccView.showState(percent);

        mAccView.showEndBtnDisable();
        mAccView.showSelectAllBtnDisalbe();

        mAccView.showToastTotalClearMemory(appCount, totalDirtyMem);
        mAppInfoDataSource.killProcess(killProcessList);
    }

    @Override
    public void selectLockApp(Context context) {
        SelectLockAppActivity.startSelectLockAppActivity(context);
        mAccView.hideAppList();
    }

    @Override
    public void resume() {
        if (runningUserAppList == null) {
            return;
        }
        //区分是否有加速锁
        mAccLockAppList = mAppInfoDataSource.getAccLockAppList();

        for (int i = 0; i < runningUserAppList.size(); i++) {
            runningUserAppList.get(i).setLock(false);
            for (AppInfo lock : mAccLockAppList) {
                if (lock.getPackageName().equals(runningUserAppList.get(i).getPackageName())) {
                    runningUserAppList.get(i).setLock(true);
                }
            }
        }

        //更新列表
        mAccView.upDateAppList(runningUserAppList, runningSysAppList);
    }
}
