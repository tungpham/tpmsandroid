package com.ethan.morephone.presentation.phone.log;

import com.android.morephone.data.entity.call.Call;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 3/21/17.
 */

public interface CallLogContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void showCallLog(List<Call> calls);

    }

    interface Presenter extends BasePresenter {

        void loadCallsIncoming(String phoneNumberIncoming);

        void loadCallsOutgoing(String phoneNumberOutgoing);

        void clearData();

    }
}
