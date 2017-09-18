package com.ethan.morephone.presentation.phone.log;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.call.Calls;
import com.android.morephone.data.entity.call.ResourceCall;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCallsIncoming;
import com.android.morephone.domain.usecase.call.GetCallsOutgoing;
import com.ethan.morephone.Constant;

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

//    private List<Call> mCalls;

    private ResourceCall mResourceCall;

    public CallLogPresenter(@NonNull CallLogContract.View view,
                            @NonNull UseCaseHandler useCaseHandler,
                            @NonNull GetCallsIncoming getCallsIncoming,
                            @NonNull GetCallsOutgoing getCallsOutgoing) {
        mView = view;

        mUseCaseHandler = useCaseHandler;
        mGetCallsIncoming = getCallsIncoming;
        mGetCallsOutgoing = getCallsOutgoing;

//        mCalls = new ArrayList<>();

        mResourceCall = new ResourceCall(new ArrayList<Call>(), "", Constant.FIRST_PAGE, "", "", "",Constant.FIRST_PAGE, "", "", 0);
        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadCalls(Context context, String phoneNumberIncoming) {

        String pageIncoming = "";
        String pageOutgoing = "";
        if (mResourceCall != null) {

            pageIncoming = mResourceCall.incomingNextPageUri;
            pageOutgoing = mResourceCall.outgoingNextPageUri;

            mResourceCall.incomingNextPageUri = "";
            mResourceCall.outgoingNextPageUri = "";
        }

        if(!TextUtils.isEmpty(pageIncoming) || !TextUtils.isEmpty(pageOutgoing)){
            new DataAsync(context, this, phoneNumberIncoming, pageIncoming, pageOutgoing).execute();
        }


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
//        mCalls.clear();
        mResourceCall = new ResourceCall(new ArrayList<Call>(), "", Constant.FIRST_PAGE, "", "", "",Constant.FIRST_PAGE, "", "", 0);
    }

    @Override
    public boolean hasNextPage() {
        if (mResourceCall != null && (!TextUtils.isEmpty(mResourceCall.incomingNextPageUri) || !TextUtils.isEmpty(mResourceCall.outgoingNextPageUri))) {
            return true;
        }
        return false;
    }

    private void executeData(Calls calls) {
//        mCalls.addAll(calls.calls);
    }


    private static class DataAsync extends AsyncTask<Void, Integer, Void> {
        private final WeakReference<CallLogPresenter> mWeakReference;
        private final String mPhoneNumber;
        private final String mPageIncoming;
        private final String mPageOutgoing;
        private final Context mContext;

        public DataAsync(Context context, CallLogPresenter presenter, String phoneNumber, String pageIncoming, String pageOutgoing) {
            mWeakReference = new WeakReference<>(presenter);
            this.mPhoneNumber = phoneNumber;
            mContext = context;
            mPageIncoming = pageIncoming;
            mPageOutgoing = pageOutgoing;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CallLogPresenter presenter = mWeakReference.get();
            if (presenter != null) {
                presenter.mView.showLoading(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            CallLogPresenter presenter = mWeakReference.get();
            if (presenter != null) {
//                Calls callIncoming = ApiManager.getCallsIncoming(mContext, mPhoneNumber, 0);
//                Calls callOutgoing = ApiManager.getCallsOutgoing(mContext, mPhoneNumber, 0);
//
//                if (callIncoming != null && callIncoming.calls != null && !callIncoming.calls.isEmpty()) {
//                    presenter.executeData(callIncoming);
//                }
//
//                if (callOutgoing != null && callOutgoing.calls != null && !callOutgoing.calls.isEmpty()) {
//                    presenter.executeData(callOutgoing);
//                }

                BaseResponse<ResourceCall> baseResponse = ApiMorePhone.getCallLogs(mContext, mPhoneNumber, mPageIncoming, mPageOutgoing);

                if (baseResponse != null && baseResponse.getResponse() != null) {
                    presenter.mResourceCall = baseResponse.getResponse();
                }

            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            CallLogPresenter presenter = mWeakReference.get();
            if (presenter != null) {
                if (presenter.mResourceCall != null && presenter.mResourceCall.records != null) {
                    presenter.mView.showCallLog(presenter.mResourceCall.records);
                }
                presenter.mView.showLoading(false);
            }
        }
    }
}
