package com.android.morephone.data.repository.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.call.source.CallDataSource;

/**
 * Created by Ethan on 4/25/17.
 */

public class CallRepository implements CallDataSource {

    private static CallRepository INSTANCE = null;

    private final CallDataSource mCallRemoteDataSource;

    private CallRepository(@NonNull CallDataSource callRemoteDataSource) {
        mCallRemoteDataSource = callRemoteDataSource;
    }

    public static CallRepository getInstance(CallDataSource callDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CallRepository(callDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getCalls(String accountSid, String phoneNumber, @NonNull LoadCallsCallback callback) {
        mCallRemoteDataSource.getCalls(accountSid, phoneNumber, callback);
    }
}
