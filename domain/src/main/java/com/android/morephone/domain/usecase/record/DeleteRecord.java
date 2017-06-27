package com.android.morephone.domain.usecase.record;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.record.RecordRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class DeleteRecord extends UseCase<DeleteRecord.RequestValue, DeleteRecord.ResponseValue>{

    private final RecordRepository mRecordRepository;

    public DeleteRecord(@NonNull RecordRepository recordRepository) {
        mRecordRepository = recordRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mRecordRepository.deleteRecord(requestValue.getAccountSid(), requestValue.getRecordSid());
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mAccountSid;
        private final String mCallSid;
        private final String mRecordSid;

        public RequestValue(String accountSid, String callSid, String recordSid) {
            mAccountSid = accountSid;
            mCallSid = callSid;
            mRecordSid = recordSid;
        }

        public String getAccountSid() {
            return mAccountSid;
        }

        public String getCallSid() {
            return mCallSid;
        }

        public String getRecordSid() {
            return mRecordSid;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}
