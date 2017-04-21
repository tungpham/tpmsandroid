package com.android.morephone.data.repository.record;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.record.source.RecordDataSource;

/**
 * Created by Ethan on 4/15/17.
 */

public class RecordRepository implements RecordDataSource {

    private static RecordRepository INSTANCE = null;

    private final RecordDataSource mRecordRemoteDataSource;

    private RecordRepository(@NonNull RecordDataSource recordDataSource) {
        mRecordRemoteDataSource = recordDataSource;
    }

    public static RecordRepository getInstance(RecordDataSource recordDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new RecordRepository(recordDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getRecords(String accountSid, @NonNull LoadRecordCallback callback) {

    }

    @Override
    public void getRecords(String accountSid, String callSid, @NonNull LoadRecordCallback callback) {
        mRecordRemoteDataSource.getRecords(accountSid, callSid, callback);
    }

    @Override
    public void getRecord(String accountSid, String callSid, String recordSid, @NonNull GetRecordCallback callback) {
        mRecordRemoteDataSource.getRecord(accountSid, callSid, recordSid, callback);
    }

    @Override
    public void createRecord(String accountSid, String callSid, String url, @NonNull GetRecordCallback callback) {
        mRecordRemoteDataSource.createRecord(accountSid, callSid, url, callback);
    }

    @Override
    public void deleteRecord(String accountSid, String callSid, String recordSid) {
        mRecordRemoteDataSource.deleteRecord(accountSid, callSid, recordSid);
    }

    @Override
    public void deleteRecord(String accountSid, String recordSid) {
        mRecordRemoteDataSource.deleteRecord(accountSid, recordSid);
    }
}
