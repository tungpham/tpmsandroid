package com.ethan.morephone.presentation.authentication.register.name;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * Created by truongnguyen on 10/15/16.
 */

public class RegisterNamePresenter implements RegisterNameContract.Presenter {

    private final RegisterNameContract.View mView;

    public RegisterNamePresenter(@NonNull RegisterNameContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void nextStep() {
        mView.showNextStep();
    }

    @Override
    public void checkMissingInfo(final EditText editTextFirstName, final EditText editTextLastName) {
        editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateInfo(editTextFirstName, editTextLastName);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInfo(editTextFirstName, editTextLastName);
            }
        });

        editTextLastName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                validateInfo(editTextFirstName, editTextLastName);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validateInfo(editTextFirstName, editTextLastName);
            }
        });
    }

    @Override
    public void start() {
        mView.setEnableNextStep(false);
    }

    public void validateInfo(EditText editTextFirstName, EditText editTextLastName) {
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();

        if(!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)){
            mView.setEnableNextStep(true);
        }else{
            mView.setEnableNextStep(false);
        }
    }
}
