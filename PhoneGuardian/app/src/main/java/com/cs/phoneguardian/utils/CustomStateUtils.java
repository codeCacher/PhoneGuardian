package com.cs.phoneguardian.utils;

import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.provider.Settings;

import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 * 获取一些常见手机状态的工具类
 */

public class CustomStateUtils {
    /**
     * 判断手机中是否有usage access选项
     *
     * @param context 上下文
     * @return 是否有usage access选项
     */
    public static boolean hasUsageAccessOption(Context context) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }

    /**
     * 判断是否有usage access权限
     *
     * @param context 上下文
     * @return 是否有权限
     */
    public static boolean usageAccessOptionState(Context context) {
        if (!hasUsageAccessOption(context)) {
            return true;
        }
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }
}
