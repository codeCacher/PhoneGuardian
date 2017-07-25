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

package com.cs.phoneguardian.intercept;


import com.cs.phoneguardian.BasePresenter;
import com.cs.phoneguardian.BaseView;
import com.cs.phoneguardian.bean.InterceptPhoneCall;
import com.cs.phoneguardian.bean.InterceptSMS;

import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
public interface InterceptContract {

    interface View extends BaseView<Presenter> {
        void showInterceptSms();
        void showInterceptPhone();
        void updateSMSList(List<InterceptSMS> list);
        void updatePhoneCallList(List<InterceptPhoneCall> list);
        void enableDeleteAllBtn();
        void disableDeleteAllBtn();
        void showSMSDetailDialog(InterceptSMS sms);
    }

    interface Presenter extends BasePresenter {
        void selectInterceptSms();
        void selectInterceptPhone();
        void selectSMS(int position);
        void selectPhoneCall(int position);
        void deletAllSMS();
        void deletAllPhoneCall();
        void updateSMS();
        void updatePhoneCall();
    }
}
