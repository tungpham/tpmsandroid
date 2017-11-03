package com.ethan.morephone.presentation.buy.result;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumbers;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/2/17.
 */

public class AvailablePhoneNumberPresenter implements AvailablePhoneNumberContract.Presenter {

    private AvailablePhoneNumberContract.View mView;

    public AvailablePhoneNumberPresenter(@NonNull AvailablePhoneNumberContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void searchPhoneNumber(Context context, String countryCode, String phoneNumber, boolean smsEnabled, boolean mmsEnabled, boolean voiceEnabled) {
        mView.showLoading(true);
        DebugTool.logD("RESULT: " + smsEnabled + "    ||   mmsEnabled: " + mmsEnabled + "   ||   voiceEnabled: " + voiceEnabled);
        ApiManager.getAvailablePhoneNumbers(context,
                countryCode,
                phoneNumber,
                smsEnabled,
                mmsEnabled,
                voiceEnabled,
                new Callback<AvailablePhoneNumbers>() {
                    @Override
                    public void onResponse(Call<AvailablePhoneNumbers> call, Response<AvailablePhoneNumbers> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().availableAvailablePhoneNumbers != null && !response.body().availableAvailablePhoneNumbers.isEmpty()) {
                            mView.showResultSearchNumber(response.body().availableAvailablePhoneNumbers);
                        } else {
                            mView.emptyPhoneNumberAvailable();
                        }
                        mView.showLoading(false);
                    }

                    @Override
                    public void onFailure(Call<AvailablePhoneNumbers> call, Throwable t) {
                        mView.showLoading(false);
                        mView.emptyPhoneNumberAvailable();
                    }
                });
    }

    @Override
    public void start() {

    }
}
