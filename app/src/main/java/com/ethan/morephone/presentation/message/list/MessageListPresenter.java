package com.ethan.morephone.presentation.message.list;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.HTTPStatus;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.group.CreateGroup;
import com.android.morephone.domain.usecase.message.CreateMessage;
import com.android.morephone.domain.usecase.message.DeleteMessage;
import com.android.morephone.domain.usecase.message.GetMessages;
import com.ethan.morephone.fcm.NotificationHelpper;

/**
 * Created by Ethan on 3/4/17.
 */

public class MessageListPresenter implements MessageListContract.Presenter {

    private final MessageListContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetMessages mGetMessages;
    private final CreateMessage mCreateMessage;
    private final DeleteMessage mDeleteMessage;
    private final CreateGroup mCreateGroup;

    public MessageListPresenter(@NonNull MessageListContract.View view,
                                @NonNull UseCaseHandler useCaseHandler,
                                @NonNull GetMessages getMessages,
                                @NonNull CreateMessage createMessage,
                                @NonNull DeleteMessage deleteMessage,
                                @NonNull CreateGroup createGroup) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetMessages = getMessages;
        mCreateMessage = createMessage;
        mDeleteMessage = deleteMessage;
        mCreateGroup = createGroup;

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
    public void createMessage(final Context context, String userId, String groupId, long dateSent, String to, String from, String body, final int position, boolean isResend, final boolean isGroup) {
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
        DebugTool.logD("POS: " + position + " GROUP: " + isGroup);
        if (!isGroup) {
            if (!isResend) mView.createMessageSuccess(messageItem);
            mView.showProgress(true, position);
        }

        CreateMessage.RequestValue requestValue = new CreateMessage.RequestValue(userId.trim(), groupId, dateSent, to.trim(), from.trim(), body.trim());
        mUseCaseHandler.execute(mCreateMessage, requestValue, new UseCase.UseCaseCallback<CreateMessage.ResponseValue>() {
            @Override
            public void onSuccess(CreateMessage.ResponseValue response) {
                DebugTool.logD("STATUS: " + response.getStatusCode());
                if (!isGroup) {
                    mView.showProgress(false, position);
                }
            }

            @Override
            public void onError() {
                if (!isGroup) {
                    mView.createMessageError(position);
                    mView.showProgress(false, position);
                }
            }
        });
    }

    @Override
    public void createGroup(Context context, Group group) {
        mView.showLoading(true);
        CreateGroup.RequestValues requestValue = new CreateGroup.RequestValues(group);
        mUseCaseHandler.execute(mCreateGroup, requestValue, new UseCase.UseCaseCallback<CreateGroup.ResponseValue>() {
            @Override
            public void onSuccess(CreateGroup.ResponseValue response) {
                mView.showLoading(false);
                mView.createGroupSuccess(response.getMessageGroups());
            }

            @Override
            public void onError() {
                mView.createGroupError();
                mView.showLoading(false);
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
