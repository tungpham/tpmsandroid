package com.android.morephone.data.repository.voice.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.entity.twilio.voice.VoiceListResourceResponse;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.repository.voice.source.VoiceDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 3/10/17.
 */

public class VoiceRemoteDataSource implements VoiceDataSource {

    private static VoiceRemoteDataSource INSTANCE;

    private Context mContext;

    private VoiceRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static VoiceRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new VoiceRemoteDataSource(context);
        }
        return INSTANCE;
    }


    @Override
    public void getVoices(@NonNull final LoadVoiceCallback callback) {
        ApiManager.getAllVoice(mContext, new Callback<VoiceListResourceResponse>() {
            @Override
            public void onResponse(Call<VoiceListResourceResponse> call, Response<VoiceListResourceResponse> response) {
                if (response.isSuccessful()) {
                    VoiceListResourceResponse voiceListResourceResponse = response.body();
                    if (voiceListResourceResponse != null && voiceListResourceResponse.calls != null && !voiceListResourceResponse.calls.isEmpty()) {
                        callback.onVoiceLoaded(voiceListResourceResponse.calls);

                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<VoiceListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getVoices(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull final LoadVoiceCallback callback) {
        ApiManager.getVoices(mContext, phoneNumberIncoming, phoneNumberOutgoing, new Callback<VoiceListResourceResponse>() {
            @Override
            public void onResponse(Call<VoiceListResourceResponse> call, Response<VoiceListResourceResponse> response) {
                if (response.isSuccessful()) {
                    VoiceListResourceResponse voiceListResourceResponse = response.body();
                    if (voiceListResourceResponse != null && voiceListResourceResponse.calls != null && !voiceListResourceResponse.calls.isEmpty()) {
                        callback.onVoiceLoaded(voiceListResourceResponse.calls);
                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<VoiceListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getVoicesIncoming(String phoneNumberIncoming, @NonNull final LoadVoiceCallback callback) {
        ApiManager.getVoicesIncoming(mContext, phoneNumberIncoming, new Callback<VoiceListResourceResponse>() {
            @Override
            public void onResponse(Call<VoiceListResourceResponse> call, Response<VoiceListResourceResponse> response) {
                if (response.isSuccessful()) {
                    VoiceListResourceResponse voiceListResourceResponse = response.body();
                    if (voiceListResourceResponse != null && voiceListResourceResponse.calls != null && !voiceListResourceResponse.calls.isEmpty()) {
                        callback.onVoiceLoaded(voiceListResourceResponse.calls);
                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<VoiceListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getVoicesOutgoing(String phoneNumberOutgoing, @NonNull final LoadVoiceCallback callback) {
        ApiManager.getVoicesOutgoing(mContext, phoneNumberOutgoing, new Callback<VoiceListResourceResponse>() {
            @Override
            public void onResponse(Call<VoiceListResourceResponse> call, Response<VoiceListResourceResponse> response) {
                if (response.isSuccessful()) {
                    VoiceListResourceResponse voiceListResourceResponse = response.body();
                    if (voiceListResourceResponse != null && voiceListResourceResponse.calls != null && !voiceListResourceResponse.calls.isEmpty()) {
                        callback.onVoiceLoaded(voiceListResourceResponse.calls);
                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<VoiceListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
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
                            @NonNull final GetVoiceCallback callback) {
        ApiManager.createVoice(mContext, phoneNumberOutgoing, phoneNumberIncoming, applicationSid, sipAuthUsername, sipAuthPassword, new Callback<VoiceItem>() {
            @Override
            public void onResponse(Call<VoiceItem> call, Response<VoiceItem> response) {
                if (response.isSuccessful()) {
                    callback.onVoiceLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<VoiceItem> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteVoice(String callsid) {
        ApiManager.deleteVoice(mContext, callsid);
    }
}
