package com.cs.phoneguardian.intercept.presenter;

import android.content.Context;

import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;
import com.cs.phoneguardian.intercept.InterceptContract;
import com.cs.phoneguardian.intercept.modle.InterceptDataSource;
import com.cs.phoneguardian.utils.DialogUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/7/23.
 */

public class InterceptPresenter implements InterceptContract.Presenter {

    private final InterceptContract.View mInterceptView;
    private final InterceptDataSource mDataSource;
    private final Context mContext;
    private List<InterceptSMS> mSMSList;
    private List<InterceptPhoneCall> mPhoneCallList;

    private InterceptPresenter(Context context, InterceptContract.View interceptView, InterceptDataSource dataSource) {
        this.mContext = context;
        this.mInterceptView = interceptView;
        this.mDataSource = dataSource;
        interceptView.setPresenter(this);
    }

    public static InterceptPresenter getInstance(Context context, InterceptContract.View interceptView, InterceptDataSource dataSource) {
        return new InterceptPresenter(context,interceptView, dataSource);
    }

    @Override
    public void start() {

    }

    @Override
    public void selectInterceptSms() {
        mInterceptView.showInterceptSms();
        if(mSMSList.size()==0){
            mInterceptView.disableDeleteAllBtn();
        }else {
            mInterceptView.enableDeleteAllBtn();
        }
    }

    @Override
    public void selectInterceptPhone() {
        mInterceptView.showInterceptPhone();
        if(mPhoneCallList.size()==0){
            mInterceptView.disableDeleteAllBtn();
        }else {
            mInterceptView.enableDeleteAllBtn();
        }
    }

    @Override
    public void selectSMS(int position) {

    }

    @Override
    public void selectPhoneCall(int position) {

    }

    @Override
    public void deletAllSMS() {
        DialogUtils.showConfirmDialog(mContext, "是否删除所有被拦截信息？", new DialogUtils.OnButtonClickedListener() {
            @Override
            public void OnConfirm() {
                mSMSList.clear();
                mInterceptView.updateSMSList(mSMSList);
                mDataSource.deleteAllInterceptSMS();
            }
            @Override
            public void OnCancel() {}
        });
    }

    @Override
    public void deletAllPhoneCall() {
        DialogUtils.showConfirmDialog(mContext, "是否删除所有被拦截来电？", new DialogUtils.OnButtonClickedListener() {
            @Override
            public void OnConfirm() {
                mPhoneCallList.clear();
                mInterceptView.updatePhoneCallList(mPhoneCallList);
                mDataSource.deleteAllInterceptPhoneCall();
            }
            @Override
            public void OnCancel() {}
        });
    }

    @Override
    public void updateSMS() {
        mSMSList = mDataSource.getInterceptSMSList();
        mInterceptView.updateSMSList(mSMSList);
        if(mSMSList.size()==0){
            mInterceptView.disableDeleteAllBtn();
        }else {
            mInterceptView.enableDeleteAllBtn();
        }
    }

    @Override
    public void updatePhoneCall() {
        mPhoneCallList = mDataSource.getInterceptPhoneCallList();
        mInterceptView.updatePhoneCallList(mPhoneCallList);
        if(mPhoneCallList.size()==0){
            mInterceptView.disableDeleteAllBtn();
        }else {
            mInterceptView.enableDeleteAllBtn();
        }
    }
}
