package com.android.morephone.data.repository.usage.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.usage.Usage;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.repository.usage.source.UsageDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 7/14/17.
 */

public class UsageRemoteDataSource implements UsageDataSource {

    private static UsageRemoteDataSource INSTANCE;

    private Context mContext;

    private UsageRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static UsageRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new UsageRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getUsageAllTime(String category, int page, String pageToken, @NonNull final GetUsageCallback callback) {
        ApiManager.getUsageAllTime(mContext, category, page, pageToken, new Callback<Usage>() {
            @Override
            public void onResponse(Call<Usage> call, Response<Usage> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onUsageLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<Usage> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }
}
