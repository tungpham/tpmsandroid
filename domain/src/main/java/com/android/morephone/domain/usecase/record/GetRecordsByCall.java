package com.android.morephone.domain.usecase.record;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.repository.record.RecordRepository;
import com.android.morephone.data.repository.record.source.RecordDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetRecordsByCall extends UseCase<GetRecordsByCall.RequestValue, GetRecordsByCall.ResponseValue>{

    private final RecordRepository mRecordRepository;

    public GetRecordsByCall(@NonNull RecordRepository recordRepository) {
        mRecordRepository = recordRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mRecordRepository.getRecords(requestValue.getAccountSid(), requestValue.getCallSid(), new RecordDataSource.LoadRecordsCallback() {
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

        private final String mAccountSid;
        private final String mCallSid;

        public RequestValue(String accountSid, String callSid) {
            this.mAccountSid = accountSid;
            this.mCallSid = callSid;
        }

        public String getAccountSid() {
            return mAccountSid;
        }

        public String getCallSid() {
            return mCallSid;
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
