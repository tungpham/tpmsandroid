package com.ethan.morephone.presentation.numbers;

import android.content.Context;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public interface IncomingPhoneNumbersContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showPhoneNumbers(List<IncomingPhoneNumber> incomingPhoneNumbers);

        void emptyPhoneNumber();

        void showFakeData(FakeData fakeData);
    }

    interface Presenter extends BasePresenter {

        void getFakeData(Context context);

        void deleteIncomingPhoneNumber(Context context, String phoneNumber, String incomingPhoneNumberSid);

        void loadIncomingPhoneNumbers(Context context);

    }
}
