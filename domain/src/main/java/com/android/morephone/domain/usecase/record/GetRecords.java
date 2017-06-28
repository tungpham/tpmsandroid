package com.android.morephone.domain.usecase.record;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.repository.record.RecordRepository;
import com.android.morephone.data.repository.record.source.RecordDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetRecords extends UseCase<GetRecords.RequestValue, GetRecords.ResponseValue>{

    private final RecordRepository mRecordRepository;

    public GetRecords(@NonNull RecordRepository recordRepository) {
        mRecordRepository = recordRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mRecordRepository.getRecords(new RecordDataSource.LoadRecordsCallback() {
            @Override
            public void onRecordsLoaded(RecordListResourceResponse recordListResourceResponse) {
                getUseCaseCallback().onSuccess(new ResponseValue(recordListResourceResponse));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        public RequestValue() {
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final RecordListResourceResponse mRecordListResourceResponse;

        public ResponseValue(@NonNull RecordListResourceResponse recordListResourceResponse) {
            mRecordListResourceResponse = recordListResourceResponse;
        }

        public RecordListResourceResponse getRecordListResourceResponse(){
            return mRecordListResourceResponse;
        }
    }
}
