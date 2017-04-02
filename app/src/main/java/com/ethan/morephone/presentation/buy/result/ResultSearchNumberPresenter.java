package com.ethan.morephone.presentation.buy.result;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumbers;
import com.android.morephone.data.network.ApiManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/2/17.
 */

public class ResultSearchNumberPresenter implements ResultSearchNumberContract.Presenter {

    private ResultSearchNumberContract.View mView;

    public ResultSearchNumberPresenter(@NonNull ResultSearchNumberContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void searchPhoneNumber(Context context, String countryCode) {
        mView.showLoading(true);
        ApiManager.getAvailablePhoneNumbers(context, countryCode, new Callback<AvailablePhoneNumbers>() {
            @Override
            public void onResponse(Call<AvailablePhoneNumbers> call, Response<AvailablePhoneNumbers> response) {
                if(response.isSuccessful()){
                    mView.showResultSearchNumber(response.body().availablePhoneNumbers);
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<AvailablePhoneNumbers> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void start() {

    }
}
