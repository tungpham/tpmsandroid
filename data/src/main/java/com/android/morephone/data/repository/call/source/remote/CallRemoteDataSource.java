package com.android.morephone.data.repository.call.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.repository.call.source.CallDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/25/17.
 */

public class CallRemoteDataSource implements CallDataSource {
    private static CallRemoteDataSource INSTANCE;

    private Context mContext;

    private CallRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static CallRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new CallRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getCalls(String accountSid, String phoneNumber, @NonNull final LoadCallsCallback callback) {
        ApiMorePhone.getCallLogs(mContext, accountSid, phoneNumber, new Callback<Calls>() {
            @Override
            public void onResponse(Call<Calls> call, Response<Calls> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onCallsLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<Calls> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }
}
