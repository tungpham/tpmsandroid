package com.android.morephone.domain.usecase.call;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.repository.call.CallRepository;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetCallLogs extends UseCase<GetCallLogs.RequestValue, GetCallLogs.ResponseValue> {

    private final CallRepository mCallRepository;

    public GetCallLogs(@NonNull CallRepository messageRepository) {
        mCallRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
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
