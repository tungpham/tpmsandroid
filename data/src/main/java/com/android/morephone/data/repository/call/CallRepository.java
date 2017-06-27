package com.android.morephone.data.repository.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.call.source.CallDataSource;

/**
 * Created by Ethan on 3/7/17.
 */

public class CallRepository implements CallDataSource {

    private static CallRepository INSTANCE = null;

    private final CallDataSource mVoiceRemoteDataSource;

    private CallRepository(@NonNull CallDataSource voiceRemoteDataSource) {
        mVoiceRemoteDataSource = voiceRemoteDataSource;
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
        mVoiceRemoteDataSource.getCalls(callback);
    }

    @Override
    public void getCalls(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull LoadCallCallback callback) {
        mVoiceRemoteDataSource.getCalls(phoneNumberIncoming, phoneNumberOutgoing, callback);
    }

    @Override
    public void getCallsIncoming(String phoneNumber, @NonNull LoadCallCallback callback) {
        mVoiceRemoteDataSource.getCallsIncoming(phoneNumber, callback);
    }

    @Override
    public void getCallsOutgoing(String phoneNumber, @NonNull LoadCallCallback callback) {
        mVoiceRemoteDataSource.getCallsOutgoing(phoneNumber, callback);
    }

    @Override
    public void getCall(String messageSid, @NonNull GetCallCallback callback) {

    }

    @Override
    public void createCall(String phoneNumberIncoming,
                           String phoneNumberOutgoing,
                           String applicationSid,
                           String sipAuthUsername,
                           String sipAuthPassword,
                           @NonNull GetCallCallback callback) {
        mVoiceRemoteDataSource.createCall(phoneNumberIncoming, phoneNumberOutgoing, applicationSid, sipAuthUsername, sipAuthPassword, callback);
    }

    @Override
    public void deleteCall(String callsid) {
        mVoiceRemoteDataSource.deleteCall(callsid);
    }
}
