package com.cs.phoneguardian.intercept.presenter;

import android.content.Context;

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

/**
 * Created by Administrator on 2017/7/23.
 */

public class InterceptPresenter implements InterceptContract.InterceptBasePresenter {

    public final static int PAGE_SMS = 0;
    public final static int PAGE_PHONE_CALL = 1;

    private final InterceptContract.InterceptBaseView mInterceptView;
    private final InterceptDataSource mDataSource;
    private final Context mContext;
    private List<InterceptSMS> mSMSList;
    private List<InterceptPhoneCall> mPhoneCallList;

    private InterceptPresenter(Context context, InterceptContract.InterceptBaseView interceptView, InterceptDataSource dataSource) {
        this.mContext = context;
        this.mInterceptView = interceptView;
        this.mDataSource = dataSource;
        mSMSList=new ArrayList<>();
        mPhoneCallList = new ArrayList<>();
        interceptView.setPresenter(this);
    }

    public static InterceptPresenter getInstance(Context context, InterceptContract.InterceptBaseView interceptView, InterceptDataSource dataSource) {
        return new InterceptPresenter(context,interceptView, dataSource);
    }

    @Override
    public void start() {
        mInterceptView.showInterceptSms(mSMSList.size());
    }

    @Override
    public void selectInterceptSms() {
        mInterceptView.showInterceptSms(mSMSList.size());
    }

    @Override
    public void selectInterceptPhone() {
        mInterceptView.showInterceptPhone(mPhoneCallList.size());
    }

    @Override
    public void selectSMS(int position) {
        mInterceptView.showSMSDetailDialog(mSMSList.get(position));
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
                mInterceptView.disableDeleteAllBtn();
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
                mInterceptView.disableDeleteAllBtn();
            }
            @Override
            public void OnCancel() {}
        });
    }

    @Override
    public void updateSMS() {
        mSMSList = mDataSource.getInterceptSMSList();
        mInterceptView.updateSMSList(mSMSList);
        mInterceptView.updateDelAllBtn(mSMSList.size(),mPhoneCallList.size());
    }

    @Override
    public void updatePhoneCall() {
        mPhoneCallList = mDataSource.getInterceptPhoneCallList();
        mInterceptView.updatePhoneCallList(mPhoneCallList);
        mInterceptView.updateDelAllBtn(mSMSList.size(),mPhoneCallList.size());
    }

    @Override
    public void contactBlackToWhite(String phoneNumber) {
        mDataSource.updateInterceptContactType(phoneNumber, InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE_NONE);
    }

    @Override
    public void deleteSMS(final int id) {
        DialogUtils.showConfirmDialog(mContext, "确定删除该信息？", new DialogUtils.OnButtonClickedListener() {
            @Override
            public void OnConfirm() {
                Iterator<InterceptSMS> iterator = mSMSList.iterator();
                while (iterator.hasNext()){
                    InterceptSMS sms = iterator.next();
                    if(sms.getId()==id){
                        iterator.remove();
                    }
                }
                mInterceptView.updateSMSList(mSMSList);
                mDataSource.deleteInterceptSMS(id);
                mInterceptView.updateDelAllBtn(mSMSList.size(),mPhoneCallList.size());
            }
            @Override
            public void OnCancel() {}
        });
    }

    @Override
    public void deletePhoneCall(final int id) {
        DialogUtils.showConfirmDialog(mContext, "确定删除该来电？", new DialogUtils.OnButtonClickedListener() {
            @Override
            public void OnConfirm() {
                Iterator<InterceptPhoneCall> iterator = mPhoneCallList.iterator();
                while (iterator.hasNext()){
                    InterceptPhoneCall phoneCall = iterator.next();
                    if(phoneCall.getId()==id){
                        iterator.remove();
                    }
                }
                mInterceptView.updatePhoneCallList(mPhoneCallList);
                mDataSource.deleteInterceptPhoneCall(id);
                mInterceptView.updateDelAllBtn(mSMSList.size(),mPhoneCallList.size());
            }
            @Override
            public void OnCancel() {}
        });
    }
}
