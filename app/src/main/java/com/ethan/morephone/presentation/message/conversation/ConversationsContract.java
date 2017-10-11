package com.ethan.morephone.presentation.message.conversation;

import android.content.Context;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public interface ConversationsContract {

    interface View extends BaseView<Presenter> {

        void showListMessage(List<ConversationModel> conversationModels);

        void showLoading(boolean isActive);

        void createMessageSuccess(MessageItem messageItem);

        void createMessageError();

    }

    interface Presenter extends BasePresenter {

        void loadListMessageResource(Context context, String phoneNumber, boolean isShowLoading);

        void loadMessagesIncoming(String phoneNumberIncoming);

        void loadMessageOutgoing(String phoneNumberOutgoing);

        void clearData();

        void parseFakeData(FakeData fakeData, String phoneNumber);

        void createMessage(String userId, String to, String from, String body, int position);

        void createMessageGroup(Context context, MessageGroup messageGroup);

        boolean hasNextPage();

    }
}
