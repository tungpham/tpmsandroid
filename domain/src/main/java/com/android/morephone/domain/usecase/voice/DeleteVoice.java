package com.android.morephone.domain.usecase.voice;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.voice.VoiceRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class DeleteVoice extends UseCase<DeleteVoice.RequestValue, DeleteVoice.ResponseValue>{

    private final VoiceRepository mVoiceRepository;

    public DeleteVoice(@NonNull VoiceRepository voiceRepository) {
        mVoiceRepository = voiceRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mVoiceRepository.deleteVoice(requestValue.getCallSid());
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
