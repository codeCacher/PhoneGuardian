package com.cs.phoneguardian.intercept.view.InterceptContent;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.intercept.InterceptContract;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class InterceptPhoneAdaper extends RecyclerView.Adapter<InterceptPhoneAdaper.PhoneViewHolder> {

    private Context mContext;
    InterceptContract.InterceptBasePresenter mPresenter;
    List<InterceptPhoneCall> mPhoneCallList;

    public InterceptPhoneAdaper(Context context, InterceptContract.InterceptBasePresenter presenter) {
        mPhoneCallList = new ArrayList<>();
        this.mContext = context;
        this.mPresenter = presenter;
    }

    public void updateList(List<InterceptPhoneCall> list) {
        this.mPhoneCallList = list;
        notifyDataSetChanged();
    }

    @Override
    public PhoneViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new PhoneViewHolder(LayoutInflater.from(mContext).inflate(R.layout.intercept_item, parent, false));
    }

    @Override
    public void onBindViewHolder(PhoneViewHolder holder, int position) {
        InterceptPhoneCall phoneCall = mPhoneCallList.get(position);
        String name = phoneCall.getName();
        String phoneNumber = phoneCall.getPhoneNumber();
        Timestamp timestamp = phoneCall.getTime();
        int month = timestamp.getMonth()+1;
        int day = timestamp.getDate();

        holder.tvName.setText(name);
        holder.tvText.setText(phoneNumber);
        holder.tvDate.setText(month + "/" + day);
        final int fPosition = position;
        holder.rlRoot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.selectPhoneCall(fPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mPhoneCallList.size();
    }

    class PhoneViewHolder extends RecyclerView.ViewHolder {
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

        public PhoneViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
