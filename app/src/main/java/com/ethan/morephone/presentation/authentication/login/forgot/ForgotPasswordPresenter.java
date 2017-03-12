package com.ethan.morephone.presentation.authentication.login.forgot;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.StormpathError;


/**
 * Created by Ethan on 11/3/16.
 */

public class ForgotPasswordPresenter implements ForgotPasswordContract.Presenter {
    private final ForgotPasswordContract.View mView;

    public ForgotPasswordPresenter(@NonNull ForgotPasswordContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }


    @Override
    public void nextStep() {

    }

    @Override
    public void checkMissingInfo(final EditText editTextUsername) {
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(editTextUsername.getText().toString())) {
                    mView.setEnableNextStep(true);
                } else {
                    mView.setEnableNextStep(false);
                }
            }
        });
    }

    @Override
    public void resetPassword(String email) {
        mView.setLoading(true);
        Stormpath.resetPassword(email, new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mView.setLoading(false);
                mView.showResultResetPassword(true);
            }

            @Override
            public void onFailure(StormpathError error) {
                mView.setLoading(false);
                mView.showResultResetPassword(false);
            }
        });
    }
}
