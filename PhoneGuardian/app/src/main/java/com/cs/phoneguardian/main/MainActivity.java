package com.cs.phoneguardian.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
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

        initViewPager();
        initIndacator();

        initProgressBar();

        ivRoundBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rpbProgress.setRoundProgressColor(getResources().getColor(android.R.color.white));
                rpbProgress.setProgress(0);
                ivSetting.setVisibility(View.INVISIBLE);

                TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_SELF, 0,
                        Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0.5f);
                translateAnimation.setDuration(500);
                translateAnimation.setFillAfter(true);
                vp.startAnimation(translateAnimation);
            }
        });
    }

    private void initViewPager() {
        String[] nameList0 = new String[]{"加速优化", "空间清理", "骚扰拦截", "省电管理", "流量管理", "通知中心"};
        Integer[] pictureIdList0 = new Integer[]{R.drawable.accelerate, R.drawable.clean, R.drawable.accelerate,
                R.drawable.accelerate, R.drawable.accelerate, R.drawable.accelerate};
        String[] nameList1 = new String[]{"加速优化", "空间清理", "骚扰拦截", "省电管理", "流量管理", "通知中心"};
        Integer[] pictureIdList1 = new Integer[]{R.drawable.accelerate, R.drawable.clean, R.drawable.accelerate,
                R.drawable.accelerate, R.drawable.accelerate, R.drawable.accelerate};

        mFragmentList = new ArrayList<>();
        MainViewPagerFragment mainViewPagerFragment0 = new MainViewPagerFragment();
        mainViewPagerFragment0.setArgument(nameList0, pictureIdList0);
        mFragmentList.add(mainViewPagerFragment0);
        MainViewPagerFragment mainViewPagerFragment1 = new MainViewPagerFragment();
        mainViewPagerFragment1.setArgument(nameList1, pictureIdList1);
        mFragmentList.add(mainViewPagerFragment1);

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


    private void initProgressBar() {
        int preCheckScore = SharedPreferencesUtils.getInt(this, Constants.CHECK_RESULT, -1);
        int maxProgress = preCheckScore;
        if (preCheckScore < 0) {
            maxProgress = 100;
            rpbProgress.setRoundProgressColor(getResources().getColor(R.color.colorOrange));
            tvLittle.setVisibility(View.GONE);
            tvScore.setText("体检");
            tvTip.setText("开始扫描");
            tvStatuse.setText("首次使用？请从扫描开始");
        } else if (preCheckScore < 60) {
            rpbProgress.setRoundProgressColor(getResources().getColor(android.R.color.holo_red_dark));
            tvLittle.setVisibility(View.VISIBLE);
            tvScore.setText(maxProgress + "");
            tvTip.setText("点击优化");
            tvStatuse.setText("立即优化，让手机更健康");
        } else if (preCheckScore < 100) {
            rpbProgress.setRoundProgressColor(getResources().getColor(R.color.colorOrange));
            tvLittle.setVisibility(View.VISIBLE);
            tvScore.setText(maxProgress + "");
            tvTip.setText("点击优化");
            tvStatuse.setText("立即优化，让手机更健康");
        } else if (preCheckScore == 100) {
            rpbProgress.setRoundProgressColor(getResources().getColor(R.color.colorGreen));
            tvLittle.setVisibility(View.VISIBLE);
            tvScore.setText(maxProgress + "");
            tvTip.setText("点击扫描");
            tvStatuse.setText("手机状态不错，继续保持");
        }

        rpbProgress.setProgress(maxProgress);
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
