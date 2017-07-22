package com.cs.phoneguardian.applock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class AppLockSettingActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_password)
    RelativeLayout rlPassword;
    @BindView(R.id.tv_enable_state)
    TextView tvEnableState;
    @BindView(R.id.sc_enalbe_state)
    SwitchCompat scEnalbeState;
    @BindView(R.id.rl_enalbe)
    RelativeLayout rlEnalbe;
    private boolean mEnalbe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock_setting);

        ButterKnife.bind(this);

        rlPassword.setOnClickListener(this);
        rlEnalbe.setOnClickListener(this);

        //初始化界面
        mEnalbe = SharedPreferencesUtils.getBoolean(this, Constants.KEY_ENABLE_APP_LOCK, false);
        if(mEnalbe){
            tvEnableState.setText("应用锁开启");
            scEnalbeState.setChecked(true);
        }else {
            tvEnableState.setText("应用锁未开启");
            scEnalbeState.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_password:
                AppLockPasswordSettingActivity.startAppLockPasswordSettingActivity(AppLockSettingActivity.this);
                break;

            case R.id.rl_enalbe:
                if(mEnalbe){
                    mEnalbe = false;
                    tvEnableState.setText("应用锁未开启");
                    scEnalbeState.setChecked(false);
                    SharedPreferencesUtils.putBoolean(this,Constants.KEY_ENABLE_APP_LOCK,false);
                }else {
                    mEnalbe = true;
                    tvEnableState.setText("应用锁开启");
                    scEnalbeState.setChecked(true);
                    SharedPreferencesUtils.putBoolean(this,Constants.KEY_ENABLE_APP_LOCK,true);
                }
                break;
        }
    }

    public static void startAppLockSettingActivity(Context context) {
        Intent intent = new Intent(context, AppLockSettingActivity.class);
        context.startActivity(intent);
    }
}
