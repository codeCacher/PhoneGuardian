package com.cs.phoneguardian.accelerate;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs.phoneguardian.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEELE on 2017/7/13.
 */

public class ActiveAppAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TOTLE_APP_NUM = 0;
    private static final int TYPE_USER_APP_NUM = 1;
    private static final int TYPE_USER_APP = 2;
    private static final int TYPE_SYS_APP_NUM = 3;
    private static final int TYPE_SYS_APP = 4;

    private Context mContext;
    AccContract.Presenter mPresenter;
    List<AppInfo> mUserAppList;
    List<AppInfo> mSysAppList;

    public ActiveAppAdapter(Context context, AccContract.Presenter presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
        mUserAppList = new ArrayList<>();
        mSysAppList = new ArrayList<>();
    }

    public void updateList(List<AppInfo> mUserAppList, List<AppInfo> mSysAppList) {
        this.mUserAppList = mUserAppList;
        this.mSysAppList = mSysAppList;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TOTLE_APP_NUM;
        } else if (position == 1) {
            return TYPE_USER_APP_NUM;
        } else if (position > 1 && position <= 1 + mUserAppList.size()) {
            return TYPE_USER_APP;
        } else if (position == 2 + mUserAppList.size()) {
            return TYPE_SYS_APP_NUM;
        } else {
            return TYPE_SYS_APP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TOTLE_APP_NUM:
            case TYPE_USER_APP_NUM:
            case TYPE_SYS_APP_NUM:
                return new ActiveAppCountViewHolder(LayoutInflater.from(mContext).inflate(R.layout.active_app_count_item, parent, false));
            case TYPE_USER_APP:
                return new ActiveUserAppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.active_user_app_item, parent, false));
            case TYPE_SYS_APP:
                return new ActiveSysAppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.active_sys_app_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_TOTLE_APP_NUM:
                ActiveAppCountViewHolder totalCountHolder = (ActiveAppCountViewHolder) holder;
                int totalCount = mUserAppList.size() + mSysAppList.size();
                totalCountHolder.tvCount.setText(totalCount + "个应用正在后台运行");
                break;

            case TYPE_USER_APP_NUM:
                ActiveAppCountViewHolder userCountHolder = (ActiveAppCountViewHolder) holder;
                userCountHolder.tvCount.setText("普通应用：" + mUserAppList.size() + "个");
                break;

            case TYPE_SYS_APP_NUM:
                ActiveAppCountViewHolder sysCountHolder = (ActiveAppCountViewHolder) holder;
                sysCountHolder.tvCount.setText("关键应用：" + mSysAppList.size() + "个");
                break;

            case TYPE_USER_APP:
                ActiveUserAppViewHolder userAppHolder = (ActiveUserAppViewHolder) holder;
                userAppHolder.ivIcon.setImageDrawable(mUserAppList.get(position - 2).getIcon());
                userAppHolder.tvName.setText(mUserAppList.get(position - 2).getName());
                userAppHolder.tvMemSize.setText("内存："+Formatter.formatFileSize(mContext,mUserAppList.get(position-2).getMemSize()));
                break;

            case TYPE_SYS_APP:
                ActiveSysAppViewHolder sysAppHolder = (ActiveSysAppViewHolder) holder;
                sysAppHolder.ivIcon.setImageDrawable(mSysAppList.get(position - 3-mUserAppList.size()).getIcon());
                sysAppHolder.tvName.setText(mSysAppList.get(position - 3-mUserAppList.size()).getName());
                sysAppHolder.tvMemSize.setText("内存："+Formatter.formatFileSize(mContext,mSysAppList.get(position - 3-mUserAppList.size()).getMemSize()));
                break;
        }

    }

    @Override
    public int getItemCount() {
        return 3 + mUserAppList.size() + mSysAppList.size();
    }

    class ActiveAppCountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_count)
        TextView tvCount;

        ActiveAppCountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ActiveUserAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_mem_size)
        TextView tvMemSize;
        @BindView(R.id.iv_check_state)
        ImageView ivCheckState;

        ActiveUserAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ActiveSysAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_icon)
        ImageView ivIcon;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_mem_size)
        TextView tvMemSize;
        @BindView(R.id.iv_detail)
        ImageView ivDetail;

        ActiveSysAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
