package com.cs.phoneguardian.guardian.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.Contact;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class ContactSettingActivity extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_contact)
    ImageView ivContact;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.tv_yes)
    TextView tvYes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_setting);

        ButterKnife.bind(this);

        String setedPhone = SharedPreferencesUtils.getString(this, Constants.KEY_MERGENCY_CONTACT, "");
        if(!TextUtils.isEmpty(setedPhone)){
            etPhone.setText(setedPhone);
        }

        ivContact.setOnClickListener(this);
        tvNo.setOnClickListener(this);
        tvYes.setOnClickListener(this);
    }

    public static void startContactSettingActivity(Context context) {
        Intent intent = new Intent(context, ContactSettingActivity.class);
        context.startActivity(intent);
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
                ContactActivity.startContactActivityForResult(ContactSettingActivity.this,this,0);
                break;

            case R.id.tv_yes:
                String phone = etPhone.getText().toString();
                if(TextUtils.isEmpty(phone)){
                    Toast.makeText(ContactSettingActivity.this, "请输入紧急联系人号码", Toast.LENGTH_SHORT).show();
                }else {
                    SharedPreferencesUtils.putString(ContactSettingActivity.this, Constants.KEY_MERGENCY_CONTACT,phone);
                    ContactSettingActivity.this.finish();
                }
                break;

            case R.id.tv_no:
                ContactSettingActivity.this.finish();
                break;
        }
    }
}
