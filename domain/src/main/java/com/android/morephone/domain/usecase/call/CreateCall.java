package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.data.repository.call.source.CallDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class CreateCall extends UseCase<CreateCall.RequestValue, CreateCall.ResponseValue> {

    private final CallRepository mVoiceRepository;

    public CreateCall(@NonNull CallRepository voiceRepository) {
        mVoiceRepository = voiceRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.createCall(
                requestValue.getPhoneNumberIncoming(),
                requestValue.getPhoneNumberOutgoing(),
                requestValue.getApplicationSid(),
                requestValue.getSipAuthUsername(),
                requestValue.getSipAuthPassword(),
                new CallDataSource.GetCallCallback() {
                    @Override
                    public void onCallLoaded(Call call) {
                        getUseCaseCallback().onSuccess(new ResponseValue(call));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        getUseCaseCallback().onError();
                    }
                });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mPhoneNumberIncoming;
        private final String mPhoneNumberOutgoing;
        private final String mApplicationSid;
        private final String mSipAuthUsername;
        private final String mSipAuthPassword;

        public RequestValue(String phoneNumberIncoming,
                            String phoneNumberOutgoing,
                            String applicationSid,
                            String sipAuthUsername,
                            String sipAuthPassword) {
            mPhoneNumberIncoming = phoneNumberIncoming;
            mPhoneNumberOutgoing = phoneNumberOutgoing;
            mApplicationSid = applicationSid;
            mSipAuthUsername = sipAuthUsername;
            mSipAuthPassword = sipAuthPassword;
        }

        public String getPhoneNumberIncoming() {
            return mPhoneNumberIncoming;
        }

        public String getPhoneNumberOutgoing() {
            return mPhoneNumberOutgoing;
        }

        public String getApplicationSid() {
            return mApplicationSid;
        }

        public String getSipAuthUsername() {
            return mSipAuthUsername;
        }

        public String getSipAuthPassword() {
            return mSipAuthPassword;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Call mCall;

        public ResponseValue(@NonNull Call call) {
            mCall = call;
        }

        public Call getCall() {
            return mCall;
        }
    }
}
