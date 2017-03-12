package com.android.morephone.data.repository.voice.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;

import java.util.List;

/**
 * Created by Ethan on 3/7/17.
 */

public interface VoiceDataSource {

    interface LoadVoiceCallback {
        void onVoiceLoaded(List<VoiceItem> voiceItems);

        void onDataNotAvailable();
    }

    interface GetVoiceCallback {
        void onVoiceLoaded(VoiceItem voiceItem);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getVoices(@NonNull VoiceDataSource.LoadVoiceCallback callback);

    void getVoices(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull VoiceDataSource.LoadVoiceCallback callback);

    void getVoicesIncoming(String phoneNumber, @NonNull VoiceDataSource.LoadVoiceCallback callback);

    void getVoicesOutgoing(String phoneNumber, @NonNull VoiceDataSource.LoadVoiceCallback callback);

    void getVoice(String messageSid, @NonNull VoiceDataSource.GetVoiceCallback callback);

    void createVoice(String phoneNumberIncoming,
                     String phoneNumberOutgoing,
                     String applicationSid,
                     String sipAuthUsername,
                     String sipAuthPassword,
                     @NonNull VoiceDataSource.GetVoiceCallback callback);

    void deleteVoice(String callsid);

}
