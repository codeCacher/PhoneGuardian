package com.cs.phoneguardian.modle;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import android.os.SystemClock;
import android.util.Log;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.accelerate.AppInfo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Observer;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by SEELE on 2017/7/11.
 */

public class AppInfoDataSource implements BaseDataSource {

    private Context mContext;
    private ActivityManager mActivityManager;
    private PackageManager mPackageManager;

    private AppInfoDataSource(Context context) {
        if (mActivityManager == null || mPackageManager == null) {
            this.mContext = context;
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mPackageManager = context.getPackageManager();
        }
    }

    public interface OnGetAppListFinishedListener{
        void onFinish(List<AppInfo> list);
    }

    public static AppInfoDataSource getInstance(Context context) {
        return new AppInfoDataSource(context);
    }

    public void getRunningAppList(Subscriber<List<AppInfo>> subscriber) {
        Observable.create(new Observable.OnSubscribe<List<AppInfo>>() {
            @Override
            public void call(Subscriber<? super List<AppInfo>> subscriber) {
                List<AppInfo> appInfoList = new ArrayList<>();
                HashMap<String, AppInfo> appInfoMap = new HashMap<>();
                //TODO android 5.0 之后无法获取正在运行的进程列表，故获取服务列表
//        List<ActivityManager.RunningAppProcessInfo> runningAppProcessesList = mActivityManager.getRunningAppProcesses();

                List<ActivityManager.RunningServiceInfo> runningAppProcessesList = mActivityManager.getRunningServices(Integer.MAX_VALUE);

                for (ActivityManager.RunningServiceInfo processInfo : runningAppProcessesList) {
                    AppInfo appInfo = new AppInfo();
                    //包名
                    appInfo.setPackageName(processInfo.process);

                    try {
                        //通过PackageManager获取应用名、图标、是否为系统应用
                        ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(appInfo.getPackageName(), 0);
                        appInfo.setName(applicationInfo.loadLabel(mPackageManager).toString());
                        appInfo.setIcon(applicationInfo.loadIcon(mPackageManager));
                        if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                            appInfo.setSystem(true);
                        } else {
                            appInfo.setSystem(false);
                        }

                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                        //系统应用
                        appInfo.setName(processInfo.process);
                        appInfo.setIcon(mContext.getResources().getDrawable(R.drawable.icon));
                        appInfo.setSystem(true);
                    }
                    if (appInfoMap.containsKey(appInfo.getName())) {
                        continue;
                    }

                    //占用内存大小
                    Debug.MemoryInfo[] processMemoryInfo = mActivityManager.getProcessMemoryInfo(new int[]{processInfo.pid});
                    Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
                    appInfo.setMemSize(memoryInfo.getTotalPrivateDirty() * 1024);

                    appInfoMap.put(appInfo.getName(), appInfo);
                    appInfoList.add(appInfo);
                }

                subscriber.onNext(appInfoList);

            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
