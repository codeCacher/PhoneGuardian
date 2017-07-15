package com.cs.phoneguardian.modle;

import android.app.ActivityManager;
import android.content.Context;

/**
 * Created by SEELE on 2017/7/11.
 */

public class PhoneStateDataSource implements BaseDataSource {

    private ActivityManager mActivityManager;
    private ActivityManager.MemoryInfo mMemoryInfo;

    private PhoneStateDataSource(Context context) {
        if(mActivityManager==null){
            mActivityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            mMemoryInfo = new ActivityManager.MemoryInfo();
            mActivityManager.getMemoryInfo(mMemoryInfo);
        }
    }

    public static PhoneStateDataSource getInstance(Context context){
        return new PhoneStateDataSource(context);
    }

    public long getAvilableRAMSize(){
        return mMemoryInfo.availMem;
    }

    public long getTotleRAMSize(){
        return mMemoryInfo.totalMem;
    }

    public long getUsedRAMSize(){
        return mMemoryInfo.totalMem-mMemoryInfo.availMem;
    }
}
