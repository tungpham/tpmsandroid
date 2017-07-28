package com.android.morephone.domain.usecase.message;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetMessagesIncoming extends UseCase<GetMessagesIncoming.RequestValue, GetMessagesIncoming.ResponseValue>{

    private final MessageRepository mMessageRepository;

    public GetMessagesIncoming(@NonNull MessageRepository messageRepository) {
        mMessageRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        getUseCaseCallback().onSuccess(new ResponseValue(mMessageRepository.getMessagesIncoming(requestValue.getPhoneNumberIncoming())));
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

        private final MessageListResourceResponse mMessageItems;

        public ResponseValue(@NonNull MessageListResourceResponse messageItems) {
            mMessageItems = messageItems;
        }

        public MessageListResourceResponse getMessages(){
            return mMessageItems;
        }
    }
}
