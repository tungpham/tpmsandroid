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

public class GetVoicesOutgoing extends UseCase<GetVoicesOutgoing.RequestValue, GetVoicesOutgoing.ResponseValue> {

    private final VoiceRepository mVoiceRepository;

    public GetVoicesOutgoing(@NonNull VoiceRepository messageRepository) {
        mVoiceRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.getVoicesOutgoing(requestValue.getPhoneNumberOutgoing(), new VoiceDataSource.LoadVoiceCallback() {
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

        private final String mPhoneNumberOutgoing;

        public RequestValue(String phoneNumberOutgoing) {
            this.mPhoneNumberOutgoing = phoneNumberOutgoing;
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

        public List<VoiceItem> getVoiceItems() {
            return mVoiceItems;
        }
    }
}
