package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.data.repository.call.source.CallDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetCall extends UseCase<GetCall.RequestValue, GetCall.ResponseValue>{

    private final CallRepository mCallRepository;

    public GetCall(@NonNull CallRepository callRepository) {
        mCallRepository = callRepository;
    }


    @Override
    protected void executeUseCase(final RequestValue requestValue) {
        mCallRepository.getCall(requestValue.getCallSid(), new CallDataSource.GetCallCallback() {
            @Override
            public void onCallLoaded(Call call) {
                getUseCaseCallback().onSuccess(new ResponseValue(call, requestValue.getRecordItem()));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mCallSid;
        private final RecordItem mRecordItem;

        public RequestValue(String callSid, RecordItem recordItem) {
            this.mCallSid = callSid;
            this.mRecordItem = recordItem;
        }

        public String getCallSid() {
            return mCallSid;
        }

        public RecordItem getRecordItem() {
            return mRecordItem;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Call mCall;
        private final RecordItem mRecordItem;

        public ResponseValue(@NonNull Call call, @NonNull RecordItem recordItem) {
            mCall = call;
            mRecordItem = recordItem;
        }

        public Call getCall(){
            return mCall;
        }

        public RecordItem getRecordItem() {
            return mRecordItem;
        }
    }
}
