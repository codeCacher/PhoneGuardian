package com.cs.phoneguardian.accelerate;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.modle.AppInfoDataSource;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cs.phoneguardian.accelerate.AccContract.SettingPreseter;
import static com.cs.phoneguardian.accelerate.AccContract.SettingView;

/**
 * Created by Administrator on 2017/7/18.
 */

public class SelectLockAppActivity extends AppCompatActivity implements SettingView {
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_count)
    TextView tvCount;
    @BindView(R.id.rv_app)
    RecyclerView rvApp;
    @BindView(R.id.iv_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_menu)
    TextView tvMenu;
    @BindView(R.id.rl_menu)
    RelativeLayout rlMenu;
    private SettingPreseter mPresenter;
    private UserAppAdapter mUserAppAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lock_app);
        ButterKnife.bind(this);

        SelectLockAppPresenter.getInstance(AppInfoDataSource.getInstance(this), this);

        mLinearLayoutManager = new LinearLayoutManager(this);
        rvApp.setLayoutManager(mLinearLayoutManager);
        mUserAppAdapter = new UserAppAdapter(this);
        rvApp.setAdapter(mUserAppAdapter);

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog dialog = new AlertDialog.Builder(SelectLockAppActivity.this).create();
                View view = View.inflate(SelectLockAppActivity.this, R.layout.select_all_dialog, null);
                View tvAll = view.findViewById(R.id.tv_all);
                View tvCacelAll = view.findViewById(R.id.tv_cancel_all);
                tvAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mPresenter.selectAll();
                    }
                });
                tvCacelAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        mPresenter.cancelAll();
                    }
                });
                dialog.setView(view);
                dialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.start();
    }

    @Override
    public void setPresenter(SettingPreseter presenter) {
        this.mPresenter = presenter;
    }

    public static void startSelectLockAppActivity(Context context) {
        Intent intent = new Intent(context, SelectLockAppActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void upDateAppList(List<AppInfo> userAppList) {
        mUserAppAdapter.updateUserAppList(userAppList);
    }

    @Override
    public void showLockAppCount(int count) {
        tvCount.setText("锁屏后，" + count + "个应用保持运行");
    }

    /**
     * 适配器
     */
    class UserAppAdapter extends RecyclerView.Adapter {
        List<AppInfo> userAppList;
        Context mContext;

        UserAppAdapter(Context context) {
            userAppList = new ArrayList<>();
            this.mContext = context;
        }

        public void updateUserAppList(List<AppInfo> list) {
            this.userAppList = list;
            this.notifyDataSetChanged();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new UserAppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.select_lock_app_item, parent, false));
        }

        private void setLockState(UserAppViewHolder holder) {
            holder.tvState.setText("已保护");
            holder.ivLock.setVisibility(View.VISIBLE);
            holder.scState.setChecked(true);
        }

        private void setUnLockState(UserAppViewHolder holder) {
            holder.tvState.setText("未保护");
            holder.ivLock.setVisibility(View.INVISIBLE);
            holder.scState.setChecked(false);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            final UserAppViewHolder userAppHolder = (UserAppViewHolder) holder;
            userAppHolder.ivIcon.setImageDrawable(userAppList.get(position).getIcon());
            userAppHolder.tvName.setText(userAppList.get(position).getName());
            if (userAppList.get(position).isLock()) {
                setLockState(userAppHolder);
            } else {
                setUnLockState(userAppHolder);
            }

            //设置点击事件
            final int fPosition = position;
            userAppHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AppInfo appInfo = userAppList.get(fPosition);
                    if (appInfo.isLock()) {
                        appInfo.setLock(false);
                        setUnLockState(userAppHolder);
                        mPresenter.removeLockApp(userAppList.get(fPosition));
                    } else {
                        appInfo.setLock(true);
                        setLockState(userAppHolder);
                        mPresenter.addLockApp(userAppList.get(fPosition));
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return userAppList.size();
        }

        class UserAppViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.iv_icon)
            ImageView ivIcon;
            @BindView(R.id.tv_name)
            TextView tvName;
            @BindView(R.id.iv_lock)
            ImageView ivLock;
            @BindView(R.id.tv_state)
            TextView tvState;
            @BindView(R.id.sc_state)
            SwitchCompat scState;
            @BindView(R.id.rl_root)
            RelativeLayout rlRoot;

            public UserAppViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
