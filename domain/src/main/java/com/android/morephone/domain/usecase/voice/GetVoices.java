package com.android.morephone.domain.usecase.voice;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.repository.voice.VoiceRepository;
import com.android.morephone.data.repository.voice.source.VoiceDataSource;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetVoices extends UseCase<GetVoices.RequestValue, GetVoices.ResponseValue>{

    private final VoiceRepository mVoiceRepository;

    public GetVoices(@NonNull VoiceRepository messageRepository) {
        mVoiceRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.getVoices(requestValue.getPhoneNumberIncoming(), requestValue.getPhoneNumberOutgoing(), new VoiceDataSource.LoadVoiceCallback() {
            @Override
            public void onVoiceLoaded(List<VoiceItem> voiceItems) {
                getUseCaseCallback().onSuccess(new ResponseValue(voiceItems));
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

        public RequestValue(String phoneNumberIncoming, String phoneNumberOutgoing) {
            this.mPhoneNumberIncoming = phoneNumberIncoming;
            this.mPhoneNumberOutgoing = phoneNumberOutgoing;
        }

        public String getPhoneNumberIncoming() {
            return mPhoneNumberIncoming;
        }

        public String getPhoneNumberOutgoing() {
            return mPhoneNumberOutgoing;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<VoiceItem> mVoiceItems;

        public ResponseValue(@NonNull List<VoiceItem> voiceItems) {
            mVoiceItems = voiceItems;
        }

        public List<VoiceItem> getVoiceItems(){
            return mVoiceItems;
        }
    }
}
