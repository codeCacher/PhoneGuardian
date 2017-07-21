package com.cs.phoneguardian.guardian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.guardian.fragment.PassWordSettingFragment;
import com.cs.phoneguardian.modle.PhoneStateDataSource;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_password)
    RelativeLayout rlPassword;
    @BindView(R.id.tv_contact)
    TextView tvContact;
    @BindView(R.id.rl_contact)
    RelativeLayout rlContact;
    @BindView(R.id.tv_sim_state)
    TextView tvSimState;
    @BindView(R.id.sc_sim_state)
    SwitchCompat scSimState;
    @BindView(R.id.rl_sim)
    RelativeLayout rlSim;
    @BindView(R.id.tv_enable_state)
    TextView tvEnableState;
    @BindView(R.id.sc_enalbe_state)
    SwitchCompat scEnalbeState;
    @BindView(R.id.rl_enalbe)
    RelativeLayout rlEnalbe;

    private boolean mSimCheckState;
    private boolean mEnableState;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard_setting);

        ButterKnife.bind(this);

        rlPassword.setOnClickListener(this);
        rlContact.setOnClickListener(this);
        rlSim.setOnClickListener(this);
        rlEnalbe.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        //根据设置值初始化界面
        String contactNumber = SharedPreferencesUtils.getString(this, Constants.KEY_MERGENCY_CONTACT, "");
        String sim = SharedPreferencesUtils.getString(this, Constants.KEY_SIM, "");
        mEnableState = SharedPreferencesUtils.getBoolean(this, Constants.KEY_GUARD_OPEN_STATE, false);
        if(TextUtils.isEmpty(contactNumber)){
            tvContact.setText("未设置紧急联系人");
        }else {
            tvContact.setText("紧急联系人："+contactNumber+"");
        }
        if(TextUtils.isEmpty(sim)){
            mSimCheckState = false;
            tvSimState.setText("SIM卡未绑定");
            scSimState.setChecked(false);
        }else {
            mSimCheckState = true;
            tvSimState.setText("SIM卡已绑定");
            scSimState.setChecked(true);
        }
        if(mEnableState){
            tvEnableState.setText("手机防盗开启");
            scEnalbeState.setChecked(true);
        }else {
            tvEnableState.setText("手机防盗未开启");
            scEnalbeState.setChecked(false);
        }
    }

    public static void startSettingActivity(Context context) {
        Intent intent = new Intent(context, SettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_password:
                PasswordSettingActivity.startPasswordSettingActivity(SettingActivity.this);
                break;

            case R.id.rl_contact:
                ContactSettingActivity.startContactSettingActivity(SettingActivity.this);
                break;

            case R.id.rl_sim:
                if(mSimCheckState){
                    SharedPreferencesUtils.remove(SettingActivity.this, Constants.KEY_SIM);
                    mSimCheckState = false;
                    scSimState.setChecked(false);
                    tvSimState.setText("SIM卡未绑定");
                }else {
                    String simSerialNumber = PhoneStateDataSource.getInstance(SettingActivity.this).getSimSerialNumber();
                    SharedPreferencesUtils.putString(SettingActivity.this,Constants.KEY_SIM,simSerialNumber);
                    scSimState.setChecked(true);
                    mSimCheckState = true;
                    tvSimState.setText("SIM卡已绑定");
                }
                break;

            case R.id.rl_enalbe:
                if(mEnableState){
                    SharedPreferencesUtils.putBoolean(SettingActivity.this,Constants.KEY_GUARD_OPEN_STATE,false);
                    scEnalbeState.setChecked(false);
                    mEnableState = false;
                    tvEnableState.setText("手机防盗未开启");
                }else {
                    SharedPreferencesUtils.putBoolean(SettingActivity.this,Constants.KEY_GUARD_OPEN_STATE,true);
                    scEnalbeState.setChecked(true);
                    mEnableState = true;
                    tvEnableState.setText("手机防盗开启");
                }
                break;
        }
    }
}
