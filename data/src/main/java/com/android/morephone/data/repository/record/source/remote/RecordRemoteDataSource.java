package com.android.morephone.data.repository.record.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.repository.record.source.RecordDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/15/17.
 */

public class RecordRemoteDataSource implements RecordDataSource {

    private static RecordRemoteDataSource INSTANCE;

    private Context mContext;

    private RecordRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static RecordRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new RecordRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getRecords(String accountSid, @NonNull LoadRecordCallback callback) {

    }

    @Override
    public void getRecords(String accountSid, String callSid, @NonNull final LoadRecordCallback callback) {
        ApiManager.getRecordListResource(mContext, accountSid, callSid, new Callback<RecordListResourceResponse>() {
            @Override
            public void onResponse(Call<RecordListResourceResponse> call, Response<RecordListResourceResponse> response) {
                if (response.isSuccessful()) {
                    RecordListResourceResponse recordListResourceResponse = response.body();
                    if (recordListResourceResponse != null) {
                        callback.onRecordLoaded(recordListResourceResponse);
                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<RecordListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getRecord(String accountSid, String callSid, String recordSid, @NonNull GetRecordCallback callback) {

    }

    @Override
    public void createRecord(String accountSid, String callSid, String url, @NonNull GetRecordCallback callback) {

    }

    @Override
    public void deleteRecord(String accountSid, String callSid, String recordSid) {

    }
}
