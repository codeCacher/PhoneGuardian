package com.cs.phoneguardian.intercept.view;

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
import com.cs.phoneguardian.intercept.view.InterceptContact.BlackWhiteContactActivity;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/26.
 */

public class InterceptSettingActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_enable_state)
    TextView tvEnableState;
    @BindView(R.id.sc_enalbe_state)
    SwitchCompat scEnalbeState;
    @BindView(R.id.rl_enalbe)
    RelativeLayout rlEnalbe;
    @BindView(R.id.rl_rule)
    RelativeLayout rlRule;
    @BindView(R.id.rl_black)
    RelativeLayout rlBlack;
    @BindView(R.id.rl_white)
    RelativeLayout rlWhite;
    @BindView(R.id.tv_notify)
    TextView tvNotify;
    @BindView(R.id.sc_notify)
    SwitchCompat scNotify;
    @BindView(R.id.rl_notify)
    RelativeLayout rlNotify;
    private boolean mEnable;
    private boolean mInterBlack;
    private boolean mInterStranger;
    private boolean mOnlyWhite;
    private boolean mEnableNotify;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept_setting);
        ButterKnife.bind(this);

        rlEnalbe.setOnClickListener(this);
        rlRule.setOnClickListener(this);
        rlNotify.setOnClickListener(this);
        rlBlack.setOnClickListener(this);
        rlWhite.setOnClickListener(this);

        initSate();
    }

    private void initSate(){
        mEnable = SharedPreferencesUtils.getBoolean(this, Constants.KEY_ENABLE_INTERCEPT, false);
        mInterBlack = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_RULE_BLACK, false);
        mInterStranger = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_RULE_STRANGER, false);
        mOnlyWhite = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_RULE_WHITE, false);
        mEnableNotify = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_NOTIFY_ENALBE, false);

        if(mEnable){
            tvEnableState.setText("骚扰拦截开启");
            scEnalbeState.setChecked(true);
        }else {
            tvEnableState.setText("骚扰拦截未开启");
            scEnalbeState.setChecked(false);
        }

        if(mEnableNotify){
            tvNotify.setText("拦截通知开启");
            scNotify.setChecked(true);
        }else {
            tvNotify.setText("拦截通知未开启");
            scNotify.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_enalbe:
                if(mEnable){
                    mEnable = false;
                    tvEnableState.setText("骚扰拦截未开启");
                    scEnalbeState.setChecked(false);
                    SharedPreferencesUtils.putBoolean(this,Constants.KEY_ENABLE_INTERCEPT,false);
                }else {
                    mEnable = true;
                    tvEnableState.setText("骚扰拦截开启");
                    scEnalbeState.setChecked(true);
                    SharedPreferencesUtils.putBoolean(this,Constants.KEY_ENABLE_INTERCEPT,true);
                }
                break;

            case R.id.rl_rule:
                InterceptRuleActivity.startInterceptRuleActivity(this);
                break;

            case R.id.rl_notify:
                if(mEnableNotify){
                    mEnableNotify = false;
                    tvNotify.setText("骚扰拦截未开启");
                    scNotify.setChecked(false);
                    SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_NOTIFY_ENALBE,false);
                }else {
                    mEnableNotify = true;
                    tvNotify.setText("骚扰拦截开启");
                    scNotify.setChecked(true);
                    SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_NOTIFY_ENALBE,true);
                }
                break;

            case R.id.rl_black:
                BlackWhiteContactActivity.startBlackWhiteContactActivity(this,BlackWhiteContactActivity.BLACK_STATE);
                break;

            case R.id.rl_white:
                BlackWhiteContactActivity.startBlackWhiteContactActivity(this,BlackWhiteContactActivity.WHITE_STATE);
                break;
        }
    }

    public static void startInterceptSettingActivity(Context context){
        Intent intent = new Intent(context,InterceptSettingActivity.class);
        context.startActivity(intent);
    }
}
