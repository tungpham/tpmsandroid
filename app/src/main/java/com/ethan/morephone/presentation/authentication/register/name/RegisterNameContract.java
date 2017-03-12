package com.ethan.morephone.presentation.authentication.register.name;

import android.widget.EditText;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;


/**
 * Created by truongnguyen on 10/15/16.
 */

public interface RegisterNameContract {

    interface View extends BaseView<Presenter> {

        void showNextStep();

        void setEnableNextStep(boolean isEnable);

    }

    interface Presenter extends BasePresenter {

        void nextStep();

        void checkMissingInfo(EditText editTextFirstName, EditText editTextLastName);

        void validateInfo(EditText editTextFirstName, EditText editTextLastName);
    }
}
