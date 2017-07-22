package com.cs.phoneguardian.applock;

import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 */

public class AppLockPresenter implements AppLockContract.Presenter {

    private final AppInfoDataSource mAppInfoDataSource;
    private final AppLockContract.View mAppLockView;
    private List<AppInfo> mInstalledAppList;
    private List<AppInfo> mAppLockAppList;

    private AppLockPresenter(AppInfoDataSource appInfoDataSource, AppLockContract.View appLockView) {
        this.mAppInfoDataSource = appInfoDataSource;
        this.mAppLockView = appLockView;

        mAppLockView.setPresenter(this);
    }

    static AppLockPresenter getInstance(AppInfoDataSource appInfoDataSource, AppLockContract.View appLockView) {
        return new AppLockPresenter(appInfoDataSource, appLockView);
    }

    @Override
    public void start() {
        mInstalledAppList = mAppInfoDataSource.getInstalledAppList();
        mAppLockAppList = mAppInfoDataSource.getAppLockAppList();
        //遍历设置应用锁状态
        for (AppInfo info:mAppLockAppList) {
            for(int i=0;i<mInstalledAppList.size();i++){
                if(mInstalledAppList.get(i).getPackageName().equals(info.getPackageName())){
                    mInstalledAppList.get(i).setAppLock(true);
                }
            }
        }
        mAppLockView.upDateAppList(mInstalledAppList);
        mAppLockView.showLockAppCount(mAppLockAppList.size());
    }

    @Override
    public void removeLockApp(AppInfo info) {
        mAppInfoDataSource.removeAppFromAppLockDB(info);
        Iterator<AppInfo> iterator = mAppLockAppList.iterator();
        while (iterator.hasNext()){
            AppInfo appInfo = iterator.next();
            if(appInfo.getPackageName().equals(info.getPackageName())){
                iterator.remove();
            }
        }
        mAppLockView.showLockAppCount(mAppLockAppList.size());
    }

    @Override
    public void addLockApp(AppInfo info) {
        mAppInfoDataSource.addAppToAppLockDB(info);
        mAppLockAppList.add(info);
        mAppLockView.showLockAppCount(mAppLockAppList.size());
    }
}
