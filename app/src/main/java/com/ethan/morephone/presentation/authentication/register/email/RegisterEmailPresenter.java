package com.ethan.morephone.presentation.authentication.register.email;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

/**
 * Created by truongnguyen on 10/15/16.
 */

public class RegisterEmailPresenter implements RegisterEmailContract.Presenter {

    private final RegisterEmailContract.View mView;

    public RegisterEmailPresenter(@NonNull RegisterEmailContract.View view){
        mView = view;

        mView.setPresenter(this);
    }



    @Override
    public void start() {

    }

    @Override
    public void onSaveDataAndNext(Context context, String data) {
        mView.showNextScreen();
    }

    @Override
    public void checkMissingInfo(final AppCompatEditText editTextUsername) {
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInfo(editTextUsername);
            }
        });
    }

    @Override
    public void validateInfo(AppCompatEditText editTextUsername) {
        if (!TextUtils.isEmpty(editTextUsername.getText().toString())) {
            mView.setEnableNextStep(true);
        } else {
            mView.setEnableNextStep(false);
        }
    }
}
