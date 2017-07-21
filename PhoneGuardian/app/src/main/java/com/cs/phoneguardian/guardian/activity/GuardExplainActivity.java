package com.cs.phoneguardian.guardian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.cs.phoneguardian.R;

import butterknife.ButterKnife;

/**
 * Created by SEELE on 2017/7/13.
 * 防盗卫士功能说明界面
 */

public class GuardExplainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_explain);
        ButterKnife.bind(this);

    }

    public static void startGuardExplainActivity(Context context) {
        Intent intent = new Intent(context, GuardExplainActivity.class);
        context.startActivity(intent);
    }
}
