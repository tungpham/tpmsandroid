package com.ethan.morephone.presentation.setting;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.TwilioManager;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.incoming.ChangeFriendlyName;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    public void changeFriendlyName(Context context, String incomingPhoneNumberSid, String friendlyName) {
        mView.showLoading(true);
        ChangeFriendlyName.RequestValue requestValue = new ChangeFriendlyName.RequestValue(TwilioManager.getSid(context), incomingPhoneNumberSid, friendlyName);
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
    public void settingForward(final Context context, String userId, String forwardPhoneNumber, String forwardEmail) {
        mView.showLoading(true);
        ApiMorePhone.updateForward(context, userId, forwardPhoneNumber, forwardEmail, new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if(response.isSuccessful() && response.body() != null){
                    MyPreference.setSettingConfigurePhone(context, response.body().getResponse().getForwardPhoneNumber());
                    MyPreference.setSettingConfigureEmail(context, response.body().getResponse().getForwardEmail());
                    mView.updateForward();
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void enableForward(final Context context, String userId, final boolean isForward) {
        mView.showLoading(true);
        ApiMorePhone.enableForward(context, userId, isForward, new Callback<BaseResponse<User>>() {
            @Override
            public void onResponse(Call<BaseResponse<User>> call, Response<BaseResponse<User>> response) {
                if(response.isSuccessful()){
                    MyPreference.setSettingConfigure(context, isForward);
                }
                mView.showLoading(false);
                mView.showConfigure();
            }

            @Override
            public void onFailure(Call<BaseResponse<User>> call, Throwable t) {
                mView.showLoading(false);
                mView.showConfigure();
            }
        });
    }

    @Override
    public void start() {

    }
}
