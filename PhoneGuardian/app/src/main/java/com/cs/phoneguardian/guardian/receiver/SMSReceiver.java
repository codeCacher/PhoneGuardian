package com.cs.phoneguardian.guardian.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.cs.phoneguardian.guardian.service.AlarmService;
import com.cs.phoneguardian.guardian.service.LocationService;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/7/22.
 */

public class SMSReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(final Context context, Intent intent) {
        boolean guardOpenState = SharedPreferencesUtils.getBoolean(context, Constants.KEY_GUARD_OPEN_STATE, false);
        if (!guardOpenState) {
            return;
        }

        //获取短信内容
        String contactNumber = SharedPreferencesUtils.getString(context, Constants.KEY_MERGENCY_CONTACT, "");
        Object[] objects = (Object[]) intent.getExtras().get("pdus");
        if (objects == null) return;
        for (Object object : objects) {
            SmsMessage sms = SmsMessage.createFromPdu((byte[]) object);
            String originatingAddress = sms.getOriginatingAddress();
            String messageBody = sms.getMessageBody();

            //根据短信命令执行对应的操作

            //TODO 需要处理号码有区号
//            if(contactNumber.equals(originatingAddress) && messageBody.contains(Constants.SMS_CMD_ALARM)){
            if (messageBody.contains(Constants.SMS_CMD_ALARM)) {
                //播放报警音乐
                context.startService(new Intent(context, AlarmService.class));
            }
            if (messageBody.contains(Constants.SMS_CMD_LOCATION)) {
                //发送位置信息
                context.startService(new Intent(context, LocationService.class));
            }
            if (messageBody.contains(Constants.SMS_CMD_LOCK)) {
                //锁屏
            }
            if (messageBody.contains(Constants.SMS_CMD_WIPE)) {
                //数据擦除
            }
        }
    }
}
