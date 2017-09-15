package com.ethan.morephone.presentation.phone.log;

import android.content.Context;

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

        void loadCalls(Context context, String phoneNumberIncoming, int pageIncoming);

        void loadCallsIncoming(String phoneNumberIncoming, int pageIncoming);

        void loadCallsOutgoing(String phoneNumberOutgoing, int pageOutgoing);

        void clearData();

        boolean hasNextPage();

    }
}
