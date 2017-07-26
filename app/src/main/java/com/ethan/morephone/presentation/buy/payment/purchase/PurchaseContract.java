package com.ethan.morephone.presentation.buy.payment.purchase;

import android.content.Context;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 7/5/17.
 */

public interface PurchaseContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void buyIncomingPhoneNumberSuccess(IncomingPhoneNumber incomingPhoneNumber);

        void buyIncomingPhoneNumberFail();
    }

    interface Presenter extends BasePresenter {

        void buyIncomingPhoneNumber(Context context, String phoneNumber);

        /**
         * Create phone with backend
         * @param incomingPhoneNumber  .
         */
        void createPhoneNumber(Context context, IncomingPhoneNumber incomingPhoneNumber);
    }
}
