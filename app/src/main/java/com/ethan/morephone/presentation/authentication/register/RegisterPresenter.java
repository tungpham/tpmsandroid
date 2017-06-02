package com.ethan.morephone.presentation.authentication.register;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.RegistrationForm;
import com.stormpath.sdk.models.StormpathError;

/**
 * Created by Ethan on 6/2/17.
 */

public class RegisterPresenter implements RegisterContract.Presenter {

    private final RegisterContract.View mView;

    public RegisterPresenter(@NonNull RegisterContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void checkMissingInfo(final EditText editTextFirstName, final EditText editTextLastName) {
        editTextFirstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

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

    @Override
    public void createUser(final Context context, String email, String password) {
        mView.setLoading(true);
        MyPreference.setUserEmail(context, email);
        MyPreference.setPassword(context, password);
        RegistrationForm registrationData = new RegistrationForm(email, password);

//        registrationData.setGivenName(MyPreference.getFirstName(context))
//                .setSurname(MyPreference.getLastName(context));
        Stormpath.register(registrationData, new StormpathCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mView.setLoading(false);
                mView.registerSuccess();
            }

            @Override
            public void onFailure(StormpathError error) {
                DebugTool.logD("CODE: " + error.code());
                DebugTool.logD("MSG: " + error.message());
                DebugTool.logD("STATUS: " + error.status());
                if(error.status() == -1){
                    mView.registerSuccess();
                }else {
                    mView.registerFailure(error.status(), error.message());
                }
                mView.setLoading(false);
            }
        });
    }
}
