package com.cs.phoneguardian.guardian.activity;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.AdminReceiver;
import com.cs.phoneguardian.R;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.CustomActivityJumpUtils;
import com.cs.phoneguardian.utils.DialogUtils;
import com.cs.phoneguardian.utils.MD5Utils;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;
import com.cs.phoneguardian.view.NestScrollLayout;
import com.cs.phoneguardian.view.ScrollViewChild;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEELE on 2017/7/13.
 * 防盗卫士主界面
 */

public class GuardActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_logo)
    ImageView ivLogo;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.mask_id)
    View maskId;
    @BindView(R.id.rl_explarin)
    RelativeLayout rlExplarin;
    @BindView(R.id.rl_set_nav)
    RelativeLayout rlSetNav;
    @BindView(R.id.child_id)
    ScrollViewChild childId;
    @BindView(R.id.nsl)
    NestScrollLayout nsl;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private int mMinMaskHeight;
    private int mDefaultMaskHeight;
    private int mBottomHeight;
    private boolean mGuardOpenState;
    private boolean mCipherState;
    private ComponentName mComponentName;
    private DevicePolicyManager mDPM;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guard);
        ButterKnife.bind(this);

        mMinMaskHeight = rlTitle.getLayoutParams().height;
        mDefaultMaskHeight = rlTop.getLayoutParams().height;
        mBottomHeight = rlBottom.getLayoutParams().height;
        nsl.init(mMinMaskHeight, mDefaultMaskHeight, mBottomHeight, rlTop);

        rlExplarin.setOnClickListener(this);
        rlSetNav.setOnClickListener(this);
        rlSetting.setOnClickListener(this);

        //检查设备管理器权限
        mComponentName = new ComponentName(this, AdminReceiver.class);
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        if(!mDPM.isAdminActive(mComponentName)){
            DialogUtils.showConfirmDialog(this, "手机防盗功能需要管理员权限，是否前往开启？", new DialogUtils.OnButtonClickedListener() {
                @Override
                public void OnConfirm() {
                    CustomActivityJumpUtils.startDevicePolicyActivityForResult(GuardActivity.this,0, mComponentName);
                }

                @Override
                public void OnCancel() {
                    Toast.makeText(GuardActivity.this,"未激活管理员权限，无法使用手机防盗功能",Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //获取目前的设置状态，手机防盗是否已开启，密码是否已经设置
        mCipherState = SharedPreferencesUtils.getBoolean(this, Constants.KEY_PSD_STATE, false);
        mGuardOpenState = SharedPreferencesUtils.getBoolean(this, Constants.KEY_GUARD_OPEN_STATE, false);
        if(mGuardOpenState){
            tvState.setText("手机防盗已开启");
        }else {
            tvState.setText("手机防盗未开启");
        }

        if(!mDPM.isAdminActive(mComponentName)) {
            tvState.setText("管理员权限未开启");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==0){
            Toast.makeText(this,"未激活管理员权限，无法使用手机防盗功能",Toast.LENGTH_SHORT).show();
        }
    }

    public static void startGuardAcitvity(Context context) {
        Intent intent = new Intent(context, GuardActivity.class);
        context.startActivity(intent);
    }

    private interface OnPSDPassedListener{
        void OnPSDPassde();
    }

    private void showCipherDialog(final OnPSDPassedListener listener){
        final AlertDialog cipherDialog = new AlertDialog.Builder(GuardActivity.this).create();
        View view = View.inflate(GuardActivity.this, R.layout.password_dialog, null);
        final EditText etPsd = (EditText) view.findViewById(R.id.et_psd);
        TextView tvOK = (TextView) view.findViewById(R.id.tv_ok);
        TextView tvCancel = (TextView) view.findViewById(R.id.tv_cancel);

        tvOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputPsd = etPsd.getText().toString();
                String MD5InputPsd = MD5Utils.MD5Encode(inputPsd);
                String MD5Psd = SharedPreferencesUtils.getString(GuardActivity.this, Constants.KEY_PSD, "");
                if(MD5InputPsd.equals(MD5Psd)){
                    listener.OnPSDPassde();
                    cipherDialog.dismiss();
                }else {
                    Toast.makeText(GuardActivity.this,"密码错误",Toast.LENGTH_SHORT).show();
                    etPsd.setText("");
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cipherDialog.dismiss();
            }
        });

        cipherDialog.setView(view);
        cipherDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            //使用说明
            case R.id.rl_explarin:
                GuardExplainActivity.startGuardExplainActivity(GuardActivity.this);
                break;
            //设置导航
            case R.id.rl_set_nav:
                if(mCipherState){
                    showCipherDialog(new OnPSDPassedListener() {
                        @Override
                        public void OnPSDPassde() {
                            //密码验证通过，跳转到导航界面
                            SettingNavActivity.startSettingNavActivity(GuardActivity.this);
                        }
                    });
                }else {
                    //没有设置密码，跳转到设置导航界面
                    SettingNavActivity.startSettingNavActivity(GuardActivity.this);
                }
                break;
            //设置
            case R.id.rl_setting:
                if(mCipherState){
                    //如果已经设置了密码，则要输入密码
                    showCipherDialog(new OnPSDPassedListener() {
                        @Override
                        public void OnPSDPassde() {
                            //密码验证通过，跳转到设置界面
                            SettingActivity.startSettingActivity(GuardActivity.this);
                        }
                    });
                }else {
                    //如果还没有设置密码，则要提醒进行设置导航
                    AlertDialog dialog = new AlertDialog.Builder(GuardActivity.this)
                            .setMessage("请先进行设置导航，是否进入设置导航？")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    SettingNavActivity.startSettingNavActivity(GuardActivity.this);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create();
                    dialog.show();
                }

                break;
        }
    }
}
