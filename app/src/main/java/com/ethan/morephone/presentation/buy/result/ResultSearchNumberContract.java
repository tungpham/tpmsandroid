package com.ethan.morephone.presentation.buy.result;

import android.content.Context;

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 4/2/17.
 */

public interface ResultSearchNumberContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showResultSearchNumber(List<PhoneNumber> phoneNumbers);
    }

    interface Presenter extends BasePresenter {
        void searchPhoneNumber(Context context, String countryCode);
    }
}
