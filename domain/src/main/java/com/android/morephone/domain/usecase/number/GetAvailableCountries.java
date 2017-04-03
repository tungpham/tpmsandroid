package com.android.morephone.domain.usecase.number;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.AvailableCountries;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.domain.UseCase;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetAvailableCountries extends UseCase<GetAvailableCountries.RequestValue, GetAvailableCountries.ResponseValue> {

    private Context mContext;

    public GetAvailableCountries(@NonNull Context context) {
        mContext = context;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        ApiManager.getAvailableCountries(mContext, new Callback<AvailableCountries>() {
            @Override
            public void onResponse(Call<AvailableCountries> call, Response<AvailableCountries> response) {
                if (response.isSuccessful()) {
                    AvailableCountries availableCountries = response.body();
                    getUseCaseCallback().onSuccess(new ResponseValue(availableCountries));
                } else {
                    getUseCaseCallback().onError();
                }
            }

            @Override
            public void onFailure(Call<AvailableCountries> call, Throwable t) {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {


        public RequestValue() {
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final AvailableCountries mAvailableCountries;

        public ResponseValue(@NonNull AvailableCountries availableCountries) {
            mAvailableCountries = availableCountries;
        }

        public AvailableCountries getAvailableCountries() {
            return mAvailableCountries;
        }
    }
}
