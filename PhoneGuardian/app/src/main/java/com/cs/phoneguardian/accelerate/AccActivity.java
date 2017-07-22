package com.cs.phoneguardian.accelerate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.modle.PhoneStateDataSource;
import com.cs.phoneguardian.view.NestScrollLayout;
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
    @BindView(R.id.tv_lock_state)
    TextView tvMemSize;
    @BindView(R.id.child_id)
    RecyclerView rvActiveApp;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.mask_id)
    View maskId;
    @BindView(R.id.nsl)
    NestScrollLayout nsl;
    @BindView(R.id.tv_total_count)
    TextView tvTotalCount;
    @BindView(R.id.ll_total_count)
    LinearLayout llTotalCount;
    @BindView(R.id.tv_user_app_count)
    TextView tvUserAppCount;
    @BindView(R.id.ll_user_app_count)
    LinearLayout llUserAppCount;
    @BindView(R.id.tv_sys_app_count)
    TextView tvSysAppCount;
    @BindView(R.id.ll_sys_app_count)
    LinearLayout llSysAppCount;
    @BindView(R.id.iv_end)
    ImageView ivEnd;
    @BindView(R.id.tv_end)
    TextView tvEnd;
    @BindView(R.id.rl_end)
    RelativeLayout rlEnd;
    @BindView(R.id.iv_select_all)
    ImageView ivSelectAll;
    @BindView(R.id.tv_select_all)
    TextView tvSelectAll;
    @BindView(R.id.rl_select_all)
    RelativeLayout rlSelectAll;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;

    private AccContract.Presenter mPresenter;
    private ActiveAppAdapter mActiveAppAdapter;
    private int mMinMaskHeight;
    private int mDefaultMaskHeight;
    private int mBottomHeight;

    private LinearLayoutManager mLinearLayoutManager;
    private int mDefaultCountTitleTop;
    private int mCountTitleHeight;
    private int mAppItemHeight;
    private boolean mSelectAll;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc);
        ButterKnife.bind(this);

        mMinMaskHeight = rlTitle.getLayoutParams().height;
        mDefaultMaskHeight = rlTop.getLayoutParams().height;
        mBottomHeight = rlBottom.getLayoutParams().height;
        nsl.init(mMinMaskHeight, mDefaultMaskHeight,mBottomHeight, rlTop);

        AccPresenter.getInstance(AppInfoDataSource.getInstance(this), PhoneStateDataSource.getInstance(this), this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        rvActiveApp.setLayoutManager(mLinearLayoutManager);
        mActiveAppAdapter = new ActiveAppAdapter(this, mPresenter);
        rvActiveApp.setAdapter(mActiveAppAdapter);

        //设置底部按钮的点击事件
        rlEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.killSelectedProcess();
            }
        });
        rlSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectAll) {
                    mSelectAll = false;
                    mPresenter.cacelSelectAll();
                } else {
                    mSelectAll = true;
                    mPresenter.selectAll();
                }
            }
        });
        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.selectLockApp(AccActivity.this);
            }
        });

        mPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
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
                rvActiveApp.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        rvActiveApp.startAnimation(translateAnimation);

        int duration = this.getResources().getInteger(R.integer.defaultSwipDuration);
        rpbPercent.swip(0,0, percent, duration, new RoundProgressBar.OnProgressChangeListener() {
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
        int duration = this.getResources().getInteger(R.integer.defaultSwipDuration);
        rpbPercent.swip(0,rpbPercent.getProgress(), percent, duration, new RoundProgressBar.OnProgressChangeListener() {
            @Override
            public void OnProgressChange(int progress) {
                tvPercent.setText(progress + "");
            }
        });
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
    public void showCountTitle(int totalCount, int userAppCount, int sysAppCount) {
        tvTotalCount.setText(totalCount + "个应用正在后台运行");
        tvUserAppCount.setText("普通应用：" + userAppCount + "个");
        tvSysAppCount.setText("关键应用：" + sysAppCount + "个");
    }

    @Override
    public void showEndBtnEnable(int appCount) {
        ivEnd.setImageResource(R.drawable.end_app);
        tvEnd.setText("一键结束（" + appCount + "）");
        tvEnd.setTextColor(this.getResources().getColor(android.R.color.black));
    }

    @Override
    public void showEndBtnDisable() {
        ivEnd.setImageResource(R.drawable.end_app_highlight);
        tvEnd.setText("一键结束（0）");
        tvEnd.setTextColor(this.getResources().getColor(R.color.colorGrayBlack));
    }

    @Override
    public void showSelectAllBtnEnable() {
        ivSelectAll.setImageResource(R.drawable.select_all_highlight);
        tvSelectAll.setTextColor(this.getResources().getColor(R.color.colorGreen));
    }

    @Override
    public void showSelectAllBtnDisalbe() {
        ivSelectAll.setImageResource(R.drawable.select_all);
        tvSelectAll.setTextColor(this.getResources().getColor(android.R.color.black));
    }

    @Override
    public void showToastTotalClearMemory(int appCount, long memorySize) {
        Toast.makeText(this, "已结束" + appCount + "个应用，释放" + Formatter.formatFileSize(this, memorySize) + "内存", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hideAppList() {
        rvActiveApp.setVisibility(View.INVISIBLE);
    }

    //TODO 该逻辑比较复杂，可考虑简化
    @Override
    public void initCountTitle() {
        nsl.setOnScrollListener(new NestScrollLayout.OnScrollListener() {
            @Override
            public void OnScroll(int y) {
                if (y == mDefaultMaskHeight - mMinMaskHeight) {
                    llTotalCount.setVisibility(View.VISIBLE);
                    llUserAppCount.setVisibility(View.VISIBLE);
                } else {
                    llTotalCount.setVisibility(View.INVISIBLE);
                    llUserAppCount.setVisibility(View.INVISIBLE);
                }
            }
        });

        mDefaultCountTitleTop = llTotalCount.getLayoutParams().height + rlTitle.getLayoutParams().height;
        mCountTitleHeight = llTotalCount.getLayoutParams().height;
        mAppItemHeight = 2 * mCountTitleHeight;
        rvActiveApp.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int mDY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int position = mLinearLayoutManager.findFirstVisibleItemPosition();
                if (mActiveAppAdapter.mUserAppList.size() > 0) {
                    if (position == 1 + mActiveAppAdapter.mUserAppList.size()) {
                        mDY += dy;
                        if (mDY > mAppItemHeight / 2) {
                            llSysAppCount.setVisibility(View.VISIBLE);
                        } else if (mDY < mAppItemHeight / 2) {
                            llSysAppCount.setVisibility(View.INVISIBLE);
                        }
                        int top = llUserAppCount.getTop() - dy;
                        int bottom = llUserAppCount.getBottom() - dy;
                        llUserAppCount.layout(llUserAppCount.getLeft(), top, llUserAppCount.getRight(), bottom);
                    } else if (position == mActiveAppAdapter.mUserAppList.size()) {
                        mDY = 0;
                        llUserAppCount.layout(llUserAppCount.getLeft(), mDefaultCountTitleTop, llUserAppCount.getRight(), mDefaultCountTitleTop + llUserAppCount.getLayoutParams().height);
                        llSysAppCount.setVisibility(View.INVISIBLE);
                    } else if (position > 1 + mActiveAppAdapter.mUserAppList.size()) {
                        mDY = mAppItemHeight;
                        llSysAppCount.setVisibility(View.VISIBLE);
                    } else if (!ViewCompat.canScrollVertically(rvActiveApp, -1)) {
                        llUserAppCount.layout(llUserAppCount.getLeft(), mDefaultCountTitleTop, llUserAppCount.getRight(), mDefaultCountTitleTop + llUserAppCount.getLayoutParams().height);
                    }
                } else {
                    if (!ViewCompat.canScrollVertically(rvActiveApp, -1)) {
                        llUserAppCount.layout(llUserAppCount.getLeft(), mDefaultCountTitleTop, llUserAppCount.getRight(), mDefaultCountTitleTop + llUserAppCount.getLayoutParams().height);
                    } else if (position == 0) {
                        int top = llUserAppCount.getTop() - dy;
                        int bottom = llUserAppCount.getBottom() - dy;
                        llUserAppCount.layout(llUserAppCount.getLeft(), top, llUserAppCount.getRight(), bottom);
                        llSysAppCount.setVisibility(View.INVISIBLE);
                    } else {
                        llSysAppCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }
}
