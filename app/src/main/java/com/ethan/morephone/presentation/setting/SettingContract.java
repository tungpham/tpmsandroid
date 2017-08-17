package com.ethan.morephone.presentation.setting;

import android.content.Context;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 4/25/17.
 */

public interface SettingContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void updateFriendlyName(String friendlyName);

        void updateForward();
    }

    interface Presenter extends BasePresenter {

        void changeFriendlyName(Context context, String incomingPhoneNumberSid, String friendlyName);

        void settingForward(Context context, String userId, String forwardPhoneNumber, String forwardEmail);
    }
}
