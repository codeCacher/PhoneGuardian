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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class InterceptDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "InterceptDB.db";

    private static final String SQL_CREATE_INTERCEPT_CONTACT_TABLE =
            "create table " + InterceptPersistenceContract.AppEntry.INTERCEPT_CONTACT_TABLE_NAME + " (" +
                    InterceptPersistenceContract.AppEntry._ID + " integer" + " primary key autoincrement," +
                    InterceptPersistenceContract.AppEntry.CONTACT_NAME + " varchar(20),"+
                    InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER + " varchar(20)," +
                    InterceptPersistenceContract.AppEntry.INTERCEPT_TYPE + " integer" + " )";

    private static final String SQL_CREATE__INTERCEPT_SMS_TABLE =
            "create table " + InterceptPersistenceContract.AppEntry.INTERCEPT_SMS_TABLE_NAME + " (" +
                    InterceptPersistenceContract.AppEntry._ID + " integer" + " primary key autoincrement," +
                    InterceptPersistenceContract.AppEntry.CONTACT_NAME + " varchar(20),"+
                    InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER + " varchar(20)," +
                    InterceptPersistenceContract.AppEntry.TIME + " integer," +
                    InterceptPersistenceContract.AppEntry.SMS_CONTENT + " varchar(100)" + " )";

    private static final String SQL_CREATE__INTERCEPT_PHONE_TABLE =
            "create table " + InterceptPersistenceContract.AppEntry.INTERCEPT_PHONE_TABLE_NAME + " (" +
                    InterceptPersistenceContract.AppEntry._ID + " integer" + " primary key autoincrement," +
                    InterceptPersistenceContract.AppEntry.CONTACT_NAME + " varchar(20),"+
                    InterceptPersistenceContract.AppEntry.CONTACT_PHONE_NUMBER + " varchar(20)," +
                    InterceptPersistenceContract.AppEntry.TIME + " integer" + " )";

    public InterceptDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_INTERCEPT_CONTACT_TABLE);
        db.execSQL(SQL_CREATE__INTERCEPT_SMS_TABLE);
        db.execSQL(SQL_CREATE__INTERCEPT_PHONE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
