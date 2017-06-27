package com.ethan.morephone.presentation.authentication.register.password;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import com.ethan.morephone.MyPreference;


/**
 * Created by truongnguyen on 10/17/16.
 */

public class RegisterPasswordPresenter implements RegisterPasswordContract.Presenter {

    private final RegisterPasswordContract.View mView;

    public RegisterPasswordPresenter(@NonNull RegisterPasswordContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

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

    @Override
    public void createUser(final Context context, String password) {
        mView.setLoading(true);
        MyPreference.setPassword(context, password);
//        RegistrationForm registrationData = new RegistrationForm(MyPreference.getUserEmail(context), password);
//
//        registrationData.setGivenName(MyPreference.getFirstName(context))
//                .setSurname(MyPreference.getLastName(context));
//        Stormpath.register(registrationData, new StormpathCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                mView.setLoading(false);
//                mView.registerSuccess();
//            }
//
//            @Override
//            public void onFailure(StormpathError error) {
//                DebugTool.logD("CODE: " + error.code());
//                DebugTool.logD("MSG: " + error.message());
//                DebugTool.logD("STATUS: " + error.status());
//                if(error.status() == -1){
//                    mView.registerSuccess();
//                }else {
//                    mView.registerFailure(error.status(), error.message());
//                }
//                mView.setLoading(false);
//            }
//        });
    }
}
