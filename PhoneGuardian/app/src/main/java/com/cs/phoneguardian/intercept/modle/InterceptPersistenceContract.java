/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.cs.phoneguardian.intercept.modle;

import android.provider.BaseColumns;

/**
 * The contract used for the db to save the tasks locally.
 */
public final class InterceptPersistenceContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private InterceptPersistenceContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class AppEntry implements BaseColumns {

        /**
         * 拦截类型，仅拦截短信
         */
        public static final int INTERCEPT_TYPE_SMS = 0;
        /**
         * 拦截类型，仅拦截来电
         */
        public static final int INTERCEPT_TYPE_PHONE = 1;
        /**
         * 拦截类型，拦截短信和来电
         */
        public static final int INTERCEPT_TYPE_ALL = 2;
        /**
         * 拦截类型，不拦截短信
         */
        public static final int INTERCEPT_TYPE_NO_SMS = 3;
        /**
         * 拦截类型，不拦截来电
         */
        public static final int INTERCEPT_TYPE_NO_PHONE = 4;
        /**
         * 拦截类型，不拦截所有
         */
        public static final int INTERCEPT_TYPE_NONE = 5;

        /**
         * 拦截联系人数据库表名
         */
        public static final String INTERCEPT_CONTACT_TABLE_NAME = "interceptContact";
        /**
         * 拦截短信数据库表名
         */
        public static final String INTERCEPT_SMS_TABLE_NAME = "interceptSMS";
        /**
         * 拦截来电数据库表名
         */
        public static final String INTERCEPT_PHONE_TABLE_NAME = "interceptPhone";

        /**
         * 联系人姓名
         */
        public static final String CONTACT_NAME = "contactName";
        /**
         * 联系人号码
         */
        public static final String CONTACT_PHONE_NUMBER = "contactPhoneNumber";
        /**
         * 拦截类型
         */
        public static final String INTERCEPT_TYPE = "interceptType";
        /**
         * 拦截的日期
         */
        public static final String TIME = "time";
        /**
         * 短信内容
         */
        public static final String SMS_CONTENT = "interceptSMS";
    }
}
