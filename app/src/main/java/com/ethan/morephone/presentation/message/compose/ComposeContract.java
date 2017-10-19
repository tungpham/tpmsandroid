package com.ethan.morephone.presentation.message.compose;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 3/4/17.
 */

public interface ComposeContract {

    interface View extends BaseView<Presenter> {
        void createMessageSuccess(MessageItem messageItem);

        void createMessageError();

        void showLoading(boolean isActive);
    }

    interface Presenter extends BasePresenter {

        void createMessage(String userId, String groupId, long dateSent, String to, String from, String body, int position);

    }
}
