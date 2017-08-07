package com.ethan.morephone.presentation.buy;

import android.content.Context;

import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 4/3/17.
 */

public interface SearchPhoneNumberContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showAvailableCountries(List<AvailableCountry> availableCountries);

        void loadEmptyCountries();
    }

    interface Presenter extends BasePresenter {
        void loadAvailableCountries(Context context);


    }
}
