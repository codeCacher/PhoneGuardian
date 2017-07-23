package com.cs.phoneguardian.applock;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.CustomStateUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/22.
 */

public class AppLockService extends Service {

    private AppInfoDataSource mAppInfoDataSource;
    private List<AppInfo> mAppLockAppList;
    private InnerReceiver mInnerReceiver;
    private Runnable mRrunnable;
    private Thread watchThread;

    private boolean mWatchFlag;
    private String mSkipPKGName;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppInfoDataSource = AppInfoDataSource.getInstance(getApplicationContext());
        mAppLockAppList = mAppInfoDataSource.getAppLockAppList();
        mWatchFlag = true;
        mSkipPKGName = "";


        //注册接收解锁成功的广播
        IntentFilter intentFilter = new IntentFilter(Constants.INTENT_FILT_APP_LOCK_SKIP);
        mInnerReceiver = new InnerReceiver();
        registerReceiver(mInnerReceiver, intentFilter);

        watch();
    }

    class InnerReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mSkipPKGName = intent.getStringExtra(Constants.KEY_PKG_NAME_EXTRA);
        }
    }

    private void watch() {
        mRrunnable = new Runnable() {
            @Override
            public void run() {
                while (mWatchFlag) {
                    SystemClock.sleep(500);
                    //获取顶部应用包名
                    String topTaskPackageName = CustomStateUtils.getTopTaskPackageName(getApplicationContext());
                    //如果顶部应用不是解锁应用,则遍历加锁应用列表判断是否加锁，并将解锁应用清空
                    if (!topTaskPackageName.equals(mSkipPKGName)) {
                        mSkipPKGName = "";
                        for (AppInfo info : mAppLockAppList) {
                            if (info.getPackageName().equals(topTaskPackageName)) {
                                Intent intent = new Intent(getApplicationContext(), LockActivity.class);
                                intent.putExtra(Constants.KEY_PKG_NAME_EXTRA, info.getPackageName());
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        }
                    }

                }
            }
        };
        watchThread = new Thread(mRrunnable);
        watchThread.start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mAppLockAppList = mAppInfoDataSource.getAppLockAppList();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        mWatchFlag = false;
        unregisterReceiver(mInnerReceiver);
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
