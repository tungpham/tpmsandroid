package com.ethan.morephone.presentation.authentication.login.forgot;

import android.widget.EditText;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;


/**
 * Created by Ethan on 11/3/16.
 */

public interface ForgotPasswordContract {

    interface View extends BaseView<Presenter> {

        void showNextStep();

        void setEnableNextStep(boolean isEnable);

        void setLoading(boolean isActive);

        void showResultResetPassword(boolean isSuccess);

    }

    interface Presenter extends BasePresenter {

        void nextStep();

        void checkMissingInfo(EditText editTextFirstName);

        void resetPassword(String email);
    }
}
