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

package com.cs.phoneguardian.applock;


import android.app.Activity;

import com.cs.phoneguardian.BasePresenter;
import com.cs.phoneguardian.BaseView;
import com.cs.phoneguardian.bean.AppInfo;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface AppLockContract {

    interface View extends BaseView<Presenter> {
        void upDateAppList(List<AppInfo> list);
        void showLockAppCount(int count);
        void showPassWordSettingActivity();
        void showPassWordDialog(String correctPsd);
    }

    interface Presenter extends BasePresenter {
        void removeLockApp(AppInfo info);
        void addLockApp(AppInfo info);
        void openAppLock(Activity activity);
        void onSetUsageAccessResult();
    }
}
