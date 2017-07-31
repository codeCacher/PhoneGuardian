package com.cs.phoneguardian.guardian.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.Contact;
import com.cs.phoneguardian.guardian.activity.ContactActivity;
import com.cs.phoneguardian.guardian.activity.SettingNavActivity;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class MergencyContactSettingFragment extends Fragment implements View.OnClickListener {
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_contact)
    ImageView ivContact;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    private SettingNavActivity mParentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mergency_contact_setting, container, false);
        ButterKnife.bind(this, view);

        mParentActivity = (SettingNavActivity) getActivity();

        String setedPhone = SharedPreferencesUtils.getString(getContext(), Constants.KEY_MERGENCY_CONTACT, "");
        if(!TextUtils.isEmpty(setedPhone)){
            etPhone.setText(setedPhone);
        }

        ivContact.setOnClickListener(this);
        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            Contact contact = (Contact)data.getParcelableExtra(Constants.KEY_CONTACT_RESULT);
            etPhone.setText(contact.getPhoneNumber());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_contact:
                ContactActivity.fragmentStartContactActivityForResult(getContext(),this,0);
                break;

            case R.id.tv_yes:
                String phone = etPhone.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(getContext(), "请输入紧急联系人号码", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferencesUtils.putString(getContext(), Constants.KEY_MERGENCY_CONTACT,phone);
                    mParentActivity.setNavPosition(3);
                }
                break;

            case R.id.tv_no:
                mParentActivity.setNavPosition(1);
                break;
        }
    }
}
