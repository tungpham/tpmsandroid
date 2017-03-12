package com.android.morephone.data.repository.voice;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.voice.source.VoiceDataSource;

/**
 * Created by Ethan on 3/7/17.
 */

public class VoiceRepository implements VoiceDataSource {

    private static VoiceRepository INSTANCE = null;

    private final VoiceDataSource mVoiceRemoteDataSource;

    private VoiceRepository(@NonNull VoiceDataSource voiceRemoteDataSource) {
        mVoiceRemoteDataSource = voiceRemoteDataSource;
    }

    public static VoiceRepository getInstance(VoiceDataSource voiceRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new VoiceRepository(voiceRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getVoices(@NonNull LoadVoiceCallback callback) {
        mVoiceRemoteDataSource.getVoices(callback);
    }

    @Override
    public void getVoices(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull LoadVoiceCallback callback) {
        mVoiceRemoteDataSource.getVoices(phoneNumberIncoming, phoneNumberOutgoing, callback);
    }

    @Override
    public void getVoicesIncoming(String phoneNumber, @NonNull LoadVoiceCallback callback) {
        mVoiceRemoteDataSource.getVoicesIncoming(phoneNumber, callback);
    }

    @Override
    public void getVoicesOutgoing(String phoneNumber, @NonNull LoadVoiceCallback callback) {
        mVoiceRemoteDataSource.getVoicesOutgoing(phoneNumber, callback);
    }

    @Override
    public void getVoice(String messageSid, @NonNull GetVoiceCallback callback) {

    }

    @Override
    public void createVoice(String phoneNumberIncoming,
                            String phoneNumberOutgoing,
                            String applicationSid,
                            String sipAuthUsername,
                            String sipAuthPassword,
                            @NonNull GetVoiceCallback callback) {
        mVoiceRemoteDataSource.createVoice(phoneNumberIncoming, phoneNumberOutgoing, applicationSid, sipAuthUsername, sipAuthPassword, callback);
    }

    @Override
    public void deleteVoice(String callsid) {
        mVoiceRemoteDataSource.deleteVoice(callsid);
    }
}
