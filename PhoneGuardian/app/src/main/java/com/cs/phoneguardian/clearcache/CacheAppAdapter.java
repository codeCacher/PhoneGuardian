package com.cs.phoneguardian.clearcache;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.AppInfo;
import com.cs.phoneguardian.utils.CustomActivityJumpUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SEELE on 2017/7/13.
 */

public class CacheAppAdapter extends RecyclerView.Adapter {

    private static final int TYPE_TITLE = 0;
    private static final int TYPE_APP = 1;

    private Context mContext;
    ClearCacheContract.Presenter mPresenter;
    List<AppInfo> mCacheAppList;
    private OnItemClickedListener mOnItemClickedListener;
    private boolean mListStateSpread;

    public CacheAppAdapter(Context context, ClearCacheContract.Presenter presenter) {
        this.mContext = context;
        this.mPresenter = presenter;
        mCacheAppList = new ArrayList<>();
        mListStateSpread = true;
    }

    public void updateList(List<AppInfo> mCacheAppList) {
        this.mCacheAppList = mCacheAppList;
        this.notifyDataSetChanged();
    }

    public interface OnItemClickedListener {
        void OnItemClicked(int position);
    }

    public void setOnItemClickedListener(OnItemClickedListener listener) {
        this.mOnItemClickedListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_TITLE;
        } else {
            return TYPE_APP;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_TITLE:
                return new CacheAppCountViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cache_app_count_item, parent, false));
            case TYPE_APP:
                return new CacheAppViewHolder(LayoutInflater.from(mContext).inflate(R.layout.cache_app_item, parent, false));
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        switch (itemViewType) {
            case TYPE_TITLE:
                CacheAppCountViewHolder totalCountHolder = (CacheAppCountViewHolder) holder;
                int totalCount = mCacheAppList.size();
                totalCountHolder.tvCount.setText("应用缓存（" + totalCount + "）");
                //设置点击事件
                totalCountHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mListStateSpread = !mListStateSpread;
                        notifyDataSetChanged();
                    }
                });
                break;

            case TYPE_APP:
                final int fPosition = position - 1;
                CacheAppViewHolder cacheAppViewHolder = (CacheAppViewHolder) holder;
                cacheAppViewHolder.tvName.setText(mCacheAppList.get(fPosition).getName());
                String strCacheSize = Formatter.formatFileSize(mContext, mCacheAppList.get(fPosition).getCacheSize());
                cacheAppViewHolder.tvCacheSize.setText("缓存大小：" + strCacheSize + "");

                //设置点击事件
                cacheAppViewHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AppInfo appInfo = mCacheAppList.get(fPosition);
                        CustomActivityJumpUtils.startAppInfoActivity(mContext, appInfo.getPackageName());
                        if (mOnItemClickedListener != null) {
                            mOnItemClickedListener.OnItemClicked(fPosition);
                        }

                    }
                });

                break;
        }

    }

    @Override
    public int getItemCount() {
        if(mListStateSpread){
            return 1 + mCacheAppList.size();
        }else {
            return 1;
        }
    }

    class CacheAppCountViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_count)
        TextView tvCount;
        @BindView(R.id.iv_more)
        ImageView ivMore;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        CacheAppCountViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class CacheAppViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_cache_size)
        TextView tvCacheSize;
        @BindView(R.id.iv_detail)
        ImageView ivDetail;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        CacheAppViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
