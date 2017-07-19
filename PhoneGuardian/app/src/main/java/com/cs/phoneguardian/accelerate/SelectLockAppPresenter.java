package com.cs.phoneguardian.accelerate;

import com.cs.phoneguardian.modle.AppInfoDataSource;

import java.util.List;

/**
 * Created by Administrator on 2017/7/18.
 */

class SelectLockAppPresenter implements AccContract.SettingPreseter {

    private final AppInfoDataSource mAppInfoDataSource;
    private final AccContract.SettingView mSettingView;
    private List<AppInfo> mUserAppList;
    private List<AppInfo> mAccLockAppList;
    private int mLockedAppCount;

    private SelectLockAppPresenter(AppInfoDataSource appInfoDataSource, AccContract.SettingView settingView) {
        this.mAppInfoDataSource = appInfoDataSource;
        this.mSettingView = settingView;

        mSettingView.setPresenter(this);
    }

    static SelectLockAppPresenter getInstance(AppInfoDataSource appInfoDataSource, AccContract.SettingView settingView) {
        return new SelectLockAppPresenter(appInfoDataSource, settingView);
    }

    @Override
    public void start() {
        mUserAppList = mAppInfoDataSource.getUserAppList();
        mAccLockAppList = mAppInfoDataSource.getAccLockAppList();
        //区分是否有加速锁
        for (AppInfo lock : mAccLockAppList) {
            for (int i = 0; i < mUserAppList.size(); i++) {
                if (lock.getPackageName().equals(mUserAppList.get(i).getPackageName())) {
                    mUserAppList.get(i).setLock(true);
                }
            }
        }
        mSettingView.upDateAppList(mUserAppList);

        mLockedAppCount = mAccLockAppList.size();
        mSettingView.showLockAppCount(mLockedAppCount);

    }

    @Override
    public void addLockApp(AppInfo info) {
        mAppInfoDataSource.addAppToAccLockDB(info);
        mLockedAppCount++;
        mSettingView.showLockAppCount(mLockedAppCount);
    }

    @Override
    public void removeLockApp(AppInfo info) {
        mAppInfoDataSource.removeAppFromAccLockDB(info);
        mLockedAppCount--;
        mSettingView.showLockAppCount(mLockedAppCount);
    }

    @Override
    public void selectAll() {
        for (AppInfo info : mUserAppList) {
            if (!info.isLock()) {
                addLockApp(info);
            }
            info.setLock(true);
        }
        mSettingView.upDateAppList(mUserAppList);
        mSettingView.showLockAppCount(mUserAppList.size());
    }

    @Override
    public void cancelAll() {
        for (AppInfo info : mUserAppList) {
            if (info.isLock()) {
                removeLockApp(info);
            }
            info.setLock(false);
        }
        mSettingView.upDateAppList(mUserAppList);
        mSettingView.showLockAppCount(0);
    }
}
