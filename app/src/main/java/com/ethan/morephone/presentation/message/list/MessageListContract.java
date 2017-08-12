package com.ethan.morephone.presentation.message.list;

import android.content.Context;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public interface MessageListContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showProgress(boolean isActive, int position);

        void showMessages(List<MessageItem> messageItems);

        void createMessageSuccess(MessageItem messageItem);

        void createMessageError();
    }

    interface Presenter extends BasePresenter {

        void loadMessages(String to, String from);

        void createMessage(Context context, String userId, String to, String from, String body, int position);

        void deleteMessage(String messagesid);
    }
}
