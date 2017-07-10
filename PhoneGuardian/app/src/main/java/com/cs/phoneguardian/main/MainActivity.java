package com.cs.phoneguardian.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;
import com.cs.phoneguardian.view.RoundProgressBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/8.
 */

public class MainActivity extends AppCompatActivity {
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.rpb_progress)
    RoundProgressBar rpbProgress;
    @BindView(R.id.iv_round_btn)
    ImageView ivRoundBtn;
    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.tv_little)
    TextView tvLittle;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    @BindView(R.id.rl_round)
    RelativeLayout rlRound;
    @BindView(R.id.tv_statuse)
    TextView tvStatuse;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.iv_dot_right)
    ImageView ivDotRight;
    @BindView(R.id.iv_dot_left)
    ImageView ivDotLeft;
    private List<Fragment> mFragmentList;
    private MainViewPagerAdapter mMainViewPagerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        int preCheckScore = SharedPreferencesUtils.getInt(this, Constants.CHECK_RESULT, 100);

        initViewPager();
        initIndacator();

    }


    private void initViewPager() {
        mFragmentList = new ArrayList<>();
        MainPageOneFragment mainPageOneFragment = new MainPageOneFragment();
        mFragmentList.add(mainPageOneFragment);
        MainPageTwoFragment mainPageTwoFragment = new MainPageTwoFragment();
        mFragmentList.add(mainPageTwoFragment);

        mMainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager(), mFragmentList);
        vp.setAdapter(mMainViewPagerAdapter);
    }

    private void initIndacator() {
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setIndacator(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setIndacator(int position) {
        switch (position) {
            case 0:
                ivDotLeft.setImageResource(R.drawable.dot_white);
                ivDotRight.setImageResource(R.drawable.dot_fog_blue);
                break;
            case 1:
                ivDotLeft.setImageResource(R.drawable.dot_fog_blue);
                ivDotRight.setImageResource(R.drawable.dot_white);
                break;
        }
    }

    public static void openMainActivity(Context applicationContext, int flagActivityNewTask) {
        applicationContext.startActivity(new Intent(applicationContext, MainActivity.class).setFlags(flagActivityNewTask));
    }
}
