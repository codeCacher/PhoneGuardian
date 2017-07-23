package com.cs.phoneguardian.applock;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.CustomActivityJumpUtils;
import com.cs.phoneguardian.utils.MD5Utils;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class LockActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_app_name)
    TextView tvAppName;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;
    private String mPackageName;
    private AppInfo mAppInfo;
    private String mMD5PSD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        ButterKnife.bind(this);

        mPackageName = getIntent().getStringExtra(Constants.KEY_PKG_NAME_EXTRA);
        mAppInfo = AppInfoDataSource.getInstance(this).getAppInfoByPackageName(mPackageName);

        tvAppName.setText(mAppInfo.getName());
        ivIcon.setImageDrawable(mAppInfo.getIcon());

        mMD5PSD = SharedPreferencesUtils.getString(this, Constants.KEY_APP_LOCK_PASSWORD, "");

        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        CustomActivityJumpUtils.startLauncher(getApplicationContext());
        super.onBackPressed();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_yes:
                String inputPSD = MD5Utils.MD5Encode(etPassword.getText().toString());
                if(inputPSD.equals(mMD5PSD)){
                    //密码正确，发送广播通知服务停止该应用的锁定
                    Intent intent = new Intent(Constants.INTENT_FILT_APP_LOCK_SKIP);
                    intent.putExtra(Constants.KEY_PKG_NAME_EXTRA,mAppInfo.getPackageName());
                    sendBroadcast(intent);
                    this.finish();
                }else {
                    Toast.makeText(this,"密码错误",Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.tv_no:
                CustomActivityJumpUtils.startLauncher(getApplicationContext());
                this.finish();
                break;
        }
    }
}
