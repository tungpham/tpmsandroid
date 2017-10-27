package com.ethan.morephone.presentation.numbers;

import android.content.Context;

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public interface IncomingPhoneNumbersContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showPhoneNumbers(List<PhoneNumber> incomingPhoneNumbers);

        void emptyPhoneNumber();
    }

    interface Presenter extends BasePresenter {

        void deleteIncomingPhoneNumber(Context context, String phoneNumber, String incomingPhoneNumberSid, String phoneNumberId);

        void loadIncomingPhoneNumbers(Context context);

    }
}
