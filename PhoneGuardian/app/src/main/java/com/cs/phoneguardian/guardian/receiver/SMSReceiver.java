package com.cs.phoneguardian.guardian.receiver;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import com.cs.phoneguardian.R;
import com.cs.phoneguardian.guardian.service.LocationService;
import com.cs.phoneguardian.utils.Constants;
import com.cs.phoneguardian.utils.SharedPreferencesUtils;

import java.io.IOException;

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

                //把音乐音量强制设置为最大音量
                AudioManager mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
                int mVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC); // 获取当前音乐音量
                int maxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);// 获取最大声音
                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, 0); // 设置为最大声音，可通过SeekBar更改音量大小

                AssetFileDescriptor fileDescriptor;
                try {
                    fileDescriptor = context.getAssets().openFd("Numb.mp3");
                    MediaPlayer mediaPlayer = new MediaPlayer();
                    mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(), fileDescriptor.getStartOffset(), fileDescriptor.getLength());
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (messageBody.contains(Constants.SMS_CMD_LOCATION)) {
                //发送位置信息
                context.startService(new Intent(context,LocationService.class));
            }
            if(messageBody.contains(Constants.SMS_CMD_LOCK)){
                //锁屏
            }
            if(messageBody.contains(Constants.SMS_CMD_WIPE)){
                //数据擦除
            }
        }
    }
}
