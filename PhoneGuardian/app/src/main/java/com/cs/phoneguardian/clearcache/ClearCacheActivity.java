package com.cs.phoneguardian.clearcache;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.modle.PhoneStateDataSource;
import com.cs.phoneguardian.view.NestScrollLayout;
import com.cs.phoneguardian.view.RoundProgressBar;
import com.cs.phoneguardian.view.ScrollViewChild;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ClearCacheActivity extends AppCompatActivity implements ClearCacheContract.View {
    @BindView(R.id.bt_scan)
    Button btScan;
    @BindView(R.id.rpb_phone_percent)
    RoundProgressBar rpbPhonePercent;
    @BindView(R.id.rl_phone_percent)
    RelativeLayout rlPhonePercent;
    @BindView(R.id.tv_phone_percent)
    TextView tvPhonePercent;
    @BindView(R.id.tv_phone_size)
    TextView tvPhoneSize;
    @BindView(R.id.rpb_sd_percent)
    RoundProgressBar rpbSdPercent;
    @BindView(R.id.rl_sd_percent)
    RelativeLayout rlSdPercent;
    @BindView(R.id.tv_sd_percent)
    TextView tvSdPercent;
    @BindView(R.id.tv_sd_size)
    TextView tvSdSize;
    @BindView(R.id.rl_top)
    RelativeLayout rlTop;
    @BindView(R.id.mask_id)
    View maskId;
    @BindView(R.id.child_id)
    ScrollViewChild childId;
    @BindView(R.id.nsl)
    NestScrollLayout nsl;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tv_scan_state)
    TextView tvScanState;
    @BindView(R.id.rv_app)
    RecyclerView rvApp;

    private final static int BTN_STATE_SCAN = 0;
    private final static int BTN_STATE_CANCEL = 1;
    private final static int BTN_STATE_CLEAN = 2;
    private static final int BTN_STATE_RESCAN = 3;

    private ClearCacheContract.Presenter mPresenter;
    private int btnState = BTN_STATE_SCAN;
    private LinearLayoutManager mLinearLayoutManager;
    private CacheAppAdapter mCacheAppAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clear_cache);
        ButterKnife.bind(this);

        ClearCachePresenter.getInstance(this, AppInfoDataSource.getInstance(this), PhoneStateDataSource.getInstance(this), this);

        int mMinMaskHeight = rlTitle.getLayoutParams().height;
        int mDefaultMaskHeight = rlTop.getLayoutParams().height;
        int mBottomHeight = rlBottom.getLayoutParams().height;
        nsl.init(mMinMaskHeight, mDefaultMaskHeight, mBottomHeight, rlTop);

        mLinearLayoutManager = new LinearLayoutManager(this);
        rvApp.setLayoutManager(mLinearLayoutManager);
        mCacheAppAdapter = new CacheAppAdapter(this, mPresenter);
        rvApp.setAdapter(mCacheAppAdapter);


        btScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (btnState) {
                    case BTN_STATE_SCAN:
                    case BTN_STATE_RESCAN:
                        mPresenter.startScan(ClearCacheActivity.this, new ClearCacheContract.Presenter.OnScanCompleteListener() {
                            @Override
                            public void OnComplete() {
                                mPresenter.completeScan();
                            }
                        });
                        break;

                    case BTN_STATE_CANCEL:
                        mPresenter.start();
                        break;

                    case BTN_STATE_CLEAN:
                        mPresenter.clean(ClearCacheActivity.this);
                        break;
                }
            }
        });

        mPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(btnState==BTN_STATE_SCAN){
            super.onBackPressed();
        }else {
            mPresenter.start();
        }
    }

    public static void startClearCacheActivity(Context context) {
        Intent intent = new Intent(context, ClearCacheActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setPresenter(ClearCacheContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showPhoneMemSize(long usedSize, long totalSize) {
        String strUsed = Formatter.formatFileSize(this, usedSize);
        String strTotal = Formatter.formatFileSize(this, totalSize);
        tvPhoneSize.setText(strUsed + "/" + strTotal);
    }

    @Override
    public void showSDMemsize(long usedSize, long totalSize) {
        String strUsed = Formatter.formatFileSize(this, usedSize);
        String strTotal = Formatter.formatFileSize(this, totalSize);
        tvSdSize.setText(strUsed + "/" + strTotal);
    }

    @Override
    public void showPhoneMemPercent(int percent) {
        rpbPhonePercent.setProgress(percent);
    }

    @Override
    public void showSDMemPercent(int percent) {
        rpbSdPercent.setProgress(percent);
    }

    @Override
    public void initRoundProgressColor(int index, int phonePercent, int sdPercent) {
        if (phonePercent < 60) {
            if (index == 0) {
                rpbPhonePercent.setRoundProgressColor(Color.GREEN);
                rpbPhonePercent.setProgressDotColor(Color.GREEN);
            } else {
                rpbPhonePercent.setSecondRoundProgressColor(Color.GREEN);
                rpbPhonePercent.setSecondProgressDotColor(Color.GREEN);
            }
        } else if (phonePercent < 90) {
            if (index == 0) {
                rpbPhonePercent.setRoundProgressColor(this.getResources().getColor(R.color.colorOrange));
                rpbPhonePercent.setProgressDotColor(this.getResources().getColor(R.color.colorOrange));
            } else {
                rpbPhonePercent.setSecondRoundProgressColor(this.getResources().getColor(R.color.colorOrange));
                rpbPhonePercent.setSecondProgressDotColor(this.getResources().getColor(R.color.colorOrange));
            }
        } else {
            if (index == 0) {
                rpbPhonePercent.setRoundProgressColor(Color.RED);
                rpbPhonePercent.setProgressDotColor(Color.RED);
            } else {
                rpbPhonePercent.setSecondRoundProgressColor(Color.RED);
                rpbPhonePercent.setSecondProgressDotColor(Color.RED);
            }
        }

        if (sdPercent < 60) {
            if (index == 0) {
                rpbSdPercent.setRoundProgressColor(Color.GREEN);
                rpbSdPercent.setProgressDotColor(Color.GREEN);
            } else {
                rpbSdPercent.setSecondRoundProgressColor(Color.GREEN);
                rpbSdPercent.setSecondProgressDotColor(Color.GREEN);
            }
        } else if (sdPercent < 90) {
            if (index == 0) {
                rpbSdPercent.setRoundProgressColor(this.getResources().getColor(R.color.colorOrange));
                rpbSdPercent.setProgressDotColor(this.getResources().getColor(R.color.colorOrange));
            } else {
                rpbSdPercent.setSecondRoundProgressColor(this.getResources().getColor(R.color.colorOrange));
                rpbSdPercent.setSecondProgressDotColor(this.getResources().getColor(R.color.colorOrange));
            }
        } else {
            if (index == 0) {
                rpbSdPercent.setRoundProgressColor(Color.RED);
                rpbSdPercent.setProgressDotColor(Color.RED);
            } else {
                rpbSdPercent.setSecondRoundProgressColor(Color.RED);
                rpbSdPercent.setSecondProgressDotColor(Color.RED);
            }
        }
    }

    @Override
    public void showStartAnimation(int phoneMemPercent, int SDMemPercent) {
        int duration = ClearCacheActivity.this.getResources().getInteger(R.integer.defaultSwipDuration);
        rpbPhonePercent.swip(0, 0, phoneMemPercent, duration, null);
        rpbSdPercent.swip(0, 0, SDMemPercent, duration, null);
    }

    @Override
    public void initStartState() {
        rpbPhonePercent.init();
        rpbSdPercent.init();
        rpbPhonePercent.setProgressDotVisible(true);
        rpbSdPercent.setProgressDotVisible(true);

        nsl.setVisibility(View.VISIBLE);
        rlBottom.setVisibility(View.VISIBLE);
        tvPhoneSize.setVisibility(View.VISIBLE);
        tvPhonePercent.setVisibility(View.VISIBLE);
        tvSdSize.setVisibility(View.VISIBLE);
        tvSdPercent.setVisibility(View.VISIBLE);

        tvScanState.setVisibility(View.INVISIBLE);
        btScan.setText("智能扫描");
        btnState = BTN_STATE_SCAN;
    }

    @Override
    public void initScanState() {
        rpbPhonePercent.init();
        rpbSdPercent.init();
        rpbPhonePercent.setProgressDotVisible(true);
        rpbSdPercent.setProgressDotVisible(true);

        int duration = ClearCacheActivity.this.getResources().getInteger(R.integer.defaultSwipDuration);
        rpbPhonePercent.turn(duration, Color.WHITE);
        rpbSdPercent.turn(duration, Color.WHITE);

        nsl.setVisibility(View.GONE);
        rlBottom.setVisibility(View.GONE);

        tvPhoneSize.setVisibility(View.INVISIBLE);
        tvPhonePercent.setVisibility(View.INVISIBLE);
        tvSdSize.setVisibility(View.INVISIBLE);
        tvSdPercent.setVisibility(View.INVISIBLE);

        tvScanState.setVisibility(View.VISIBLE);
        btScan.setText("取消扫描");
        btnState = BTN_STATE_CANCEL;
    }

    @Override
    public void updateScanState(final String packageName) {
        tvScanState.setText("正在扫描：" + packageName);
    }

    @Override
    public void showScanFinishState(final long gabbageSize, final int oldPhonePercent, final int newPhonePercent, final int oldSDPercent, final int newSDPercent) {
        String strGabbage = Formatter.formatFileSize(ClearCacheActivity.this, gabbageSize);
        tvScanState.setText("共发现" + strGabbage + "垃圾可清理");
        btScan.setText("一键清理");
        btnState = BTN_STATE_CLEAN;

        rpbPhonePercent.init();
        rpbSdPercent.init();
        rpbPhonePercent.setProgressDotVisible(true);
        rpbSdPercent.setProgressDotVisible(true);

        initRoundProgressColor(0, newPhonePercent, newSDPercent);
        initRoundProgressColor(1, newPhonePercent, newSDPercent);
        int duration = ClearCacheActivity.this.getResources().getInteger(R.integer.defaultSwipDuration);
        rpbPhonePercent.swip(0, 0, newPhonePercent, duration, new RoundProgressBar.OnProgressChangeListener() {
            @Override
            public void OnProgressChange(int progress) {
                if (progress == newPhonePercent) {
                    rpbPhonePercent.setSecondProgressDotVisible(false);
                    rpbPhonePercent.setSecondProgress(newPhonePercent);
                    rpbPhonePercent.setRoundProgressColor(Color.WHITE);
                    rpbPhonePercent.setProgressDotColor(Color.WHITE);
                    rpbPhonePercent.setProgress(oldPhonePercent);
                }
            }
        });

        rpbSdPercent.swip(0, 0, newSDPercent, duration, new RoundProgressBar.OnProgressChangeListener() {
            @Override
            public void OnProgressChange(int progress) {
                if (progress == newSDPercent) {
                    rpbSdPercent.setSecondProgressDotVisible(false);
                    rpbSdPercent.setSecondProgress(newSDPercent);
                    rpbSdPercent.setRoundProgressColor(Color.WHITE);
                    rpbSdPercent.setProgressDotColor(Color.WHITE);
                    rpbSdPercent.setProgress(oldSDPercent);
                }
            }
        });
    }

    @Override
    public void updateAppList(final List<AppInfo> appInfo) {
        mCacheAppAdapter.updateList(appInfo);
    }

    @Override
    public void showFinishClean(long allCacheSize, int oldPhonePercent, final int newPhonePercent, int oldSDPercent, final int newSDPercent) {
        int duration = ClearCacheActivity.this.getResources().getInteger(R.integer.defaultSwipDuration);
        rpbPhonePercent.swip(0, oldPhonePercent, newPhonePercent, duration, new RoundProgressBar.OnProgressChangeListener() {
            @Override
            public void OnProgressChange(int progress) {
                if(progress==newPhonePercent){
                    rpbPhonePercent.setProgressDotColor(rpbPhonePercent.getSecondRoundProgressColor());
                }
            }
        });
        rpbSdPercent.swip(0, oldSDPercent, newSDPercent, duration, new RoundProgressBar.OnProgressChangeListener() {
            @Override
            public void OnProgressChange(int progress) {
                if(progress == newSDPercent){
                    rpbSdPercent.setProgressDotColor(rpbSdPercent.getSecondRoundProgressColor());
                }
            }
        });
        String strCacheSize = Formatter.formatFileSize(this, allCacheSize);
        tvScanState.setText("清理完成，共清理了"+ strCacheSize+"垃圾");
        btScan.setText("重新扫描");
        btnState = BTN_STATE_RESCAN;
    }
}
