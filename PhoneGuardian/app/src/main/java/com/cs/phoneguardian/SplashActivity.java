package com.cs.phoneguardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cs.phoneguardian.applock.AppLockService;
import com.cs.phoneguardian.main.MainActivity;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //开启需要的服务
        openAppLockService();

        //waite this activity to show a few seconds,start the MainActivity and finish it
        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                MainActivity.openMainActivity(getApplicationContext(), Intent.FLAG_ACTIVITY_NEW_TASK);
                finish();
            }
        };
        timer.schedule(task,2000);
    }

    private void openAppLockService() {
        boolean AppLockEnable = SharedPreferencesUtils.getBoolean(this, Constants.KEY_ENABLE_APP_LOCK, false);
        if(AppLockEnable){
            startService(new Intent(this, AppLockService.class));
        }
    }
}
