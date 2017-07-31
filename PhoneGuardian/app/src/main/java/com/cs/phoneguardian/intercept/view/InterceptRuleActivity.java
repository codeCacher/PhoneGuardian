package com.cs.phoneguardian.intercept.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
 * Created by Administrator on 2017/7/26.
 */

public class InterceptRuleActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_black)
    TextView tvBlack;
    @BindView(R.id.sc_black)
    SwitchCompat scBlack;
    @BindView(R.id.rl_black)
    RelativeLayout rlBlack;
    @BindView(R.id.tv_stranger)
    TextView tvStranger;
    @BindView(R.id.sc_stranger)
    SwitchCompat scStranger;
    @BindView(R.id.rl_stranger)
    RelativeLayout rlStranger;
    @BindView(R.id.tv_white)
    TextView tvWhite;
    @BindView(R.id.sc_white)
    SwitchCompat scWhite;
    @BindView(R.id.rl_white)
    RelativeLayout rlWhite;
    private boolean mBlack;
    private boolean mStranger;
    private boolean mWhite;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept_rule);
        ButterKnife.bind(this);

        rlBlack.setOnClickListener(this);
        rlStranger.setOnClickListener(this);
        rlWhite.setOnClickListener(this);

        initState();
    }

    private void initState() {
        mBlack = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_RULE_BLACK, false);
        mStranger = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_RULE_STRANGER, false);
        mWhite = SharedPreferencesUtils.getBoolean(this, Constants.KEY_INTERCEPT_RULE_WHITE, false);

        if(mWhite){
            scWhite.setChecked(true);
            disableBlackStrangerItem();
            return;
        }else {
            enableBlackStrangerItem();
            scWhite.setChecked(false);
        }

        if(mBlack){
            scBlack.setChecked(true);
        }else {
            scBlack.setChecked(false);
        }

        if(mStranger){
            scStranger.setChecked(true);
        }else {
            scStranger.setChecked(false);
        }

    }

    public static void startInterceptRuleActivity(Context context) {
        Intent intent = new Intent(context, InterceptRuleActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_black:
                if(mBlack){
                    disableBlack();
                }else {
                    enableBlack();
                }
                break;

            case R.id.rl_stranger:
                if(mStranger){
                    disableStranger();
                }else {
                    enableStranger();
                }
                break;

            case R.id.rl_white:
                if(mWhite){
                    disableWhite();
                    enableBlackStrangerItem();
                    initState();
                }else {
                    disableBlackStrangerItem();
                    enableWhite();
                }
                break;
        }
    }

    private void enableBlack(){
        mBlack= true;
        scBlack.setChecked(true);
        SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_RULE_BLACK,true);
    }
    private void disableBlack(){
        mBlack= false;
        scBlack.setChecked(false);
        SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_RULE_BLACK,false);
    }
    private void enableStranger(){
        mStranger= true;
        scStranger.setChecked(true);
        SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_RULE_STRANGER,true);
    }
    private void disableStranger(){
        mStranger= false;
        scStranger.setChecked(false);
        SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_RULE_STRANGER,false);
    }
    private void enableWhite(){
        mWhite= true;
        scWhite.setChecked(true);
        SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_RULE_WHITE,true);
    }
    private void disableWhite(){
        mWhite= false;
        scWhite.setChecked(false);
        SharedPreferencesUtils.putBoolean(this,Constants.KEY_INTERCEPT_RULE_WHITE,false);
    }
    private void disableBlackStrangerItem(){
        tvBlack.setTextColor(getResources().getColor(R.color.colorGrayBlack));
        scBlack.setChecked(false);
        rlBlack.setClickable(false);
        tvStranger.setTextColor(getResources().getColor(R.color.colorGrayBlack));
        scStranger.setChecked(false);
        rlStranger.setClickable(false);
    }
    private void enableBlackStrangerItem(){
        tvBlack.setTextColor(Color.BLACK);
        scBlack.setChecked(true);
        rlBlack.setClickable(true);
        tvStranger.setTextColor(Color.BLACK);
        scStranger.setChecked(true);
        rlStranger.setClickable(true);
    }
}
