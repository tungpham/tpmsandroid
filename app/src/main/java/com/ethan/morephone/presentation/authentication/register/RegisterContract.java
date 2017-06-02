package com.ethan.morephone.presentation.authentication.register;

import android.content.Context;
import android.widget.EditText;

import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 6/2/17.
 */

public interface RegisterContract {

    interface View extends BaseView<RegisterContract.Presenter> {

        void registerSuccess();

        void setEnableNextStep(boolean isEnable);

        void registerFailure(int status, String message);

        void setLoading(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void checkMissingInfo(EditText editTextFirstName, EditText editTextLastName);
        void validateInfo(EditText editTextFirstName, EditText editTextLastName);

        void createUser(Context context, String email, String password);
    }

}
