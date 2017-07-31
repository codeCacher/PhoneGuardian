package com.cs.phoneguardian.intercept.modle;

import com.cs.phoneguardian.bean.Contact;
import com.cs.phoneguardian.bean.InterceptContact;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;
import com.cs.phoneguardian.intercept.InterceptContract;
import com.cs.phoneguardian.modle.BaseDataSource;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public interface InterceptBaseDataSource extends BaseDataSource {
    public List<InterceptContact> getInterceptContactList();
    public List<InterceptContact> getInterceptContactList(int type);
    public InterceptContact getInterceptContact(String phoneNumber);
    public long updateInterceptContactType(String phoneNumber,int type);
    public long insertInterceptContact(InterceptContact contract);
    public int deleteInterceptContact(String phoneNumber);
    public int deleteInterceptContact(int type);
    public int deleteAllInterceptContact();


    public List<InterceptSMS> getInterceptSMSList();
    public long insertInterceptSMS(InterceptSMS sms);
    public int deleteInterceptSMS(int id);
    public int deleteAllInterceptSMS();

    public List<InterceptPhoneCall> getInterceptPhoneCallList();
    public long insertInterceptPhoneCall(InterceptPhoneCall phoneCall);
    public int deleteInterceptPhoneCall(int id);
    public int deleteAllInterceptPhoneCall();
}
