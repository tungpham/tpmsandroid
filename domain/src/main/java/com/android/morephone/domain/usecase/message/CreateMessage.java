package com.android.morephone.domain.usecase.message;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.data.repository.message.source.MessageDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class CreateMessage extends UseCase<CreateMessage.RequestValue, CreateMessage.ResponseValue>{

    private final MessageRepository mMessageRepository;

    public CreateMessage(@NonNull MessageRepository messageRepository) {
        mMessageRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mMessageRepository.createMessage(requestValue.getPhoneNumberTo(), requestValue.getPhoneNumberFrom(), requestValue.getBody(), new MessageDataSource.GetMessageCallback() {
            @Override
            public void onMessageLoaded(MessageItem messageItem) {
                getUseCaseCallback().onSuccess(new ResponseValue(messageItem));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mPhoneNumberTo;
        private final String mPhoneNumberFrom;
        private final String mBody;

        public RequestValue(String phoneNumberTo, String phoneNumberFrom, String body) {
            mPhoneNumberTo = phoneNumberTo;
            mPhoneNumberFrom = phoneNumberFrom;
            mBody = body;
        }

        public String getPhoneNumberTo() {
            return mPhoneNumberTo;
        }

        public String getPhoneNumberFrom() {
            return mPhoneNumberFrom;
        }

        public String getBody() {
            return mBody;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final MessageItem mMessageItem;

        public ResponseValue(@NonNull MessageItem messageItem) {
            mMessageItem = messageItem;
        }

        public MessageItem getMessageItem(){
            return mMessageItem;
        }
    }
}
