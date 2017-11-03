package com.ethan.morephone.presentation.buy.pool;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.network.ApiMorePhone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/2/17.
 */

public class PoolPhoneNumberPresenter implements PoolPhoneNumberContract.Presenter {

    private PoolPhoneNumberContract.View mView;

    public PoolPhoneNumberPresenter(@NonNull PoolPhoneNumberContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void searchPhoneNumber(Context context) {
        mView.showLoading(true);
        ApiMorePhone.getPoolPhoneNumber(context, new Callback<BaseResponse<List<PhoneNumber>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<PhoneNumber>>> call, Response<BaseResponse<List<PhoneNumber>>> response) {
                if(response.isSuccessful() && response.body() != null && response.body().getResponse() != null){
                    mView.showResultSearchNumber(response.body().getResponse());
                }else{
                    mView.emptyPhoneNumber();
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<List<PhoneNumber>>> call, Throwable t) {
                mView.showLoading(false);
                mView.emptyPhoneNumber();
            }
        });
    }

    @Override
    public void start() {

    }
}
