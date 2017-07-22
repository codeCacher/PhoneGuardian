package com.cs.phoneguardian.applock;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.utils.CustomStateUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 */

public class AppLockService extends Service {

    private AppInfoDataSource mAppInfoDataSource;
    private List<AppInfo> mAppLockAppList;
    private Runnable mRrunnable;
    private Thread watchThread;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInfoDataSource = AppInfoDataSource.getInstance(getApplicationContext());
        mAppLockAppList = mAppInfoDataSource.getAppLockAppList();
        mRrunnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i >= 0; i++) {
                    SystemClock.sleep(1000);
                    String topTaskPackageName = CustomStateUtils.getTopTaskPackageName(getApplicationContext());
                    for (AppInfo info : mAppLockAppList) {
                        if (info.getPackageName().equals(topTaskPackageName)) {
                            Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                    }
                }
            }
        };
        watchThread = new Thread(mRrunnable);
        watchThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
