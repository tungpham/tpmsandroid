package com.android.morephone.data.repository.phonenumbers.incoming.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumbers;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.repository.phonenumbers.incoming.source.IncomingPhoneNumberDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/25/17.
 */

public class IncomingPhoneNumberRemoteDataSource implements IncomingPhoneNumberDataSource {


    private static IncomingPhoneNumberRemoteDataSource INSTANCE;

    private Context mContext;

    private IncomingPhoneNumberRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static IncomingPhoneNumberRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new IncomingPhoneNumberRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getIncomingPhoneNumbers(@NonNull final LoadIncomingPhoneNumbersCallback callback) {
        ApiManager.getIncomingPhoneNumbers(mContext, new Callback<IncomingPhoneNumbers>() {
            @Override
            public void onResponse(Call<IncomingPhoneNumbers> call, Response<IncomingPhoneNumbers> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onIncomingPhoneNumbersLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<IncomingPhoneNumbers> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void changeFriendlyName(String accountSid, String incomingPhoneNumberSid, String friendlyName, @NonNull final GetIncomingPhoneNumberCallback callback) {
        ApiManager.changeFriendlyName(mContext, accountSid, incomingPhoneNumberSid, friendlyName, new Callback<IncomingPhoneNumber>() {
            @Override
            public void onResponse(Call<IncomingPhoneNumber> call, Response<IncomingPhoneNumber> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onIncomingPhoneNumberLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<IncomingPhoneNumber> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void buyIncomingPhoneNumber(String phoneNumber, @NonNull final GetIncomingPhoneNumberCallback callback) {
        ApiManager.buyIncomingPhoneNumber(mContext, phoneNumber, new Callback<IncomingPhoneNumber>() {
            @Override
            public void onResponse(Call<IncomingPhoneNumber> call, Response<IncomingPhoneNumber> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onIncomingPhoneNumberLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<IncomingPhoneNumber> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }
}
