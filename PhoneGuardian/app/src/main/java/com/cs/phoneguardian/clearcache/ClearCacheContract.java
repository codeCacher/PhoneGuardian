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

package com.cs.phoneguardian.clearcache;


import android.app.Activity;

import com.cs.phoneguardian.BasePresenter;
import com.cs.phoneguardian.BaseView;
import com.cs.phoneguardian.bean.AppInfo;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface ClearCacheContract {

    interface View extends BaseView<Presenter> {
        void showPhoneMemSize(long usedSize,long totalSize);
        void showSDMemsize(long usedSize,long totalSize);
        void showPhoneMemPercent(int percent);
        void showSDMemPercent(int percent);
        void initRoundProgressColor(int index, int phonePercent, int sdPercent);
        void showStartAnimation(int phoneMemPercent,int SDMemPercent);
        void initStartState();
        void initScanState();
        void updateScanState(String packageName);
        void showScanFinishState(long gabbageSize,int oldPhonePercent,int newPhonePercent,int oldSDPercent,int newSDPercent);
        void updateAppList(List<AppInfo> appInfo);
        void showFinishClean(long allCacheSize,int oldPhonePercent,int newPhonePercent,int oldSDPercent,int newSDPercent);
    }

    interface Presenter extends BasePresenter {
        interface OnScanCompleteListener{
            void OnComplete();
        }
        void startScan(Activity activity, OnScanCompleteListener listener);
        void completeScan();
        void clean(Activity activity);
    }

}
