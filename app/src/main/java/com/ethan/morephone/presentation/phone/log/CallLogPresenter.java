package com.ethan.morephone.presentation.phone.log;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCallsIncoming;
import com.android.morephone.domain.usecase.call.GetCallsOutgoing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 4/25/17.
 */

public class CallLogPresenter implements CallLogContract.Presenter {

    private CallLogContract.View mView;

    private final UseCaseHandler mUseCaseHandler;
    private final GetCallsIncoming mGetCallsIncoming;
    private final GetCallsOutgoing mGetCallsOutgoing;

    private List<Call> mCalls;

    public CallLogPresenter(@NonNull CallLogContract.View view,
                            @NonNull UseCaseHandler useCaseHandler,
                            @NonNull GetCallsIncoming getCallsIncoming,
                            @NonNull GetCallsOutgoing getCallsOutgoing) {
        mView = view;

        mUseCaseHandler = useCaseHandler;
        mGetCallsIncoming = getCallsIncoming;
        mGetCallsOutgoing = getCallsOutgoing;

        mCalls = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCallsIncoming(String phoneNumberIncoming, int pageIncoming) {
        mView.showLoading(true);
        GetCallsIncoming.RequestValue requestValue = new GetCallsIncoming.RequestValue(phoneNumberIncoming, pageIncoming);
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
    public void loadCallsOutgoing(String phoneNumberOutgoing, int pageOutcoming) {
        mView.showLoading(true);
        GetCallsOutgoing.RequestValue requestValue = new GetCallsOutgoing.RequestValue(phoneNumberOutgoing, pageOutcoming);
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

    @Override
    public void clearData() {
        mCalls.clear();
    }

    private void executeData(Calls calls, boolean isComing) {
        mCalls.addAll(calls.calls);
        mView.showCallLog(mCalls);
    }
}
