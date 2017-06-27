package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class DeleteCall extends UseCase<DeleteCall.RequestValue, DeleteCall.ResponseValue>{

    private final CallRepository mVoiceRepository;

    public DeleteCall(@NonNull CallRepository voiceRepository) {
        mVoiceRepository = voiceRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.deleteCall(requestValue.getCallSid());
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mCallSid;

        public RequestValue(String callSid) {
            mCallSid = callSid;
        }

        public String getCallSid() {
            return mCallSid;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}
