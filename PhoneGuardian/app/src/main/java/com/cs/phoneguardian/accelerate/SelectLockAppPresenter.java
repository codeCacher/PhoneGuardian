package com.cs.phoneguardian.accelerate;

import com.cs.phoneguardian.modle.AppInfoDataSource;

/**
 * Created by Administrator on 2017/7/18.
 */

class SelectLockAppPresenter implements AccContract.SettingPreseter {

    private final AppInfoDataSource mAppInfoDataSource;
    private final AccContract.SettingView mSettingView;

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

    }
}
