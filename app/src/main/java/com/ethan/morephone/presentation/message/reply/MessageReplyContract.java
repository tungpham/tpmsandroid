package com.ethan.morephone.presentation.message.reply;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 7/28/17.
 */

public interface MessageReplyContract {

    interface View extends BaseView<Presenter> {

        void createMessageSuccess(MessageItem messageItem);

        void createMessageError();

        void showLoading(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void createMessage(String userId, String to, String from, String body, int position);

    }

}
