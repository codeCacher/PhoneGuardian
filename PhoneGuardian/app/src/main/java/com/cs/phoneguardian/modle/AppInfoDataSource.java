package com.cs.phoneguardian.modle;

import android.app.ActivityManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Debug;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.accelerate.AppInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private AppInfoDbHelper mAppInfoDbHelper;
    private SQLiteDatabase mAppDB;

    private AppInfoDataSource(Context context) {
        if (mActivityManager == null || mPackageManager == null) {
            this.mContext = context;
            mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            mPackageManager = context.getPackageManager();

            mAppInfoDbHelper = new AppInfoDbHelper(context);
            mAppDB = mAppInfoDbHelper.getWritableDatabase();
        }
    }

    public static AppInfoDataSource getInstance(Context context) {
        return new AppInfoDataSource(context);
    }

    /**
     * 获取正在运行的APP列表
     * @param subscriber 指定获取成功后的动作
     */
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
                    AppInfo appInfo = getAppInfoByPackageName(processInfo.process);

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

    /**
     * 获取所有的安装应用信息
     * @return AppInfo列表
     */
    public List<AppInfo> getInstalledAppList(){
        List<ApplicationInfo> installedApplications = mPackageManager.getInstalledApplications(0);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (ApplicationInfo info : installedApplications) {
            appInfoList.add(getAppInfoByPackageName(info.packageName));
        }
        return appInfoList;
    }

    /**
     * 获取所有的用户应用
     * @return 用户应用列表
     */
    public List<AppInfo> getUserAppList(){
        List<ApplicationInfo> installedApplications = mPackageManager.getInstalledApplications(0);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (ApplicationInfo info : installedApplications) {
            AppInfo appInfo = getAppInfoByPackageName(info.packageName);
            if(!appInfo.isSystem()){
                appInfoList.add(appInfo);
            }
        }
        return appInfoList;
    }

    /**
     * 获取所有的系统应用
     * @return 系统应用列表
     */
    public List<AppInfo> getSysAppList(){
        List<ApplicationInfo> installedApplications = mPackageManager.getInstalledApplications(0);
        List<AppInfo> appInfoList = new ArrayList<>();
        for (ApplicationInfo info : installedApplications) {
            AppInfo appInfo = getAppInfoByPackageName(info.packageName);
            if(appInfo.isSystem()){
                appInfoList.add(appInfo);
            }
        }
        return appInfoList;
    }

    /**
     * 通过包名获取App的名称，图标，是否为系统应用的信息，其他信息为默认初始值
     * @param packagename 包名
     * @return 应用信息
     */
    public AppInfo getAppInfoByPackageName(String packagename){
        AppInfo appInfo = new AppInfo();
        //包名
        appInfo.setPackageName(packagename);

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
            appInfo.setName(packagename);
            appInfo.setIcon(mContext.getResources().getDrawable(R.drawable.icon));
            appInfo.setSystem(true);
        }

        return appInfo;
    }

    /**
     * 获取加速锁定应用列表
     * @return 加速锁定应用列表
     */
    public List<AppInfo> getAccLockAppList(){
        List<AppInfo> list = new ArrayList<>();
        Cursor cursor = mAppDB.query(AppInfoPersistenceContract.AppEntry.ACC_LOCK_TABLE_NAME,
                new String[]{AppInfoPersistenceContract.AppEntry.PACKAGE_NAME}, null, null, null, null, null);
        if(cursor == null) {
            return list;
        }
        while(cursor.moveToNext()) {
            try {
                AppInfo appInfo = new AppInfo();
                ApplicationInfo applicationInfo = mPackageManager.getApplicationInfo(cursor.getString(0), 0);
                appInfo.setPackageName(cursor.getString(0));
                appInfo.setName(applicationInfo.loadLabel(mPackageManager).toString());
                appInfo.setIcon(applicationInfo.loadIcon(mPackageManager));
                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    appInfo.setSystem(true);
                } else {
                    appInfo.setSystem(false);
                }
                appInfo.setLock(true);
                appInfo.setSeleced(false);
                list.add(appInfo);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        cursor.close();
        return list;
    }

    /**
     * 结束列表中所有后台进程
     * @param list 进程列表
     */
    //TODO 由于系统原因，已经无法使用
    public void killProcess(List<AppInfo> list){
        for (AppInfo info : list) {
            mActivityManager.killBackgroundProcesses(info.getPackageName());
        }
    }

    /**
     * 向加速锁定数据库中加入一个App
     * @param info App信息对象
     * @return 插入的条目数
     */
    public long addAppToAccLockDB(AppInfo info){
        ContentValues contentValues = new ContentValues();
        contentValues.put(AppInfoPersistenceContract.AppEntry.PACKAGE_NAME,info.getPackageName());
        return mAppDB.insert(AppInfoPersistenceContract.AppEntry.ACC_LOCK_TABLE_NAME, null, contentValues);
    }

    /**
     * 从加速锁定数据库中移除一个App
     * @param info App信息对象
     * @return 删除的条目数
     */
    public int  removeAppFromAccLockDB(AppInfo info){
        return mAppDB.delete(AppInfoPersistenceContract.AppEntry.ACC_LOCK_TABLE_NAME,
                AppInfoPersistenceContract.AppEntry.PACKAGE_NAME + "= ?", new String[]{info.getPackageName()});
    }
}
