package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.data.repository.call.source.CallDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetCallsIncoming extends UseCase<GetCallsIncoming.RequestValue, GetCallsIncoming.ResponseValue> {

    private final CallRepository mVoiceRepository;

    public GetCallsIncoming(@NonNull CallRepository messageRepository) {
        mVoiceRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.getCallsIncoming(requestValue.getPhoneNumberIncoming(), new CallDataSource.LoadCallCallback() {
            @Override
            public void onCallLoaded(Calls calls) {
                getUseCaseCallback().onSuccess(new ResponseValue(calls));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mPhoneNumberIncoming;

        public RequestValue(String phoneNumberIncoming) {
            this.mPhoneNumberIncoming = phoneNumberIncoming;
        }

        public String getPhoneNumberIncoming() {
            return mPhoneNumberIncoming;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Calls mCalls;

        public ResponseValue(@NonNull Calls calls) {
            mCalls = calls;
        }

        public Calls getCalls() {
            return mCalls;
        }
    }
}
