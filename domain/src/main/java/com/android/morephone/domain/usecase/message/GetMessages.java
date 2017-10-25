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

public class GetMessages extends UseCase<GetMessages.RequestValue, GetMessages.ResponseValue>{

    private final MessageRepository mMessageRepository;

    public GetMessages(@NonNull MessageRepository messageRepository) {
        mMessageRepository = messageRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mMessageRepository.getMessages(requestValue.getTo(), requestValue.getFrom(), new MessageDataSource.LoadMessagesCallback() {
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

        private final String mTo;
        private final String mFrom;

        public RequestValue(String to, String from) {
            this.mTo = to;
            this.mFrom = from;
        }

        public String getTo() {
            return mTo;
        }

        public String getFrom() {
            return mFrom;
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
