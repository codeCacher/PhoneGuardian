package com.cs.phoneguardian.intercept.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.InterceptSMS;
import com.cs.phoneguardian.intercept.InterceptContract;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class InterceptSMSAdapter extends RecyclerView.Adapter<InterceptSMSAdapter.SMSViewHolder> {

    private Context mContext;
    InterceptContract.Presenter mPresenter;
    List<InterceptSMS> mSMSList;

    public InterceptSMSAdapter(Context context, InterceptContract.Presenter presenter) {
        mSMSList = new ArrayList<>();
        this.mContext = context;
        this.mPresenter = presenter;
    }

    public void updateList(List<InterceptSMS> list){
        this.mSMSList = list;
        notifyDataSetChanged();
    }

    @Override
    public SMSViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SMSViewHolder(LayoutInflater.from(mContext).inflate(R.layout.intercept_item, parent, false));
    }

    @Override
    public void onBindViewHolder(SMSViewHolder holder, int position) {
        InterceptSMS sms = mSMSList.get(position);
        String name = sms.getName();
        String content = sms.getContent();
        Timestamp timestamp = sms.getTime();
        int month = timestamp.getMonth()+1;
        int day = timestamp.getDate();

        holder.tvName.setText(name);
        holder.tvText.setText(content);
        holder.tvDate.setText(month+"/"+day);
        final int fPosition = position;
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.selectSMS(fPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSMSList.size();
    }

    class SMSViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.v_vertical)
        View vVertical;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_text)
        TextView tvText;
        @BindView(R.id.rl_root)
        RelativeLayout rlRoot;

        public SMSViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
