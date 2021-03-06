package com.cs.phoneguardian.utils;

import android.content.Context;

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
    public static final String KEY_CHECK_RESULT = "key_check_result";

    /**
     * 手机防盗是否开启的sp键值
     */
    public static final String KEY_GUARD_OPEN_STATE = "key_guard_open_state";

    /**
     * 手机防盗是否已经设置密码的sp键值
     */
    public static final String KEY_GUARD_PSD_STATE = "key_guard_psd_state";

    /**
     * 手机防盗密码的sp键值
     */
    public static final String KEY_GUARD_PSD = "key_guard_psd";

    /**
     * 手机SIM卡序列号sp键值
     */
    public static final String KEY_SIM = "key_sim";

    /**
     * 紧急联系人手机号码sp键值
     */
    public static final String KEY_MERGENCY_CONTACT = "key_mergency_contact";

    /**
     * 应用锁密码sp键值
     */
    public static final String KEY_APP_LOCK_PASSWORD = "key_app_lock_password";

    /**
     * 应用锁是否开启sp键值
     */
    public static final String KEY_ENABLE_APP_LOCK = "key_enable_app_lock";

    //骚扰拦截设置sp
    /**
     * 是否开启骚扰拦截的sp键值
     */
    public static final String KEY_ENABLE_INTERCEPT = "key_enable_intercept";
    /**
     * 拦截规则：拦截黑名单是否开启键值
     */
    public static final String KEY_INTERCEPT_RULE_BLACK = "key_intercept_rule_black";
    /**
     * 拦截规则：拦截陌生人是否开启键值
     */
    public static final String KEY_INTERCEPT_RULE_STRANGER = "key_intercept_rule_stranger";
    /**
     * 拦截规则：仅允许白名单是否开启键值
     */
    public static final String KEY_INTERCEPT_RULE_WHITE = "key_intercept_rule_white";
    /**
     * 是否开启拦截后通知栏通知的sp键值
     */
    public static final String KEY_INTERCEPT_NOTIFY_ENALBE = "key_intercept_notify_enalbe";

    //Intent Filter
    /**
     * 应用锁解锁之后告知服务停止锁定该应用的Intent filter
     */
    public static final String INTENT_FILT_APP_LOCK_SKIP = "android.intent.action.APP_LOCK_SKIP";

    //Activity之间传递数据的Key
    /**
     * 联系人手机号码选择结果
     */
    public static final String KEY_CONTACT_RESULT = "keyMerContactResult";

    /**
     * 包名
     */
    public static final String KEY_PKG_NAME_EXTRA = "key_pkg_name_extra";

    /**
     * 打开黑白名单时首先打开的页面是黑名单还是白名单状态键值
     */
    public static final String KEY_BLACK_WHITE_CONTACT_START_STATE = "key_black_white_contact_start_state";

    /**
     * 选择的联系人列表键值
     */
    public static final String KEY_SELECTED_CONTACT = "key_selected_contact";
}
