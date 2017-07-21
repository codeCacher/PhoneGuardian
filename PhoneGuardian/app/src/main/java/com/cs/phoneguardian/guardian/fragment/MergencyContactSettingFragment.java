package com.cs.phoneguardian.guardian.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cs.phoneguardian.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class MergencyContactSettingFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mergency_contact_setting, container, false);
        ButterKnife.bind(this, view);

        return view;
    }
}
