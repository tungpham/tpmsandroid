package com.ethan.morephone.presentation.message.list;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.android.morephone.domain.usecase.message.DeleteMessage;
import com.android.morephone.domain.usecase.message.GetMessages;

/**
 * Created by Ethan on 3/4/17.
 */

public class MessageListPresenter implements MessageListContract.Presenter {

    private final MessageListContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetMessages mGetMessages;
    private final CreateMessage mCreateMessage;
    private final DeleteMessage mDeleteMessage;

    public MessageListPresenter(@NonNull MessageListContract.View view,
                                @NonNull UseCaseHandler useCaseHandler,
                                @NonNull GetMessages getMessages,
                                @NonNull CreateMessage createMessage,
                                @NonNull DeleteMessage deleteMessage) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetMessages = getMessages;
        mCreateMessage = createMessage;
        mDeleteMessage = deleteMessage;

        mView.setPresenter(this);
    }

    @Override
    public void loadMessages(String to, String from) {
        mView.showLoading(true);
        GetMessages.RequestValue requestValue = new GetMessages.RequestValue(to, from);
        mUseCaseHandler.execute(mGetMessages, requestValue, new UseCase.UseCaseCallback<GetMessages.ResponseValue>() {
            @Override
            public void onSuccess(GetMessages.ResponseValue response) {
                mView.showMessages(response.getMessageItems());
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void createMessage(String userId, String to, String from, String body, final int position) {
        final MessageItem messageItem = new MessageItem(
                "",
                "",
                "",
                "",
                null,
                to,
                from,
                null,
                body,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                null);
//        mView.createMessageSuccess(messageItem);
//        mView.showProgress(true, position);
        DebugTool.logD("TO : " + to);
        DebugTool.logD("FROM : " + from);
        DebugTool.logD("BODY : " + body);
        DebugTool.logD("userId : " + userId);
        mView.createMessageSuccess(messageItem);

        CreateMessage.RequestValue requestValue = new CreateMessage.RequestValue(userId, to, from, body);
        mUseCaseHandler.execute(mCreateMessage, requestValue, new UseCase.UseCaseCallback<CreateMessage.ResponseValue>() {
            @Override
            public void onSuccess(CreateMessage.ResponseValue response) {
//                mView.showProgress(false, position);
            }

            @Override
            public void onError() {
                mView.createMessageError();
//                mView.showProgress(false, position);
            }
        });
    }

    @Override
    public void deleteMessage(String messagesid) {
        DeleteMessage.RequestValue requestValue = new DeleteMessage.RequestValue(messagesid);
        mUseCaseHandler.execute(mDeleteMessage, requestValue, new UseCase.UseCaseCallback<DeleteMessage.ResponseValue>() {
            @Override
            public void onSuccess(DeleteMessage.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void start() {

    }
}
