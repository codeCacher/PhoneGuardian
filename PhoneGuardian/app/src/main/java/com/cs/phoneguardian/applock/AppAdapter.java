package com.cs.phoneguardian.applock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEELE on 2017/7/13.
 */

public class AppAdapter extends RecyclerView.Adapter {

    private Context mContext;
    AppLockContract.Presenter mPresenter;
    List<AppInfo> mAppList;
    private OnItemClickedListener mOnItemClickedListener;

    public AppAdapter(Context context, AppLockContract.Presenter presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
        mAppList = new ArrayList<>();
    }

    public void updateList(List<AppInfo> mAppList) {
        this.mAppList = mAppList;
        this.notifyDataSetChanged();
    }

    public interface OnItemClickedListener {
        void OnItemClicked(int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.mOnItemClickedListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AppLockViewHolder(LayoutInflater.from(mContext).inflate(R.layout.active_user_app_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);

        final AppLockViewHolder appLockViewHolder = (AppLockViewHolder) holder;
        final AppInfo info = mAppList.get(position);
        appLockViewHolder.ivIcon.setImageDrawable(info.getIcon());
        appLockViewHolder.tvName.setText(info.getName());
        if (info.isAppLock()) {
            appLockViewHolder.tvLockState.setText("已锁定");
            appLockViewHolder.scCheckState.setChecked(true);
            appLockViewHolder.ivLock.setVisibility(View.VISIBLE);
        } else {
            appLockViewHolder.tvLockState.setText("未锁定");
            appLockViewHolder.scCheckState.setChecked(false);
            appLockViewHolder.ivLock.setVisibility(View.INVISIBLE);
        }

        //设置点击事件
        final int fPosition = position;
        appLockViewHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (info.isAppLock()) {
                    info.setAppLock(false);
                    appLockViewHolder.tvLockState.setText("未锁定");
                    appLockViewHolder.scCheckState.setChecked(false);
                    appLockViewHolder.ivLock.setVisibility(View.INVISIBLE);
                    mPresenter.removeLockApp(info);
                } else {
                    info.setAppLock(true);
                    appLockViewHolder.tvLockState.setText("已锁定");
                    appLockViewHolder.scCheckState.setChecked(true);
                    appLockViewHolder.ivLock.setVisibility(View.VISIBLE);
                    mPresenter.addLockApp(info);
                }

                if (mOnItemClickedListener != null) {
                    mOnItemClickedListener.OnItemClicked(fPosition);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return 1 + mAppList.size();
    }

    class AppLockViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_lock)
        ImageView ivLock;
        @BindView(R.id.tv_lock_state)
        TextView tvLockState;
        @BindView(R.id.sc_check_state)
        SwitchCompat scCheckState;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        AppLockViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
