package com.cs.phoneguardian.applock;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.modle.AppInfoDataSource;
import com.cs.phoneguardian.utils.DialogUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cs.phoneguardian.applock.AppLockPasswordSettingActivity.RESULT_CANCEL;
import static com.cs.phoneguardian.applock.AppLockPasswordSettingActivity.RESULT_FINISH;

/**
 * Created by Administrator on 2017/7/22.
 */

public class AppLockActivity extends AppCompatActivity implements AppLockContract.View {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.rv_app)
    RecyclerView rvApp;
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.tv_setting)
    TextView tvSetting;
    @BindView(R.id.rl_setting)
    RelativeLayout rlSetting;
    @BindView(R.id.rl_bottom)
    RelativeLayout rlBottom;
    @BindView(R.id.tv_count)
    TextView tvCount;

    public final static int REQUEST_CODE_SET_PSD = 0;
    public final static int REQUEST_CODE_USAGE_ACCESS = 1;

    private AppLockContract.Presenter mPresenter;
    private LinearLayoutManager mLinearLayoutManager;
    private AppAdapter mAppAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_lock);
        ButterKnife.bind(this);

        AppLockPresenter.getInstance(this,AppInfoDataSource.getInstance(this), this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        rvApp.setLayoutManager(mLinearLayoutManager);
        mAppAdapter = new AppAdapter(this, mPresenter);
        rvApp.setAdapter(mAppAdapter);

        rlSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLockSettingActivity.startAppLockSettingActivity(AppLockActivity.this);
            }
        });

        mPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_CODE_SET_PSD:
                if(resultCode==RESULT_CANCEL){
                    this.finish();
                }else if(resultCode==RESULT_FINISH){
                    mPresenter.openAppLock(this);
                    startService(new Intent(this,AppLockService.class));
                }
                break;

            case REQUEST_CODE_USAGE_ACCESS:
                mPresenter.onSetUsageAccessResult();
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static void startAppLockActivity(Context context) {
        Intent intent = new Intent(context, AppLockActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void setPresenter(AppLockContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void upDateAppList(List<AppInfo> list) {
        mAppAdapter.updateList(list);
    }

    @Override
    public void showLockAppCount(int count) {
        tvCount.setText(count+"个应用已锁定");
    }

    @Override
    public void showPassWordSettingActivity() {
        AppLockPasswordSettingActivity.startAppLockPsdSetActivityForResult(this,this, REQUEST_CODE_SET_PSD);
    }

    @Override
    public void showPassWordDialog(String correctPsd) {
        DialogUtils.showPasswordDialog(this, correctPsd, new DialogUtils.OnButtonClickedListener() {
            @Override
            public void OnConfirm() {

            }

            @Override
            public void OnCancel() {
                AppLockActivity.this.finish();
            }
        });
    }
}
