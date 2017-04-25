package com.android.morephone.data.repository.call.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.entity.twilio.record.RecordItem;

/**
 * Created by Ethan on 4/25/17.
 */

public interface CallDataSource {

    interface LoadCallsCallback {
        void onCallsLoaded(Calls calls);

        void onDataNotAvailable();
    }

    interface GetCallCallback {
        void onRecordLoaded(RecordItem recordItem);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getCalls(String accountSid, String phoneNumber, @NonNull CallDataSource.LoadCallsCallback callback);


}
