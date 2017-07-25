package com.cs.phoneguardian.intercept.modle;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.cs.phoneguardian.bean.InterceptContact;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */

public class InterceptDataSource implements InterceptBaseDataSource {

    Context mContext;
    private InterceptDbHelper mDbHelper;
    private SQLiteDatabase mDatabase;

    private InterceptDataSource(Context context) {
        if (mContext == null) {
            this.mContext = context;
            mDbHelper = new InterceptDbHelper(context);
            mDatabase = mDbHelper.getWritableDatabase();
        }
    }

    public static InterceptDataSource getInstance(Context context) {
        return new InterceptDataSource(context);
    }

    /**
     * 获取所有骚扰拦截联系人对象，包括黑名单和白名单
     *
     * @return 所有骚扰拦截联系人对象列表
     */
    @Override
    public List<InterceptContact> getInterceptContactList() {

        List<InterceptContact> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME, null, null, null, null, null, null);
        if (cursor == null) {
            return list;
        }
        while (cursor.moveToNext()) {
            InterceptContact contact = new InterceptContact();
            int idIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry._ID);
            int nameIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_NAME);
            int numberIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER);
            int typeIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            int type = cursor.getInt(typeIndex);

            contact.setId(id);
            contact.setName(name);
            contact.setPhoneNumber(number);
            contact.setInterceptType(type);

            list.add(contact);
        }
        cursor.close();
        return list;
    }

    /**
     * 根据拦截类型获取黑名单联系人对象
     *
     * @return 黑名单联系人对象列表
     */
    @Override
    public List<InterceptContact> getInterceptContactList(int interceptType) {

        List<InterceptContact> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME,
                null, InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE + "=?",
                new String[]{interceptType + ""}, null, null, null);
        if (cursor == null) {
            return list;
        }
        while (cursor.moveToNext()) {
            InterceptContact contact = new InterceptContact();
            int idIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry._ID);
            int nameIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_NAME);
            int numberIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER);
            int typeIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            int type = cursor.getInt(typeIndex);

            contact.setId(id);
            contact.setName(name);
            contact.setPhoneNumber(number);
            contact.setInterceptType(type);

            list.add(contact);
        }
        cursor.close();
        return list;
    }

    /**
     * 向拦截联系人列表中添加一项
     *
     * @param contract 要添加的联系人对象
     * @return 影响的行数
     */
    @Override
    public long insertInterceptContact(InterceptContact contract) {
        ContentValues values = new ContentValues();
        values.put(InterceptPersistenceContract.AppEntry.CONTACT_NAME, contract.getName());
        values.put(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER, contract.getPhoneNumber());
        values.put(InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE, contract.getInterceptType());

        return mDatabase.insert(InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME,
                null, values);
    }

    /**
     * 从拦截联系人列表中删除制定号码的联系人
     *
     * @param phoneNumber 要删除的联系人号码
     * @return 影响的行数
     */
    @Override
    public int deleteInterceptContact(String phoneNumber) {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME,
                InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER + "=?",
                new String[]{phoneNumber});
    }

    /**
     * 删除所有拦截类型为type的联系人
     * @param type 拦截类型
     * @return 受影响的行数
     */
    @Override
    public int deleteInterceptContact(int type) {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME,
                InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE + "=?",
                new String[]{type+""});
    }

    /**
     * 删除所有的拦截联系人
     * @return 受影响的行数
     */
    @Override
    public int deleteAllInterceptContact() {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME, null, null);
    }

    /**
     * 获取所有屏蔽短信对象
     *
     * @return 屏蔽短信对象列表
     */
    @Override
    public List<InterceptSMS> getInterceptSMSList() {
        List<InterceptSMS> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(InterceptPersistenceContract.AppEntry.INTERCEPT_SMS_TABLE_NAME, null, null, null, null, null, null);
        if (cursor == null) {
            return list;
        }
        while (cursor.moveToNext()) {
            InterceptSMS sms = new InterceptSMS();
            int idIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry._ID);
            int nameIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_NAME);
            int numberIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER);
            int contentIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.SMS_CONTENT);
            int timeIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.TIME);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            String content = cursor.getString(contentIndex);
            long timeLong = cursor.getLong(timeIndex);
            Timestamp time = new Timestamp(timeLong);


            sms.setId(id);
            sms.setName(name);
            sms.setPhoneNumber(number);
            sms.setContent(content);
            sms.setTime(time);

            list.add(sms);
        }
        cursor.close();
        return list;
    }

    /**
     * 向拦截短信数据库中插入一条短信对象
     *
     * @param sms 要插入的短信对象
     * @return 收影响的行数
     */
    @Override
    public long insertInterceptSMS(InterceptSMS sms) {
        ContentValues values = new ContentValues();
        values.put(InterceptPersistenceContract.AppEntry.CONTACT_NAME, sms.getName());
        values.put(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER, sms.getPhoneNumber());
        values.put(InterceptPersistenceContract.AppEntry.SMS_CONTENT, sms.getContent());
        values.put(InterceptPersistenceContract.AppEntry.TIME, sms.getTime().getTime());

        return mDatabase.insert(InterceptPersistenceContract.AppEntry.INTERCEPT_SMS_TABLE_NAME,
                null, values);
    }

    /**
     * 根据id从拦截短信数据库中删除一条短信对象
     *
     * @param id 要删除短信对象的id
     * @return 收影响的行数
     */
    @Override
    public int deleteInterceptSMS(int id) {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_SMS_TABLE_NAME,
                InterceptPersistenceContract.AppEntry._ID + "=?",
                new String[]{id + ""});
    }

    /**
     * 删除所有的拦截短信
     * @return 收影响的行数
     */
    @Override
    public int deleteAllInterceptSMS() {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_SMS_TABLE_NAME,null,null);
    }

    /**
     * 获取来电拦截所有来电对象列表
     *
     * @return 来电拦截列表
     */
    @Override
    public List<InterceptPhoneCall> getInterceptPhoneCallList() {
        List<InterceptPhoneCall> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(InterceptPersistenceContract.AppEntry.INTERCEPT_PHONE_TABLE_NAME, null, null, null, null, null, null);
        if (cursor == null) {
            return list;
        }
        while (cursor.moveToNext()) {
            InterceptPhoneCall phoneCall = new InterceptPhoneCall();
            int idIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry._ID);
            int nameIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_NAME);
            int numberIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER);
            int timeIndex = cursor.getColumnIndex(InterceptPersistenceContract.AppEntry.TIME);

            int id = cursor.getInt(idIndex);
            String name = cursor.getString(nameIndex);
            String number = cursor.getString(numberIndex);
            long timeLong = cursor.getLong(timeIndex);
            Timestamp time = new Timestamp(timeLong);


            phoneCall.setId(id);
            phoneCall.setName(name);
            phoneCall.setPhoneNumber(number);
            phoneCall.setTime(time);

            list.add(phoneCall);
        }
        cursor.close();
        return list;
    }

    /**
     * 向来电拦截数据库中插入一条来电对象
     *
     * @param phoneCall 要插入的来电对象
     * @return 受影响的行数
     */
    @Override
    public long insertInterceptPhoneCall(InterceptPhoneCall phoneCall) {
        ContentValues values = new ContentValues();
        values.put(InterceptPersistenceContract.AppEntry.CONTACT_NAME, phoneCall.getName());
        values.put(InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER, phoneCall.getPhoneNumber());
        values.put(InterceptPersistenceContract.AppEntry.TIME, phoneCall.getTime().getTime());

        return mDatabase.insert(InterceptPersistenceContract.AppEntry.INTERCEPT_PHONE_TABLE_NAME,
                null, values);
    }

    /**
     * 根据id从来电拦截数据库中删除一条来电对象
     *
     * @param id 要删除的来电id
     * @return 受影响的行数
     */
    @Override
    public int deleteInterceptPhoneCall(int id) {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_PHONE_TABLE_NAME,
                InterceptPersistenceContract.AppEntry._ID + "=?",
                new String[]{id + ""});
    }

    /**
     * 删除所有的拦截来电
     * @return 受影响的行数
     */
    @Override
    public int deleteAllInterceptPhoneCall() {
        return mDatabase.delete(InterceptPersistenceContract.AppEntry.INTERCEPT_PHONE_TABLE_NAME,null,null);
    }
}
