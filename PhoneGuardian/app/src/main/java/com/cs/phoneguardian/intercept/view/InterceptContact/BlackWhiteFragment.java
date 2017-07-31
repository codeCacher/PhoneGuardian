package com.cs.phoneguardian.intercept.view.InterceptContact;

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
import com.cs.phoneguardian.bean.InterceptContact;
import com.cs.phoneguardian.intercept.InterceptContract;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/7/23.
 */

public class BlackWhiteFragment extends Fragment {
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_back)
    TextView tvBack;
    @BindView(R.id.rl_back)
    RelativeLayout rlBack;

    public static final int TYPE_BLACK = 0;
    public static final int TYPE_WHITE = 1;

    private int mType = 0;
    private ContactAdapter mAdapter;
    private InterceptContract.BlackWhiteBasePresenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_intercept, container, false);
        ButterKnife.bind(this, view);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        rvList.setLayoutManager(mLinearLayoutManager);
        mAdapter = new ContactAdapter(getContext(), mPresenter,mType);
        rvList.setAdapter(mAdapter);

        switch (mType) {
            case TYPE_BLACK:
                ivBack.setImageResource(R.drawable.black_back);
                tvBack.setText("没有黑名单号码");
                break;

            case TYPE_WHITE:
                ivBack.setImageResource(R.drawable.white_back);
                tvBack.setText("没有白名单号码");
                break;
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mType== TYPE_WHITE){
            mPresenter.updateWhite();
        }else if(mType== TYPE_BLACK){
            mPresenter.updateBlack();
        }
    }

    public void setType(int type) {
        this.mType = type;
    }

    public void setPresenter(InterceptContract.BlackWhiteBasePresenter presenter) {
        this.mPresenter = presenter;
    }

    public void updateBlackList(List<InterceptContact> contactList) {
        if (mType == TYPE_BLACK) {
            if (contactList.size() == 0) {
                rlBack.setVisibility(View.VISIBLE);
            } else {
                rlBack.setVisibility(View.INVISIBLE);
            }

            mAdapter.updateList(contactList);
        }
    }

    public void updateWhiteList(List<InterceptContact> contactList) {
        if (mType == TYPE_WHITE) {
            if (contactList.size() == 0) {
                rlBack.setVisibility(View.VISIBLE);
            } else {
                rlBack.setVisibility(View.INVISIBLE);
            }
            mAdapter.updateList(contactList);
        }
    }
}
