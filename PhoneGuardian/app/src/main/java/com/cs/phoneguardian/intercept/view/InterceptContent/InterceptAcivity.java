package com.cs.phoneguardian.intercept.view.InterceptContent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;
import com.cs.phoneguardian.intercept.InterceptContract;
import com.cs.phoneguardian.intercept.modle.InterceptDataSource;
import com.cs.phoneguardian.intercept.view.InterceptSettingActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cs.phoneguardian.intercept.presenter.InterceptPresenter.PAGE_PHONE_CALL;
import static com.cs.phoneguardian.intercept.presenter.InterceptPresenter.PAGE_SMS;

/**
 * Created by Administrator on 2017/7/23.
 */

public class InterceptAcivity extends AppCompatActivity implements InterceptContract.InterceptBaseView,View.OnClickListener{
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.bt_sms)
    Button btSms;
    @BindView(R.id.bt_phone)
    Button btPhone;
    @BindView(R.id.vp_content)
    ViewPager vpContent;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.iv_del)
    ImageView ivDel;
    @BindView(R.id.tv_del)
    TextView tvDel;
    @BindView(R.id.rl_del)
    RelativeLayout rlDel;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private InterceptContract.InterceptBasePresenter mPresenter;
    private List<InterceptFragment> mFragmentList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intercept);

        ButterKnife.bind(this);

        com.cs.phoneguardian.intercept.presenter.InterceptPresenter.getInstance(this,this,InterceptDataSource.getInstance(this));

        mFragmentList = new ArrayList<>();
        InterceptFragment smsFragment = new InterceptFragment();
        smsFragment.setType(InterceptFragment.TYPE_SMS);
        smsFragment.setPresenter(mPresenter);
        final InterceptFragment phoneFragment = new InterceptFragment();
        phoneFragment.setType(InterceptFragment.TYPE_PHONE);
        phoneFragment.setPresenter(mPresenter);
        mFragmentList.add(smsFragment);
        mFragmentList.add(phoneFragment);

        vpContent.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });
        vpContent.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                if(position==0){
                    mPresenter.selectInterceptSms();
                }else {
                    mPresenter.selectInterceptPhone();
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        btSms.setOnClickListener(this);
        btPhone.setOnClickListener(this);
        rlSetting.setOnClickListener(this);
        rlDel.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static void startInterceptAcivity(Context context) {
        Intent intent = new Intent(context, InterceptAcivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setPresenter(InterceptContract.InterceptBasePresenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_sms:
                mPresenter.selectInterceptSms();
                break;

            case R.id.bt_phone:
                mPresenter.selectInterceptPhone();
                break;

            case R.id.rl_setting:
                InterceptSettingActivity.startInterceptSettingActivity(this);
                break;

            case R.id.rl_del:
                int currentItem = vpContent.getCurrentItem();
                if(currentItem==0){
                    mPresenter.deletAllSMS();
                }else {
                    mPresenter.deletAllPhoneCall();
                }
                break;
        }
    }

    @Override
    public void showInterceptSms(int smsSize) {
        btSms.setSelected(true);
        btPhone.setSelected(false);
        vpContent.setCurrentItem(0);
        if(smsSize==0){
            disableDeleteAllBtn();
        }else {
            enableDeleteAllBtn();
        }
    }

    @Override
    public void showInterceptPhone(int phoneSize) {
        btSms.setSelected(false);
        btPhone.setSelected(true);
        vpContent.setCurrentItem(1);
        if(phoneSize==0){
            disableDeleteAllBtn();
        }else {
            enableDeleteAllBtn();
        }
    }

    @Override
    public void updateSMSList(List<InterceptSMS> list) {
        mFragmentList.get(0).updateSMSList(list);

    }

    @Override
    public void updatePhoneCallList(List<InterceptPhoneCall> list) {
        mFragmentList.get(1).updatePhoneCallList(list);
    }

    @Override
    public void enableDeleteAllBtn() {
        rlDel.setClickable(true);
        ivDel.setImageResource(R.drawable.delete_list_black);
        tvDel.setTextColor(Color.BLACK);
    }

    @Override
    public void disableDeleteAllBtn() {
        rlDel.setClickable(false);
        ivDel.setImageResource(R.drawable.delete_list_gray);
        tvDel.setTextColor(getResources().getColor(R.color.colorGrayBlack));
    }

    @Override
    public void updateDelAllBtn(int smsSize,int phoneCallSize) {
        if(vpContent.getCurrentItem()==PAGE_SMS){
            if(smsSize==0){
                disableDeleteAllBtn();
            }else {
                enableDeleteAllBtn();
            }
        }else if(vpContent.getCurrentItem()==PAGE_PHONE_CALL){
            if(phoneCallSize==0){
                disableDeleteAllBtn();
            }else {
                enableDeleteAllBtn();
            }
        }
    }

    @Override
    public void showSMSDetailDialog(final InterceptSMS sms) {
        String name = sms.getName();
        String phoneNumber = sms.getPhoneNumber();
        String content = sms.getContent();
        showDialog(name + " " + phoneNumber, content, new OnButtonClickedListener() {
            @Override
            public void OnAddButtonClicked() {
                mPresenter.contactBlackToWhite(sms.getPhoneNumber());
            }

            @Override
            public void OnDeleteButtonClicked() {
                mPresenter.deleteSMS(sms.getId());
            }
        });
    }

    @Override
    public void showPhoneCallDetailDialog(final InterceptPhoneCall phoneCall) {
        String name = phoneCall.getName();
        String phoneNumber = phoneCall.getPhoneNumber();
        showDialog(name, phoneNumber, new OnButtonClickedListener() {
            @Override
            public void OnAddButtonClicked() {
                mPresenter.contactBlackToWhite(phoneCall.getPhoneNumber());
            }

            @Override
            public void OnDeleteButtonClicked() {
                mPresenter.deletePhoneCall(phoneCall.getId());
            }
        });
    }

    private interface OnButtonClickedListener{
        void OnAddButtonClicked();
        void OnDeleteButtonClicked();
    }
    private void showDialog(String title, String content, final OnButtonClickedListener listener){
        View view = View.inflate(this, R.layout.intercept_add_del_item, null);
        final AlertDialog addDelDialog = new AlertDialog.Builder(this)
                .setView(view)
                .create();

        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) view.findViewById(R.id.tv_content);
        Button btAddWhite = (Button) view.findViewById(R.id.bt_add_white);
        Button btDel = (Button) view.findViewById(R.id.bt_del);

        tvTitle.setText(title);
        tvContent.setText(content);

        btAddWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelDialog.dismiss();
                listener.OnAddButtonClicked();
            }
        });

        btDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDelDialog.dismiss();
                listener.OnDeleteButtonClicked();
            }
        });

        addDelDialog.show();
    }
}
