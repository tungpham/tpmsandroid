package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.data.repository.call.source.CallDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetCallsOutgoing extends UseCase<GetCallsOutgoing.RequestValue, GetCallsOutgoing.ResponseValue> {

    private final CallRepository mVoiceRepository;

    public GetCallsOutgoing(@NonNull CallRepository messageRepository) {
        mVoiceRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.getCallsOutgoing(requestValue.getPhoneNumberOutgoing(), requestValue.getPage(), new CallDataSource.LoadCallCallback() {
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

        private final String mPhoneNumberOutgoing;
        private final int mPage;

        public RequestValue(String phoneNumberOutgoing, int page) {
            this.mPhoneNumberOutgoing = phoneNumberOutgoing;
            this.mPage = page;
        }

        public String getPhoneNumberOutgoing() {
            return mPhoneNumberOutgoing;
        }

        public int getPage(){
            return mPage;
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
