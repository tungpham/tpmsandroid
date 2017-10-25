package com.android.morephone.domain.usecase.message;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.message.MessageRepository;
import com.android.morephone.data.repository.message.source.MessageDataSource;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by Ethan on 3/3/17.
 */

public class GetAllMessages extends UseCase<GetAllMessages.RequestValue, GetAllMessages.ResponseValue>{

    private final MessageRepository mMessageRepository;

    public GetAllMessages(@NonNull MessageRepository messageRepository) {
        mMessageRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mMessageRepository.getMessages(new MessageDataSource.LoadMessagesCallback() {
            @Override
            public void onMessagesLoaded(List<MessageItem> messageItems, int statusCode) {
                DebugTool.logD("SIZE: " + messageItems.size());
                getUseCaseCallback().onSuccess(new ResponseValue(messageItems));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        public RequestValue() {
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<MessageItem> mMessageItems;

        public ResponseValue(@NonNull List<MessageItem> messageItems) {
            mMessageItems = messageItems;
        }

        public List<MessageItem> getMessageItems(){
            return mMessageItems;
        }
    }
}
