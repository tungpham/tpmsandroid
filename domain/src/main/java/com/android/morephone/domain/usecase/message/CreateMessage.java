package com.android.morephone.domain.usecase.message;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.data.repository.message.source.MessageDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class CreateMessage extends UseCase<CreateMessage.RequestValue, CreateMessage.ResponseValue> {

    private final MessageRepository mMessageRepository;

    public CreateMessage(@NonNull MessageRepository messageRepository) {
        mMessageRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mMessageRepository.createMessage(requestValue.getUserId(),
                requestValue.getGroupId(),
                requestValue.getDateSent(),
                requestValue.getPhoneNumberTo(),
                requestValue.getPhoneNumberFrom(),
                requestValue.getBody(), new MessageDataSource.GetMessageCallback() {
            @Override
            public void onMessageLoaded(MessageItem messageItem, int statusCode) {
                getUseCaseCallback().onSuccess(new ResponseValue(messageItem, statusCode));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mUserId;
        private final String mPhoneNumberTo;
        private final String mPhoneNumberFrom;
        private final String mGroupId;
        private final long mDateSent;
        private final String mBody;

        public RequestValue(String userId, String groupId, long dateSent, String phoneNumberTo, String phoneNumberFrom,  String body) {
            mUserId = userId;
            mPhoneNumberTo = phoneNumberTo;
            mPhoneNumberFrom = phoneNumberFrom;
            mGroupId = groupId;
            mDateSent = dateSent;
            mBody = body;
        }

        public String getUserId() {
            return mUserId;
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

        public String getGroupId() {
            return mGroupId;
        }

        public long getDateSent() {
            return mDateSent;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final MessageItem mMessageItem;
        private final int mStatusCode;

        public ResponseValue(@NonNull MessageItem messageItem, @NonNull int statusCode) {
            mMessageItem = messageItem;
            mStatusCode = statusCode;
        }

        public MessageItem getMessageItem() {
            return mMessageItem;
        }

        public int getStatusCode() {
            return mStatusCode;
        }
    }
}
