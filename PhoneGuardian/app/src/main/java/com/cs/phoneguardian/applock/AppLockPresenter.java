package com.cs.phoneguardian.applock;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.CustomActivityJumpUtils;
import com.cs.phoneguardian.utils.CustomStateUtils;
import com.cs.phoneguardian.utils.DialogUtils;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import java.util.Iterator;
import java.util.List;

import static com.cs.phoneguardian.applock.AppLockActivity.REQUEST_CODE_USAGE_ACCESS;

/**
 * Created by Administrator on 2017/7/22.
 */

public class AppLockPresenter implements AppLockContract.Presenter {

    private final AppInfoDataSource mAppInfoDataSource;
    private final AppLockContract.View mAppLockView;
    private final Context mContext;
    private List<AppInfo> mInstalledAppList;
    private List<AppInfo> mAppLockAppList;
    private String psd;

    private AppLockPresenter(Context context,AppInfoDataSource appInfoDataSource, AppLockContract.View appLockView) {
        this.mContext = context;
        this.mAppInfoDataSource = appInfoDataSource;
        this.mAppLockView = appLockView;

        mAppLockView.setPresenter(this);
    }

    static AppLockPresenter getInstance(Context context, AppInfoDataSource appInfoDataSource, AppLockContract.View appLockView) {
        return new AppLockPresenter(context,appInfoDataSource, appLockView);
    }

    @Override
    public void start() {
        //检测密码
        psd = SharedPreferencesUtils.getString(mContext, Constants.KEY_APP_LOCK_PASSWORD, "");
        if(TextUtils.isEmpty(psd)){
            mAppLockView.showPassWordSettingActivity();
        }else {
            mAppLockView.showPassWordDialog(psd);
        }

        //获取应用列表
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

    @Override
    public void openAppLock(final Activity activity) {
        SharedPreferencesUtils.putBoolean(mContext,Constants.KEY_ENABLE_APP_LOCK,true);
        boolean usageAccessOptionState = CustomStateUtils.usageAccessOptionState(mContext);
        if(!usageAccessOptionState){
            DialogUtils.showConfirmDialog(mContext, "应用锁需要应用使用访问权限，是否前往打开？", new DialogUtils.OnButtonClickedListener() {
                @Override
                public void OnConfirm() {
                    CustomActivityJumpUtils.startUsageAccessActivityForResult(activity,REQUEST_CODE_USAGE_ACCESS);
                }

                @Override
                public void OnCancel() {
                    Toast.makeText(mContext,"没有打开权限会导致应用锁不可用，可在主界面设置中打开",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onSetUsageAccessResult() {
        boolean usageAccessOptionState = CustomStateUtils.usageAccessOptionState(mContext);
        if(!usageAccessOptionState){
            Toast.makeText(mContext,"没有打开权限会导致应用锁不可用，可在主界面设置中打开",Toast.LENGTH_SHORT).show();
        }
    }
}
