package com.ethan.morephone.presentation.phone.log;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCallsIncoming;
import com.android.morephone.domain.usecase.call.GetCallsOutgoing;

import java.lang.ref.WeakReference;
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
    public void loadCalls(Context context, String phoneNumberIncoming, int pageIncoming) {
        new DataAsync(context, this, phoneNumberIncoming).execute();
    }

    @Override
    public void loadCallsIncoming(String phoneNumberIncoming, int pageIncoming) {
        mView.showLoading(true);
        GetCallsIncoming.RequestValue requestValue = new GetCallsIncoming.RequestValue(phoneNumberIncoming, pageIncoming);
        mUseCaseHandler.execute(mGetCallsIncoming, requestValue, new UseCase.UseCaseCallback<GetCallsIncoming.ResponseValue>() {
            @Override
            public void onSuccess(GetCallsIncoming.ResponseValue response) {
                executeData(response.getCalls());
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
                executeData(response.getCalls());
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

    private void executeData(Calls calls) {
        mCalls.addAll(calls.calls);
    }


    private static class DataAsync extends AsyncTask<Void, Integer, Void> {
        private final WeakReference<CallLogPresenter> mWeakReference;
        private final String mPhoneNumber;
        private final Context mContext;

        public DataAsync(Context context, CallLogPresenter presenter, String phoneNumber) {
            mWeakReference = new WeakReference<>(presenter);
            this.mPhoneNumber = phoneNumber;
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CallLogPresenter presenter = mWeakReference.get();
            if(presenter != null){
                presenter.mView.showLoading(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            CallLogPresenter presenter = mWeakReference.get();
            if (presenter != null) {
                Calls callIncoming = ApiManager.getCallsIncoming(mContext, mPhoneNumber, 0);
                Calls callOutgoing = ApiManager.getCallsOutgoing(mContext, mPhoneNumber, 0);

                if (callIncoming != null && callIncoming.calls != null && !callIncoming.calls.isEmpty()) {
                    presenter.executeData(callIncoming);
                }

                if (callOutgoing != null && callOutgoing.calls != null && !callOutgoing.calls.isEmpty()) {
                    presenter.executeData(callOutgoing);
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CallLogPresenter presenter = mWeakReference.get();
            if(presenter != null){
                presenter.mView.showCallLog(presenter.mCalls);
                presenter.mView.showLoading(false);
            }
            DebugTool.logD("POST EXECUTE");
        }
    }
}
