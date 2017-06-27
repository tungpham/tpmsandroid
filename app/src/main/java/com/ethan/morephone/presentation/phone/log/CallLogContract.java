package com.ethan.morephone.presentation.phone.log;

import android.content.Context;

import com.android.morephone.data.entity.call.Calls;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

/**
 * Created by Ethan on 3/21/17.
 */

public interface CallLogContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showCallLog(Calls calls);

    }

    interface Presenter extends BasePresenter {

        void loadCallLogs(Context context);

        void loadCallsIncoming(String phoneNumberIncoming);

        void loadCallsOutgoing(String phoneNumberOutgoing);

    }
}
