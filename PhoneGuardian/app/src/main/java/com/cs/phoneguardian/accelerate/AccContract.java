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

package com.cs.phoneguardian.accelerate;


import android.content.Context;
import android.widget.BaseAdapter;

import com.cs.phoneguardian.BasePresenter;
import com.cs.phoneguardian.BaseView;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AccContract {

    interface View extends BaseView<Presenter> {
        void showResumeAnimation(int percent);
        void showMemorySize(long used,long totle);
        void showMemoryPercent(int percent);
        void showState(int precent);
        void upDateAppList(List<AppInfo> userAppList, List<AppInfo> sysAppList);
        void showCountTitle(int totalCount,int userAppCount,int sysAppCount);
        void showEndBtnEnable(int appCount);
        void showEndBtnDisable();
        void showSelectAllBtnEnable();
        void showSelectAllBtnDisalbe();
        void showToastTotalClearMemory(int appCount,long memorySize);
        void initCountTitle();
    }

    interface Presenter extends BasePresenter {
        void setEndBtnState();
        void selectAll();
        void cacelSelectAll();
        void killSelectedProcess();
        void selectLockApp(Context context);
    }

    interface SettingView extends BaseView<SettingPreseter>{

    }

    interface SettingPreseter extends BasePresenter{

    }
}
