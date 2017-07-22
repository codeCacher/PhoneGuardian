package com.cs.phoneguardian.clearcache;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;

import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.modle.PhoneStateDataSource;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ClearCachePresenter implements ClearCacheContract.Presenter {

    private AppInfoDataSource mAppInfoDataSource;
    private PhoneStateDataSource mPhoneStateDataSource;
    private ClearCacheContract.View mClearCacheView;
    private Context mContext;
    private List<AppInfo> mInstalledAppList;
    private List<AppInfo> mHaveCacheAppList;
    private int mPhonePercent;
    private int mSdPercent;
    private long mAllPhoneCacheSize;
    private long totlePhoneMemSize;
    private long usedPhoneMemSize;
    private long totleSDMemSize;
    private long usedSDMemSize;
    private boolean mStartScan;
    private int mOldPhonePercent;
    private int mOldSDPercent;

    private ClearCachePresenter(Context context, AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, ClearCacheContract.View clearCacheView) {
        this.mContext = context;
        this.mAppInfoDataSource = appInfoDataSource;
        this.mPhoneStateDataSource = phoneStateDataSource;
        this.mClearCacheView = clearCacheView;

        mClearCacheView.setPresenter(this);
    }

    static ClearCachePresenter getInstance(Context context, AppInfoDataSource appInfoDataSource, PhoneStateDataSource phoneStateDataSource, ClearCacheContract.View clearCacheView) {
        return new ClearCachePresenter(context, appInfoDataSource, phoneStateDataSource, clearCacheView);
    }

    @Override
    public void start() {
        //初始化界面
        mStartScan = false;
        mClearCacheView.initStartState();

        //初始化数据
        totlePhoneMemSize = mPhoneStateDataSource.getTotlePhoneMemSize();
        usedPhoneMemSize = mPhoneStateDataSource.getUsedPhoneMemSize();
        totleSDMemSize = mPhoneStateDataSource.getTotleSDMemSize();
        usedSDMemSize = mPhoneStateDataSource.getUsedSDMemSize();

        mClearCacheView.showPhoneMemSize(usedPhoneMemSize, totlePhoneMemSize);
        mClearCacheView.showSDMemsize(usedSDMemSize, totleSDMemSize);

        mPhonePercent = (int) (1f * usedPhoneMemSize / totlePhoneMemSize * 100);
        mSdPercent = (int) (1f * usedSDMemSize / totleSDMemSize * 100);
        mClearCacheView.initRoundProgressColor(0, mPhonePercent, mSdPercent);
        //TODO 如何在布局显示之后启动动画
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mClearCacheView.showStartAnimation(mPhonePercent, mSdPercent);
            }
        }, 500);
    }

    @Override
    public void startScan(final Activity activity, final OnScanCompleteListener listener) {
        mStartScan = true;
        //初始化扫描界面
        mClearCacheView.updateAppList(new ArrayList<AppInfo>());
        mClearCacheView.initScanState();
        //开始扫描
        new Thread(new Runnable() {
            @Override
            public void run() {
                //获取应用列表
                mInstalledAppList = mAppInfoDataSource.getInstalledAppList();
                //扫描应用列表，并更新扫描状态
                for (int i = 0; i < mInstalledAppList.size(); i++) {
                    if (!mStartScan) {
                        return;
                    }
                    final AppInfo info = mInstalledAppList.get(i);
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mClearCacheView.updateScanState(info.getPackageName());
                        }
                    });
                    mAppInfoDataSource.getCacheSize(info);
                    SystemClock.sleep(10);
                }
                //计算总缓存大小
                mAllPhoneCacheSize = 0;
                mHaveCacheAppList = new ArrayList<>();
                for (AppInfo info : mInstalledAppList) {
                    if (!mStartScan) {
                        return;
                    }
                    if (info.getCacheSize() > 0) {
                        mAllPhoneCacheSize += info.getCacheSize();
                        mHaveCacheAppList.add(info);
                    }
                }
                //根据获取的数据更新界面
                mStartScan = false;
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.OnComplete();
                    }
                });
            }
        }).start();

    }

    @Override
    public void completeScan() {
        mOldPhonePercent = this.mPhonePercent;
        mOldSDPercent = this.mSdPercent;
        mPhonePercent = (int) (1f * (usedPhoneMemSize - mAllPhoneCacheSize) / totlePhoneMemSize * 100);
        mSdPercent = (int) (1f * usedSDMemSize / totleSDMemSize * 100);

        if (mPhonePercent == mOldPhonePercent) {
            mOldPhonePercent++;
        }
        mClearCacheView.showScanFinishState(mAllPhoneCacheSize, mOldPhonePercent, mPhonePercent, mOldSDPercent, mSdPercent);
        mClearCacheView.updateAppList(mHaveCacheAppList);
    }

    @Override
    public void clean(final Activity activity) {
        mAppInfoDataSource.clearAllInternalCache(new AppInfoDataSource.OnClearCacheCompleteListener() {
            @Override
            public void OnComplete() {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mClearCacheView.showFinishClean(mAllPhoneCacheSize,mOldPhonePercent,mPhonePercent,mOldSDPercent,mSdPercent);
                        mHaveCacheAppList.clear();
                        mClearCacheView.updateAppList(mHaveCacheAppList);
                    }
                });
            }
        });
    }
}
