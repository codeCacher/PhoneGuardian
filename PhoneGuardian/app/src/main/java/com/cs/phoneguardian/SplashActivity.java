package com.cs.phoneguardian;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.cs.phoneguardian.main.MainActivity;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
}
