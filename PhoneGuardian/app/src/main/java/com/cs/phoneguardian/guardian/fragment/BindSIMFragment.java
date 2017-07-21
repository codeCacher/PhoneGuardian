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

public class BindSIMFragment extends Fragment implements View.OnClickListener{
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
        View view = inflater.inflate(R.layout.fragment_bind_sim, container, false);
        ButterKnife.bind(this, view);

        mParentActivity = (SettingNavActivity) getActivity();

        String SIM = SharedPreferencesUtils.getString(getContext(), Constants.KEY_SIM, "");
        if(TextUtils.isEmpty(SIM)){
            scState.setChecked(false);
            mCheckState = false;
            tvState.setText("SIM卡未绑定");
            tvState.setTextColor(getResources().getColor(R.color.colorGrayBlack));
        }else {
            scState.setChecked(true);
            mCheckState = true;
            tvState.setText("SIM卡已绑定");
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
                    SharedPreferencesUtils.remove(getContext(),Constants.KEY_SIM);
                    scState.setChecked(false);
                    mCheckState = false;
                    tvState.setText("SIM卡未绑定");
                    tvState.setTextColor(getResources().getColor(R.color.colorGrayBlack));
                }else {
                    String simSerialNumber = PhoneStateDataSource.getInstance(getContext()).getSimSerialNumber();
                    SharedPreferencesUtils.putString(getContext(),Constants.KEY_SIM,simSerialNumber);
                    scState.setChecked(true);
                    mCheckState = true;
                    tvState.setText("SIM卡已绑定");
                    tvState.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                break;

            case R.id.tv_yes:
                mParentActivity.setNavPosition(2);
                break;

            case R.id.tv_no:
                mParentActivity.setNavPosition(0);
                break;
        }
    }
}
