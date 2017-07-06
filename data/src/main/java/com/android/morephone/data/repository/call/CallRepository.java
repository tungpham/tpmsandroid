package com.android.morephone.data.repository.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.call.source.CallDataSource;

/**
 * Created by Ethan on 3/7/17.
 */

public class CallRepository implements CallDataSource {

    private static CallRepository INSTANCE = null;

    private final CallDataSource mCallRemoteDataSource;

    private CallRepository(@NonNull CallDataSource voiceRemoteDataSource) {
        mCallRemoteDataSource = voiceRemoteDataSource;
    }

    public static CallRepository getInstance(CallDataSource voiceRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new CallRepository(voiceRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getCalls(@NonNull LoadCallCallback callback) {
        mCallRemoteDataSource.getCalls(callback);
    }

    @Override
    public void getCalls(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull LoadCallCallback callback) {
        mCallRemoteDataSource.getCalls(phoneNumberIncoming, phoneNumberOutgoing, callback);
    }

    @Override
    public void getCallsIncoming(String phoneNumber, int page, @NonNull LoadCallCallback callback) {
        mCallRemoteDataSource.getCallsIncoming(phoneNumber, page, callback);
    }

    @Override
    public void getCallsOutgoing(String phoneNumber, int page, @NonNull LoadCallCallback callback) {
        mCallRemoteDataSource.getCallsOutgoing(phoneNumber, page, callback);
    }

    @Override
    public void getCall(String callSid, @NonNull GetCallCallback callback) {
        mCallRemoteDataSource.getCall(callSid, callback);
    }

    @Override
    public void createCall(String phoneNumberIncoming,
                           String phoneNumberOutgoing,
                           String applicationSid,
                           String sipAuthUsername,
                           String sipAuthPassword,
                           @NonNull GetCallCallback callback) {
        mCallRemoteDataSource.createCall(phoneNumberIncoming, phoneNumberOutgoing, applicationSid, sipAuthUsername, sipAuthPassword, callback);
    }

    @Override
    public void deleteCall(String callsid) {
        mCallRemoteDataSource.deleteCall(callsid);
    }
}
