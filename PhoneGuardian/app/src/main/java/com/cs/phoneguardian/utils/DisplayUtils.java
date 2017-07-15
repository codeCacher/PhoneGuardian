package com.cs.phoneguardian.utils;

import android.content.Context;
import android.view.WindowManager;

import java.lang.reflect.Field;

/**
 * Created by SEELE on 2017/7/15.
 */

public class DisplayUtils {

    private final Context mContext;
    private WindowManager mWindowsManager;

    private DisplayUtils(Context context){
        this.mContext = context;
        this.mWindowsManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }

    public static DisplayUtils getInstance(Context context){
        return new DisplayUtils(context);
    }

    public int getStatusBarHeight() {
        Class<?> c;

        Object obj;

        Field field;

        int x = 0, sbar = 0;

        try {

            c = Class.forName("com.android.internal.R$dimen");

            obj = c.newInstance();

            field = c.getField("status_bar_height");

            x = Integer.parseInt(field.get(obj).toString());

            sbar = mContext.getResources().getDimensionPixelSize(x);

        } catch (Exception e1) {

            e1.printStackTrace();

        }

        return sbar;
    }

    public int getDisplayHeight(){
        return mWindowsManager.getDefaultDisplay().getHeight();
    }

    public int getDisplayWidth(){
        return mWindowsManager.getDefaultDisplay().getWidth();
    }
}
