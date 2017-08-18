package com.ethan.morephone.presentation.setting;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.entity.user.User;
import com.android.morephone.data.log.DebugTool;
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
        ApiMorePhone.updateForward(context, userId, forwardPhoneNumber, forwardEmail, new Callback<BaseResponse<PhoneNumber>>() {
            @Override
            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getResponse() != null){
                    mView.updateForward(response.body().getResponse().getForwardPhoneNumber(), response.body().getResponse().getForwardEmail());
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void enableForward(final Context context, String userId, final boolean isForward) {
        mView.showLoading(true);
        ApiMorePhone.enableForward(context, userId, isForward, new Callback<BaseResponse<PhoneNumber>>() {
            @Override
            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getResponse() != null){
                    mView.showConfigure(response.body().getResponse().isForward());
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void getPhoneNumber(Context context, String id) {
        mView.showLoading(true);

        ApiMorePhone.getPhoneNumber(context, id, new Callback<BaseResponse<PhoneNumber>>() {
            @Override
            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                DebugTool.logD("PHONE: " + response.body().getResponse().getAccountSid());
                if(response.isSuccessful() && response.body() != null && response.body().getResponse() != null){
                    mView.showPhoneNumber(response.body().getResponse());
                }else {
                    mView.emptyPhoneNumber();
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                mView.emptyPhoneNumber();
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
