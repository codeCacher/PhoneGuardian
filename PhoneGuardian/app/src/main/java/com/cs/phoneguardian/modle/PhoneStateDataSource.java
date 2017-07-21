package com.cs.phoneguardian.modle;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;

import java.io.File;

/**
 * Created by SEELE on 2017/7/11.
 */

public class PhoneStateDataSource implements BaseDataSource {

    private Context mContext;
    private ActivityManager mActivityManager;
    private ActivityManager.MemoryInfo mMemoryInfo;

    private PhoneStateDataSource(Context context) {
        if(mActivityManager==null){
            this.mContext = context;
            mActivityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            mMemoryInfo = new ActivityManager.MemoryInfo();
            mActivityManager.getMemoryInfo(mMemoryInfo);
        }
    }

    public static PhoneStateDataSource getInstance(Context context){
        return new PhoneStateDataSource(context);
    }

    /**
     * 获取可用RAM大小
     * @return 可用RAM大小，单位Byte
     */
    public long getAvilableRAMSize(){
        return mMemoryInfo.availMem;
    }

    /**
     * 获取总RAM大小
     * @return 总RAM大小，单位Byte
     */
    public long getTotleRAMSize(){
        return mMemoryInfo.totalMem;
    }

    /**
     * 获取可用RAM大小
     * @return 可用RAM大小，单位Byte
     */
    public long getUsedRAMSize(){
        return mMemoryInfo.totalMem-mMemoryInfo.availMem;
    }

    /**
     * 获取可用手机存储空间大小
     * @return 可用手机存储空间大小，单位Byte
     */
    public long getAvilablePhoneMemSize(){
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return  availableBlocksLong * blockSizeLong;
    }

    /**
     * 获取总共手机存储空间大小
     * @return 总共手机存储空间大小，单位Byte
     */
    public long getTotlePhoneMemSize(){
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return blockCountLong * blockSizeLong;
    }

    /**
     * 获取已用手机存储空间大小
     * @return 已用手机存储空间大小，单位Byte
     */
    public long getUsedPhoneMemSize(){
        File file = Environment.getDataDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return (blockCountLong-availableBlocksLong)*blockSizeLong;
    }

    /**
     * 获取可用SD卡存储空间大小
     * @return 可用SD卡存储空间大小，单位Byte
     */
    public long getAvilableSDMemSize(){
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        return  availableBlocksLong * blockSizeLong;
    }

    /**
     * 获取总共SD卡存储空间大小
     * @return 总共SD卡存储空间大小，单位Byte
     */
    public long getTotleSDMemSize(){
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return blockCountLong * blockSizeLong;
    }

    /**
     * 获取已用SD卡存储空间大小
     * @return 已用SD卡存储空间大小，单位Byte
     */
    public long getUsedSDMemSize(){
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        long availableBlocksLong = statFs.getAvailableBlocksLong();
        long blockSizeLong = statFs.getBlockSizeLong();
        long blockCountLong = statFs.getBlockCountLong();
        return (blockCountLong-availableBlocksLong)*blockSizeLong;
    }


    /**
     * 判断SD卡是否可用
     * @return SD卡是否可用
     */
    public boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public String getSimSerialNumber(){
        TelephonyManager telephonyManager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getSimSerialNumber();
    }
}
