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

package com.cs.phoneguardian.modle;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppInfoDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_NAME = "AppInfoDB.db";

    private static final String SQL_CREATE_ACC_LOCK_APP_TABLE =
            "create table " + AppInfoPersistenceContract.AppEntry.ACC_LOCK_TABLE_NAME + " (" +
                    AppInfoPersistenceContract.AppEntry._ID + " integer" + " primary key autoincrement," +
                    AppInfoPersistenceContract.AppEntry.PACKAGE_NAME + " varchar(100)"+" )";

    private static final String SQL_CREATE__APP_LOCK_TABLE =
            "create table " + AppInfoPersistenceContract.AppEntry.APP_LOCK_TABLE_NAME + " (" +
                    AppInfoPersistenceContract.AppEntry._ID + " integer" + " primary key autoincrement," +
                    AppInfoPersistenceContract.AppEntry.PACKAGE_NAME + " varchar(100)"+" )";

    public AppInfoDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ACC_LOCK_APP_TABLE);
        db.execSQL(SQL_CREATE__APP_LOCK_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Not required as at version 1
    }
}
