package com.cs.phoneguardian.utils;

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
     * @param context 上下文
     * @param packageName 包名
     */
    public static void startAppInfoActivity(Context context,String packageName){
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package",packageName, null));
        context.startActivity(intent);
    }
}
