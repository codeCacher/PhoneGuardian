package com.cs.phoneguardian.guardian.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.text.TextUtils;

import com.cs.phoneguardian.modle.PhoneStateDataSource;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

/**
 * Created by Administrator on 2017/7/22.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        boolean guardOpen = SharedPreferencesUtils.getBoolean(context, Constants.KEY_GUARD_OPEN_STATE, false);
        String savedSIM = SharedPreferencesUtils.getString(context, Constants.KEY_SIM, "");
        String SIM = PhoneStateDataSource.getInstance(context).getSimSerialNumber();
        if ((!TextUtils.isEmpty(savedSIM)) && (!SIM.equals(savedSIM)) && guardOpen) {
            //如果重启后SIM序列号与之前绑定序列号不同，则发送报警短信
            String mergencyContactNumber = SharedPreferencesUtils.getString(context, Constants.KEY_MERGENCY_CONTACT, "");
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(mergencyContactNumber, null, "你的手机卡变更了！", null, null);
        }
    }
}
