package com.cs.phoneguardian.main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by Administrator on 2017/7/10.
 * 主页面功能列表适配器
 */

public class FunctionItemAdapter extends RecyclerView.Adapter {

    private Context mContext;
    private String[] mNameList;
    private Integer[] mPictureIdList;
    private OnItemClickedListener mOnItemClickedListener;

    FunctionItemAdapter(Context cxt, String[] nameList, Integer[] pictureIdList) {
        this.mContext = checkNotNull(cxt);
        this.mNameList = checkNotNull(nameList);
        this.mPictureIdList = checkNotNull(pictureIdList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FunctionItemViewHolder(LayoutInflater.from(mContext).inflate(R.layout.function_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        FunctionItemViewHolder fHolder = (FunctionItemViewHolder) holder;
        fHolder.ivFuncPicture.setImageResource(mPictureIdList[position]);
        fHolder.tvFuncName.setText(mNameList[position]);
        final int fPosition = position;
        fHolder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mOnItemClickedListener!=null){
                    mOnItemClickedListener.OnClicke(fPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return Math.min(mNameList.length, mPictureIdList.length);
    }

    class FunctionItemViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_func_picture)
        ImageView ivFuncPicture;
        @BindView(R.id.tv_func_name)
        TextView tvFuncName;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        FunctionItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    interface OnItemClickedListener {
        void OnClicke(int position);
    }

    void setOnItemClickedLisener(OnItemClickedListener lisener) {
        this.mOnItemClickedListener = lisener;
    }
}
