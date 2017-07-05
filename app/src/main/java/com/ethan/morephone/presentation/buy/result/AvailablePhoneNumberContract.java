package com.ethan.morephone.presentation.buy.result;

import android.content.Context;

import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumber;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 4/2/17.
 */

public interface AvailablePhoneNumberContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showResultSearchNumber(List<AvailablePhoneNumber> availablePhoneNumbers);

        void emptyPhoneNumberAvailable();
    }

    interface Presenter extends BasePresenter {
        void searchPhoneNumber(Context context, String countryCode, String phoneNumber, boolean smsEnabled, boolean mmsEnabled, boolean voiceEnabled);
    }
}
