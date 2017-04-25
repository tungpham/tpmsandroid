package com.android.morephone.data.repository.record.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;

/**
 * Created by Ethan on 4/15/17.
 */

public interface RecordDataSource {

    interface LoadRecordsCallback {
        void onRecordsLoaded(RecordListResourceResponse recordListResourceResponse);

        void onDataNotAvailable();
    }

    interface GetRecordCallback {
        void onRecordLoaded(RecordItem recordItem);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getRecords(String accountSid, @NonNull LoadRecordsCallback callback);

    void getRecords(String accountSid, String callSid, @NonNull LoadRecordsCallback callback);

    void getRecord(String accountSid, String callSid, String recordSid, @NonNull RecordDataSource.GetRecordCallback callback);

    void createRecord(String accountSid, String callSid, String url, @NonNull RecordDataSource.GetRecordCallback callback);

    void deleteRecord(String accountSid, String callSid, String recordSid);

    void deleteRecord(String accountSid, String recordSid);
}
