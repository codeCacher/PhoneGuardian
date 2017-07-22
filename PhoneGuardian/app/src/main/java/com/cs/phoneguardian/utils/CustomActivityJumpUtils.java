package com.cs.phoneguardian.utils;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;

/**
 * Created by Administrator on 2017/7/19.
 */

public class CustomActivityJumpUtils {
    /**
     * 开启系统应用信息界面
     *
     * @param context     上下文
     * @param packageName 包名
     */
    public static void startAppInfoActivity(Context context, String packageName) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", packageName, null));
        context.startActivity(intent);
    }

    /**
     * 开启设备管理器界面
     *
     * @param context 上下文
     * @param CN      组建名对象
     */
    public static void startDevicePolicyActivity(Context context, ComponentName CN) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, CN);
        context.startActivity(intent);
    }

    /**
     * 开启设备管理器界面
     *
     * @param activity 启动activity
     * @param CN       组建名对象
     */
    public static void startDevicePolicyActivityForResult(Activity activity, int requestCode, ComponentName CN) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, CN);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 开启设备管理器界面
     *
     * @param activity 启动activity
     */
    public static void startUsageAccessActivityForResult(Activity activity, int requestCode) {
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        activity.startActivityForResult(intent, requestCode);
    }
}
