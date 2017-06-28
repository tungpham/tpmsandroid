package com.android.morephone.data.repository.call.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.call.Calls;

/**
 * Created by Ethan on 3/7/17.
 */

public interface CallDataSource {

    interface LoadCallCallback {
        void onCallLoaded(Calls calls);

        void onDataNotAvailable();
    }

    interface GetCallCallback {
        void onCallLoaded(Call call);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getCalls(@NonNull LoadCallCallback callback);

    void getCalls(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull LoadCallCallback callback);

    void getCallsIncoming(String phoneNumber, @NonNull LoadCallCallback callback);

    void getCallsOutgoing(String phoneNumber, @NonNull LoadCallCallback callback);

    void getCall(String callSid, @NonNull GetCallCallback callback);

    void createCall(String phoneNumberIncoming,
                    String phoneNumberOutgoing,
                    String applicationSid,
                    String sipAuthUsername,
                    String sipAuthPassword,
                    @NonNull GetCallCallback callback);

    void deleteCall(String callsid);

}
