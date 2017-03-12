package com.ethan.morephone.presentation.authentication.register.email;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;


/**
 * Created by truongnguyen on 10/15/16.
 */

public interface RegisterEmailContract {

    interface View extends BaseView<Presenter> {

        void showNextScreen();

        void setEnableNextStep(boolean isEnable);

    }

    interface Presenter extends BasePresenter {

        void onSaveDataAndNext(Context context, String data);

        void checkMissingInfo(AppCompatEditText editTextUsername);

        void validateInfo(AppCompatEditText editTextUsername);
    }
}
