package com.ethan.morephone.presentation.message.conversation;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.GetAllMessages;
import com.android.morephone.domain.usecase.message.GetMessagesIncoming;
import com.android.morephone.domain.usecase.message.GetMessagesOutgoing;

import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public class ConversationsPresenter implements ConversationsContract.Presenter {

    private final ConversationsContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetAllMessages mGetAllMessages;
    private final GetMessagesIncoming mGetMessagesIncoming;
    private final GetMessagesOutgoing mGetMessagesOutgoing;

    public ConversationsPresenter(@NonNull ConversationsContract.View view,
                                  @NonNull UseCaseHandler useCaseHandler,
                                  @NonNull GetAllMessages getAllMessages,
                                  @NonNull GetMessagesIncoming getMessagesIncoming,
                                  @NonNull GetMessagesOutgoing getMessagesOutgoing) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetAllMessages = getAllMessages;
        mGetMessagesIncoming = getMessagesIncoming;
        mGetMessagesOutgoing = getMessagesOutgoing;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadListMessageResource() {
        mView.showLoading(true);
        GetAllMessages.RequestValue requestValue = new GetAllMessages.RequestValue();
        mUseCaseHandler.execute(mGetAllMessages, requestValue, new UseCase.UseCaseCallback<GetAllMessages.ResponseValue>() {
            @Override
            public void onSuccess(GetAllMessages.ResponseValue response) {
                List<MessageItem> messageItems = response.getMessageItems();
                for(MessageItem messageItem : messageItems) {
                    DebugTool.logD("BODY: " + messageItem.body);
                }
                mView.showListMessage(messageItems);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                DebugTool.logD("ERORR");
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadMessagesIncoming(String phoneNumberIncoming) {
        mView.showLoading(true);
        GetMessagesIncoming.RequestValue requestValue = new GetMessagesIncoming.RequestValue(phoneNumberIncoming);
        mUseCaseHandler.execute(mGetMessagesIncoming, requestValue, new UseCase.UseCaseCallback<GetMessagesIncoming.ResponseValue>() {
            @Override
            public void onSuccess(GetMessagesIncoming.ResponseValue response) {
                mView.showListMessage(response.getMessageItems());
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadMessageOutgoing(String phoneNumberOutgoing) {
        mView.showLoading(true);
        GetMessagesOutgoing.RequestValue requestValue = new GetMessagesOutgoing.RequestValue(phoneNumberOutgoing);
        mUseCaseHandler.execute(mGetMessagesOutgoing, requestValue, new UseCase.UseCaseCallback<GetMessagesOutgoing.ResponseValue>() {
            @Override
            public void onSuccess(GetMessagesOutgoing.ResponseValue response) {
                mView.showListMessage(response.getMessageItems());
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }
}
