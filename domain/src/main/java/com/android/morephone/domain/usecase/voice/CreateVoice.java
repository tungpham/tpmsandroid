package com.android.morephone.domain.usecase.voice;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.repository.voice.VoiceRepository;
import com.android.morephone.data.repository.voice.source.VoiceDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class CreateVoice extends UseCase<CreateVoice.RequestValue, CreateVoice.ResponseValue> {

    private final VoiceRepository mVoiceRepository;

    public CreateVoice(@NonNull VoiceRepository voiceRepository) {
        mVoiceRepository = voiceRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.createVoice(
                requestValue.getPhoneNumberIncoming(),
                requestValue.getPhoneNumberOutgoing(),
                requestValue.getApplicationSid(),
                requestValue.getSipAuthUsername(),
                requestValue.getSipAuthPassword(),
                new VoiceDataSource.GetVoiceCallback() {
                    @Override
                    public void onVoiceLoaded(VoiceItem voiceItem) {
                        getUseCaseCallback().onSuccess(new ResponseValue(voiceItem));
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

        private final VoiceItem mVoiceItem;

        public ResponseValue(@NonNull VoiceItem voiceItem) {
            mVoiceItem = voiceItem;
        }

        public VoiceItem getVoiceItem() {
            return mVoiceItem;
        }
    }
}
