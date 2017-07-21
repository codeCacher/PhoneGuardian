package com.cs.phoneguardian.utils;

/**
 * 放一些常量
 */

public class Constants {
    /**
     * sp文件名
     */
    public static final String SP_FILE_NAME = "cfg_sp";

    //手机防盗短信命令
    /**
     * 手机防盗短信命令：GPS追踪
     */
    public static final String SMS_CMD_LOCATION = "#*location*#";
    /**
     * 手机防盗短信命令：播放报警音乐
     */
    public static final String SMS_CMD_ALARM = "#*alarm*#";
    /**
     * 手机防盗短信命令：数据销毁
     */
    public static final String SMS_CMD_WIPE = "#*wipedata*#";
    /**
     * 手机防盗短信命令：远程锁屏
     */
    public static final String SMS_CMD_LOCK = "#*lockscreen*#";

    /**
     * 上一次的体检分数的sp键值
     */
    public static final String KEY_CHECK_RESULT = "checkResult";

    /**
     * 手机防盗是否开启的sp键值
     */
    public static final String KEY_GUARD_OPEN_STATE = "guardOpen";

    /**
     * 手机防盗是否已经设置密码的sp键值
     */
    public static final String KEY_PSD_STATE = "keyCipherState";

    /**
     * 手机防盗密码的sp键值
     */
    public static final String KEY_PSD = "keyPsd";

    /**
     * 手机SIM卡序列号sp键值
     */
    public static final String KEY_SIM = "keySIM";

    /**
     * 紧急联系人手机号码sp键值
     */
    public static final String KEY_MERGENCY_CONTACT = "keyMergencyContact";


    //Activity之间传递数据的Key
    /**
     * 紧急联系人手机号码选择结果
     */
    public static final String KEY_MERCONTACT_RESULT = "keyMerContactResult";
}
