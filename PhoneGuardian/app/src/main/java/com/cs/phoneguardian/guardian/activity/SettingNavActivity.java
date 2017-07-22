package com.cs.phoneguardian.guardian.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.guardian.fragment.BindSIMFragment;
import com.cs.phoneguardian.guardian.fragment.EnalbeSettingFragment;
import com.cs.phoneguardian.guardian.fragment.MergencyContactSettingFragment;
import com.cs.phoneguardian.guardian.fragment.PassWordSettingFragment;
import com.cs.phoneguardian.utils.DialogUtils;
import com.cs.phoneguardian.view.InhibitableViewPager;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/21.
 */

public class SettingNavActivity extends AppCompatActivity {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.vp)
    InhibitableViewPager vp;
    @BindView(R.id.iv_dot_0)
    ImageView ivDot0;
    @BindView(R.id.iv_dot_1)
    ImageView ivDot1;
    @BindView(R.id.iv_dot_2)
    ImageView ivDot2;
    @BindView(R.id.iv_dot_3)
    ImageView ivDot3;
    private List<Fragment> mFragmentList;
    private List<ImageView> mImageViewList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_nav);
        ButterKnife.bind(this);

        mFragmentList = new ArrayList<>();
        PassWordSettingFragment passWordSettingFragment = new PassWordSettingFragment();
        BindSIMFragment bindSIMFragment = new BindSIMFragment();
        MergencyContactSettingFragment mergencyContactSettingFragment = new MergencyContactSettingFragment();
        EnalbeSettingFragment enalbeSettingFragment = new EnalbeSettingFragment();
        mFragmentList.add(passWordSettingFragment);
        mFragmentList.add(bindSIMFragment);
        mFragmentList.add(mergencyContactSettingFragment);
        mFragmentList.add(enalbeSettingFragment);
        vp.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragmentList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentList.size();
            }
        });

        mImageViewList = new ArrayList<>();
        mImageViewList.add(ivDot0);
        mImageViewList.add(ivDot1);
        mImageViewList.add(ivDot2);
        mImageViewList.add(ivDot3);
        vp.setScrollEnable(false);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < mImageViewList.size(); i++) {
                    if (i == position) {
                        mImageViewList.get(i).setImageResource(R.drawable.dot_blue);
                    } else {
                        mImageViewList.get(i).setImageResource(R.drawable.dot_gray_blue);
                    }
                }
            }
            @Override
            public void onPageScrollStateChanged(int state) {}
        });

    }

    @Override
    public void onBackPressed() {
        int currentItem = vp.getCurrentItem();
        if(currentItem>0){
            vp.setCurrentItem(--currentItem);
        }else {
            DialogUtils.showConfirmDialog(this, "是否确定退出导航界面？", new DialogUtils.OnButtonClickedListener() {
                @Override
                public void OnConfirm() {
                    SettingNavActivity.this.finish();
                }

                @Override
                public void OnCancel() {

                }
            });
        }
    }

    public void setNavPosition(int position){
        vp.setCurrentItem(position);
    }

    public void setNavScrollEnable(boolean enable){
        vp.setScrollEnable(enable);
    }

    public static void startSettingNavActivity(Context context) {
        Intent intent = new Intent(context, SettingNavActivity.class);
        context.startActivity(intent);
    }
}
