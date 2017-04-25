package com.ethan.morephone.presentation.setting;

import android.support.annotation.NonNull;

import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.incoming.ChangeFriendlyName;
import com.ethan.morephone.Constant;

/**
 * Created by Ethan on 4/25/17.
 */

public class SettingPresenter implements SettingContract.Presenter {

    private final SettingContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final ChangeFriendlyName mChangeFriendlyName;

    public SettingPresenter(@NonNull SettingContract.View view,
                            @NonNull UseCaseHandler useCaseHandler,
                            @NonNull ChangeFriendlyName changeFriendlyName) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mChangeFriendlyName = changeFriendlyName;

        mView.setPresenter(this);
    }

    @Override
    public void changeFriendlyName(String incomingPhoneNumberSid, String friendlyName) {
        mView.showLoading(true);
        ChangeFriendlyName.RequestValue requestValue = new ChangeFriendlyName.RequestValue(Constant.ACCOUNT_SID, incomingPhoneNumberSid, friendlyName);
        mUseCaseHandler.execute(mChangeFriendlyName, requestValue, new UseCase.UseCaseCallback<ChangeFriendlyName.ResponseValue>() {
            @Override
            public void onSuccess(ChangeFriendlyName.ResponseValue response) {
                mView.showLoading(false);
                mView.updateFriendlyName(response.getIncomingPhoneNumber().friendlyName);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
