package com.cs.phoneguardian.intercept.view.InterceptContent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;
import com.cs.phoneguardian.intercept.InterceptContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class InterceptFragment extends Fragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    public static final int TYPE_SMS = 0;
    public static final int TYPE_PHONE = 1;

    private int mType = 0;
    private RecyclerView.Adapter mAdapter;
    private InterceptContract.InterceptBasePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intercept, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLinearLayoutManager);
        switch (mType) {
            case TYPE_SMS:
                mAdapter = new InterceptSMSAdapter(getContext(), mPresenter);
                ivBack.setImageResource(R.drawable.text_black);
                tvBack.setText("没有骚扰短信");
                break;

            case TYPE_PHONE:
                mAdapter = new InterceptPhoneAdaper(getContext(), mPresenter);
                ivBack.setImageResource(R.drawable.phone_black);
                tvBack.setText("没有骚扰电话");
                break;
        }
        rvList.setAdapter(mAdapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mType==TYPE_PHONE){
            mPresenter.updatePhoneCall();
        }else if(mType==TYPE_SMS){
            mPresenter.updateSMS();
        }
    }

    public void setType(int type) {
        this.mType = type;
    }

    public void setPresenter(InterceptContract.InterceptBasePresenter presenter) {
        this.mPresenter = presenter;
    }

    public void updateSMSList(List<InterceptSMS> smsList) {
        if (mType == TYPE_SMS) {
            if (smsList.size() == 0) {
                rlBack.setVisibility(View.VISIBLE);
            } else {
                rlBack.setVisibility(View.INVISIBLE);
            }

            ((InterceptSMSAdapter) mAdapter).updateList(smsList);
        }
    }

    public void updatePhoneCallList(List<InterceptPhoneCall> phoneCallList) {
        if (mType == TYPE_PHONE) {
            if (phoneCallList.size() == 0) {
                rlBack.setVisibility(View.VISIBLE);
            } else {
                rlBack.setVisibility(View.INVISIBLE);
            }
            ((InterceptPhoneAdaper) mAdapter).updateList(phoneCallList);
        }
    }
}
