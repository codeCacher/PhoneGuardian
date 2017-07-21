package com.cs.phoneguardian.guardian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.MD5Utils;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class PasswordSettingActivity extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_setting);

        ButterKnife.bind(this);

        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);
    }

    public static void startPasswordSettingActivity(Context context) {
        Intent intent = new Intent(context, PasswordSettingActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no:
                PasswordSettingActivity.this.finish();
                break;

            case R.id.tv_yes:
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(PasswordSettingActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(PasswordSettingActivity.this, "请再次确认密码", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(PasswordSettingActivity.this, "两次输入的密码不同", Toast.LENGTH_SHORT).show();
                } else if (password.equals(confirmPassword)) {
                    //输入密码成功
                    SharedPreferencesUtils.putString(PasswordSettingActivity.this, Constants.KEY_PSD, MD5Utils.MD5Encode(password));
                    SharedPreferencesUtils.putBoolean(PasswordSettingActivity.this, Constants.KEY_PSD_STATE, true);
                    Toast.makeText(PasswordSettingActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
                    PasswordSettingActivity.this.finish();
                }
                break;
        }

    }
}
