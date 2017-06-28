package com.android.morephone.data.repository.call.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.repository.call.source.CallDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 3/10/17.
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
    public void getCalls(@NonNull final LoadCallCallback callback) {
        ApiManager.getAllCalls(mContext, new Callback<Calls>() {
            @Override
            public void onResponse(Call<Calls> call, Response<Calls> response) {
                if (response.isSuccessful()) {
                    Calls calls = response.body();
                    if (calls != null) {
                        callback.onCallLoaded(calls);

                    } else {
                        callback.onDataNotAvailable();
                    }
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

    @Override
    public void getCalls(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull final LoadCallCallback callback) {
        ApiManager.getCalls(mContext, phoneNumberIncoming, phoneNumberOutgoing, new Callback<Calls>() {
            @Override
            public void onResponse(Call<Calls> call, Response<Calls> response) {
                if (response.isSuccessful()) {
                    Calls calls = response.body();
                    if (calls != null) {
                        callback.onCallLoaded(calls);
                    } else {
                        callback.onDataNotAvailable();
                    }
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

    @Override
    public void getCallsIncoming(String phoneNumberIncoming, @NonNull final LoadCallCallback callback) {
        ApiManager.getCallsIncoming(mContext, phoneNumberIncoming, new Callback<Calls>() {
            @Override
            public void onResponse(Call<Calls> call, Response<Calls> response) {
                if (response.isSuccessful()) {
                    Calls calls = response.body();
                    if (calls != null) {
                        callback.onCallLoaded(calls);
                    } else {
                        callback.onDataNotAvailable();
                    }
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

    @Override
    public void getCallsOutgoing(String phoneNumberOutgoing, @NonNull final LoadCallCallback callback) {
        ApiManager.getCallsOutgoing(mContext, phoneNumberOutgoing, new Callback<Calls>() {
            @Override
            public void onResponse(Call<Calls> call, Response<Calls> response) {
                if (response.isSuccessful()) {
                    Calls calls = response.body();
                    if (calls != null) {
                        callback.onCallLoaded(calls);
                    } else {
                        callback.onDataNotAvailable();
                    }
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

    @Override
    public void getCall(String callSid, final @NonNull GetCallCallback callback) {
        ApiManager.getCall(mContext, callSid, new Callback<com.android.morephone.data.entity.call.Call>() {
            @Override
            public void onResponse(Call<com.android.morephone.data.entity.call.Call> callResponse, Response<com.android.morephone.data.entity.call.Call> response) {
                if (response.isSuccessful()) {
                    com.android.morephone.data.entity.call.Call call = response.body();
                    if (call != null) {
                        callback.onCallLoaded(call);
                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<com.android.morephone.data.entity.call.Call> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void createCall(String phoneNumberIncoming,
                           String phoneNumberOutgoing,
                           String applicationSid,
                           String sipAuthUsername,
                           String sipAuthPassword,
                           @NonNull final GetCallCallback callback) {
        ApiManager.createVoice(mContext, phoneNumberOutgoing, phoneNumberIncoming, applicationSid, sipAuthUsername, sipAuthPassword, new Callback<com.android.morephone.data.entity.call.Call>() {
            @Override
            public void onResponse(Call<com.android.morephone.data.entity.call.Call> call, Response<com.android.morephone.data.entity.call.Call> response) {
                if (response.isSuccessful()) {
                    callback.onCallLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<com.android.morephone.data.entity.call.Call> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteCall(String callsid) {
        ApiManager.deleteVoice(mContext, callsid);
    }
}
