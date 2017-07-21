package com.cs.phoneguardian.guardian.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.guardian.activity.SettingNavActivity;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.DialogUtils;
import com.cs.phoneguardian.utils.MD5Utils;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class PassWordSettingFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.et_password)
    EditText etPassword;
    @BindView(R.id.et_confirm_password)
    EditText etConfirmPassword;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_password_setting, container, false);
        ButterKnife.bind(this, view);

        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_no:
                DialogUtils.showConfirmDialog(getContext(), "是否确定退出设置导航？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
                break;

            case R.id.tv_yes:
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getContext(), "请输入密码", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(getContext(), "请再次确认密码", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(getContext(), "两次输入的密码不同", Toast.LENGTH_SHORT).show();
                } else if (password.equals(confirmPassword)) {
                    //输入密码成功
                    SharedPreferencesUtils.putString(getContext(), Constants.KEY_PSD, MD5Utils.MD5Encode(password));
                    SharedPreferencesUtils.putBoolean(getContext(), Constants.KEY_PSD_STATE, true);
                    Toast.makeText(getContext(), "密码设置成功", Toast.LENGTH_SHORT).show();
                    SettingNavActivity parentActivity = (SettingNavActivity) getActivity();
                    parentActivity.setNavPosition(1);
                }
                break;
        }

    }
}
