package com.cs.phoneguardian.guardian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.guardian.activity.SettingNavActivity;
import com.cs.phoneguardian.modle.PhoneStateDataSource;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class EnalbeSettingFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.sc_state)
    SwitchCompat scState;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    private SettingNavActivity mParentActivity;
    private boolean mCheckState;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enalbe_setting, container, false);
        ButterKnife.bind(this, view);

        mParentActivity = (SettingNavActivity) getActivity();

        boolean guardEnable = SharedPreferencesUtils.getBoolean(getContext(), Constants.KEY_GUARD_OPEN_STATE, false);
        if(!guardEnable){
            scState.setChecked(false);
            mCheckState = false;
            tvState.setText("手机防盗未开启");
            tvState.setTextColor(getResources().getColor(R.color.colorGrayBlack));
        }else {
            scState.setChecked(true);
            mCheckState = true;
            tvState.setText("手机防盗开启");
            tvState.setTextColor(getResources().getColor(R.color.colorPrimary));
        }

        scState.setOnClickListener(this);
        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sc_state:
                if(mCheckState){
                    SharedPreferencesUtils.putBoolean(getContext(),Constants.KEY_GUARD_OPEN_STATE,false);
                    scState.setChecked(false);
                    mCheckState = false;
                    tvState.setText("手机防盗未开启");
                    tvState.setTextColor(getResources().getColor(R.color.colorGrayBlack));
                }else {
                    SharedPreferencesUtils.putBoolean(getContext(),Constants.KEY_GUARD_OPEN_STATE,true);
                    scState.setChecked(true);
                    mCheckState = true;
                    tvState.setText("手机防盗开启");
                    tvState.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                break;

            case R.id.tv_yes:
                Toast.makeText(getContext(),"手机防盗设置完成，可在使用说明中查看使用方法",Toast.LENGTH_LONG).show();
                mParentActivity.finish();
                break;

            case R.id.tv_no:
                mParentActivity.setNavPosition(2);
                break;
        }
    }
}
