package com.cs.phoneguardian.intercept.presenter;

import android.content.Context;

import com.cs.phoneguardian.bean.InterceptContact;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;
import com.cs.phoneguardian.intercept.InterceptContract;
import com.cs.phoneguardian.intercept.modle.InterceptDataSource;
import com.cs.phoneguardian.intercept.modle.InterceptPersistenceContract;
import com.cs.phoneguardian.utils.DialogUtils;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

/**
 * Created by Administrator on 2017/7/23.
 */

public class BlackWhitePresenter implements InterceptContract.BlackWhiteBasePresenter {

    private final InterceptContract.BlackWhiteBaseView mView;
    private final InterceptDataSource mDataSource;
    private final Context mContext;
    private List<InterceptContact> mBlackList;
    private List<InterceptContact> mWhiteList;

    private BlackWhitePresenter(Context context, InterceptContract.BlackWhiteBaseView blackWhiteBaseView, InterceptDataSource dataSource) {
        this.mContext = context;
        this.mView = blackWhiteBaseView;
        this.mDataSource = dataSource;
        mBlackList =new ArrayList<>();
        mWhiteList = new ArrayList<>();
        blackWhiteBaseView.setPresenter(this);
    }

    public static BlackWhitePresenter getInstance(Context context, InterceptContract.BlackWhiteBaseView blackWhiteBaseView, InterceptDataSource dataSource) {
        return new BlackWhitePresenter(context,blackWhiteBaseView, dataSource);
    }

    @Override
    public void start() {

    }

    @Override
    public void init(int state) {
        if(state == InterceptContract.BlackWhiteBaseView.BLACK_STATE){
            mView.showBlack();
        }else {
            mView.showWhite();
        }
    }

    @Override
    public void addBlackContact() {

    }

    @Override
    public void addWhiteContact() {

    }

    @Override
    public void selectBlackContact(int position) {
        mView.showBlackDeleteDialog(mBlackList.get(position));
    }

    @Override
    public void selectWhiteContact(int position) {
        mView.showWhiteDeleteDialog(mWhiteList.get(position));
    }

    @Override
    public void updateBlack() {
        mBlackList = mDataSource.getInterceptContactList(InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE_ALL);
        mView.updateBlackList(mBlackList);
    }

    @Override
    public void updateWhite() {
        mWhiteList = mDataSource.getInterceptContactList(InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE_NONE);
        mView.updateWhiteList(mWhiteList);
    }

    @Override
    public void deleteContact(String number) {
        mDataSource.deleteInterceptContact(number);
    }
}
