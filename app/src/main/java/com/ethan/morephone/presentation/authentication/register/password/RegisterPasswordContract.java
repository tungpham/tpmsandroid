package com.ethan.morephone.presentation.authentication.register.password;

import android.content.Context;
import android.support.v7.widget.AppCompatEditText;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;


/**
 * Created by truongnguyen on 10/17/16.
 */

public interface RegisterPasswordContract {

    interface View extends BaseView<Presenter> {

        void registerSuccess();

        void setEnableNextStep(boolean isEnable);

        void registerFailure(int status, String message);

        void setLoading(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void checkMissingInfo(AppCompatEditText editTextUsername);

        void validateInfo(AppCompatEditText editTextUsername);

        void createUser(Context context, String password);
    }

}
