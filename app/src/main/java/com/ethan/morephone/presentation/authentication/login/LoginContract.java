package com.ethan.morephone.presentation.authentication.login;

import android.widget.EditText;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 11/3/16.
 */

public interface LoginContract {

    interface View extends BaseView<Presenter> {

        void loginSuccess();

        void loginError(int status, String message);

        void setLoading(boolean isActive);

        void setEnableNextStep(boolean isEnable);

    }

    interface Presenter extends BasePresenter {

        void doLogin(String email, String password);

        void checkMissingInfo(EditText editTextFirstName, EditText editTextLastName);
        void validateInfo(EditText editTextFirstName, EditText editTextLastName);

    }
}
