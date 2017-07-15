package com.cs.phoneguardian.accelerate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.modle.PhoneStateDataSource;
import com.cs.phoneguardian.utils.DisplayUtils;
import com.cs.phoneguardian.view.RoundProgressBar;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEELE on 2017/7/13.
 * 加速界面
 */

public class AccActivity extends AppCompatActivity implements AccContract.View {
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.rpb_percent)
    RoundProgressBar rpbPercent;
    @BindView(R.id.tv_percent)
    TextView tvPercent;
    @BindView(R.id.rl_percent)
    RelativeLayout rlPercent;
    @BindView(R.id.tv_mem_size)
    TextView tvMemSize;
    @BindView(R.id.nsv)
    NestedScrollView nsv;
    @BindView(R.id.rv_active_app)
    RecyclerView rvActiveApp;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.cl_root)
    CoordinatorLayout clRoot;

    private AccContract.Presenter mPresenter;
    private ActiveAppAdapter mActiveAppAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);
        ButterKnife.bind(this);

        AccPresenter.getInstance(AppInfoDataSource.getInstance(this), PhoneStateDataSource.getInstance(this), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvActiveApp.setLayoutManager(linearLayoutManager);
        mActiveAppAdapter = new ActiveAppAdapter(this, mPresenter);
        rvActiveApp.setAdapter(mActiveAppAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    public static void startAccAcitvity(Context context) {
        Intent intent = new Intent(context, AccActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setPresenter(AccContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showResumeAnimation(int percent) {
        //应用列表出现动画
        TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_SELF, 0f);
        translateAnimation.setDuration(500);
        translateAnimation.setFillAfter(true);
        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                nsv.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        nsv.startAnimation(translateAnimation);

        rpbPercent.swip(0, percent, 500, new RoundProgressBar.OnProgressChangeListener() {
            @Override
            public void OnProgressChange(int progress) {
                tvPercent.setText(progress + "");
            }
        });
    }

    @Override
    public void showMemorySize(long used, long totle) {
        String usedSize = Formatter.formatFileSize(this, used);
        String totleSize = Formatter.formatFileSize(this, totle);
        tvMemSize.setText(usedSize + "/" + totleSize);
    }

    @Override
    public void showMemoryPercent(int percent) {
        rpbPercent.setProgress(percent);
        tvPercent.setText(percent + "");
    }

    @Override
    public void showState(int percent) {
        if (percent < 60) {
            tvState.setText("手机状态不错，继续保持");
        } else if (percent < 80) {
            tvState.setText("手机内存不足，可清理内存");
        } else {
            tvState.setText("手机严重内存不足，请清理内存");
        }
    }

    @Override
    public void upDateAppList(List<AppInfo> userAppList, List<AppInfo> sysAppList) {
        mActiveAppAdapter.updateList(userAppList, sysAppList);
    }

    @Override
    public void initNestedScrollView() {
        //初始化高度
        DisplayUtils displayUtils = DisplayUtils.getInstance(this);
        final int statusBarHeight = displayUtils.getStatusBarHeight();
        int displayHeight = displayUtils.getDisplayHeight();
        ViewGroup.LayoutParams layoutParams = nsv.getLayoutParams();
        layoutParams.height = displayHeight - statusBarHeight - rlTitle.getHeight();
        nsv.setLayoutParams(layoutParams);

        //初始化滚动事件控制
        rvActiveApp.setNestedScrollingEnabled(false);

        nsv.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                Log.e("haha",nsv.getTop()+"");


                if(scrollY>0&& nsv.getTop()>statusBarHeight+rlTitle.getHeight()){
                    nsv.smoothScrollTo(0,0);
                }
            }
        });

        rlTop.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("haha",top+"");

            }
        });

        nsv.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                Log.e("haha",top+"");
            }
        });
    }
}
