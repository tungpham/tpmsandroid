package com.ethan.morephone.presentation.phone.log;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCallsIncoming;
import com.android.morephone.domain.usecase.call.GetCallsOutgoing;

/**
 * Created by Ethan on 4/25/17.
 */

public class CallLogPresenter implements CallLogContract.Presenter {

    private CallLogContract.View mView;

    private final UseCaseHandler mUseCaseHandler;
    private final GetCallsIncoming mGetCallsIncoming;
    private final GetCallsOutgoing mGetCallsOutgoing;

    public CallLogPresenter(@NonNull CallLogContract.View view,
                            @NonNull UseCaseHandler useCaseHandler,
                            @NonNull GetCallsIncoming getCallsIncoming,
                            @NonNull GetCallsOutgoing getCallsOutgoing) {
        mView = view;

        mUseCaseHandler = useCaseHandler;
        mGetCallsIncoming = getCallsIncoming;
        mGetCallsOutgoing = getCallsOutgoing;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCallLogs(Context context) {
//        mView.showLoading(true);
    }

    @Override
    public void loadCallsIncoming(String phoneNumberIncoming) {
        mView.showLoading(true);
        GetCallsIncoming.RequestValue requestValue = new GetCallsIncoming.RequestValue(phoneNumberIncoming);
        mUseCaseHandler.execute(mGetCallsIncoming, requestValue, new UseCase.UseCaseCallback<GetCallsIncoming.ResponseValue>() {
            @Override
            public void onSuccess(GetCallsIncoming.ResponseValue response) {
                executeData(response.getCalls(), true);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadCallsOutgoing(String phoneNumberOutgoing) {
        mView.showLoading(true);
        GetCallsOutgoing.RequestValue requestValue = new GetCallsOutgoing.RequestValue(phoneNumberOutgoing);
        mUseCaseHandler.execute(mGetCallsOutgoing, requestValue, new UseCase.UseCaseCallback<GetCallsOutgoing.ResponseValue>() {
            @Override
            public void onSuccess(GetCallsOutgoing.ResponseValue response) {
                executeData(response.getCalls(), true);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    private void executeData(Calls calls, boolean isComing) {
        mView.showCallLog(calls);
    }
}
