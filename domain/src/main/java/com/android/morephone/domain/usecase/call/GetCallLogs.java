package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.repository.voice.VoiceRepository;
import com.android.morephone.data.repository.voice.source.VoiceDataSource;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetCallLogs extends UseCase<GetCallLogs.RequestValue, GetCallLogs.ResponseValue> {

    private final VoiceRepository mVoiceRepository;

    public GetCallLogs(@NonNull VoiceRepository messageRepository) {
        mVoiceRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.getVoicesIncoming(requestValue.getPhoneNumberIncoming(), new VoiceDataSource.LoadVoiceCallback() {
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

        public RequestValue(String phoneNumberIncoming) {
            this.mPhoneNumberIncoming = phoneNumberIncoming;
        }

        public String getPhoneNumberIncoming() {
            return mPhoneNumberIncoming;
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
