package com.android.morephone.domain.usecase.message;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class DeleteMessage extends UseCase<DeleteMessage.RequestValue, DeleteMessage.ResponseValue>{

    private final MessageRepository mMessageRepository;

    public DeleteMessage(@NonNull MessageRepository messageRepository) {
        mMessageRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mMessageRepository.deleteMessage(requestValue.getMessageSid());
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mMessageSid;

        public RequestValue(String messagesid) {
            mMessageSid = messagesid;
        }

        public String getMessageSid() {
            return mMessageSid;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}
