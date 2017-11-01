package com.ethan.morephone.presentation.record;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.entity.record.ResourceRecord;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.TwilioManager;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCall;
import com.android.morephone.domain.usecase.record.DeleteRecord;
import com.android.morephone.domain.usecase.record.GetRecords;
import com.ethan.morephone.Constant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 2/17/17.
 */

public class RecordPresenter implements RecordContract.Presenter {

    private final RecordContract.View mView;
    private final UseCaseHandler mUseCaseHandler;

    private final GetRecords mGetRecords;
    private final GetCall mGetCall;
    private final DeleteRecord mDeleteRecord;

//    private List<Record> mRecords;

    private ResourceRecord mResourceRecord;

    public RecordPresenter(@NonNull RecordContract.View view,
                           @NonNull UseCaseHandler useCaseHandler,
                           @NonNull GetRecords getRecords,
                           @NonNull GetCall getCall,
                           @NonNull DeleteRecord deleteRecord) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetRecords = getRecords;
        mDeleteRecord = deleteRecord;
        mGetCall = getCall;

        mResourceRecord = new ResourceRecord(new ArrayList<Record>(), "", Constant.FIRST_PAGE, "", "", 0);
//        mRecords = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void deleteRecord(Context context, String callSid, String recordSid) {
        DeleteRecord.RequestValue requestValue = new DeleteRecord.RequestValue(TwilioManager.getSid(context), callSid, recordSid);
        mUseCaseHandler.execute(mDeleteRecord, requestValue, new UseCase.UseCaseCallback<DeleteRecord.ResponseValue>() {
            @Override
            public void onSuccess(DeleteRecord.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void loadRecords(final Context context, final String phoneNumber) {

        String page = "";
        if (mResourceRecord != null) {

            page = mResourceRecord.nextPageUri;

            mResourceRecord.nextPageUri = "";
        }

        if(!TextUtils.isEmpty(page)){
            new RecordPresenter.DataAsync(context, this, phoneNumber, page).execute();
        }



//        mView.showLoading(true);
//        GetRecords.RequestValue requestValue = new GetRecords.RequestValue();
//        mUseCaseHandler.execute(mGetRecords, requestValue, new UseCase.UseCaseCallback<GetRecords.ResponseValue>() {
//            @Override
//            public void onSuccess(GetRecords.ResponseValue response) {
//                RecordListResourceResponse recordListResourceResponse = response.getRecordListResourceResponse();
//                if (recordListResourceResponse.recordings != null && !recordListResourceResponse.recordings.isEmpty()) {
//                    executeData(context, phoneNumber, recordListResourceResponse.recordings);
//                } else {
//
//                }
//
//                mView.showLoading(false);
//            }
//
//            @Override
//            public void onError() {
//                mView.showLoading(false);
//            }
//        });
    }

    @Override
    public void loadRecords(Context context) {
    }

    @Override
    public void clearData() {
//        mRecords.clear();
        mResourceRecord = new ResourceRecord(new ArrayList<Record>(), "", Constant.FIRST_PAGE, "", "", 0);
    }

    @Override
    public boolean hasNextPage() {
        return mResourceRecord != null && !TextUtils.isEmpty(mResourceRecord.nextPageUri);
    }

    private void executeData(Context context, final String phoneNumber, List<RecordItem> records) {
        DebugTool.logD("RECORDS SIZE: " + records.size() + " PHONE NUMBER: " + phoneNumber);
//        mRecords.clear();
        for (final RecordItem recordItem : records) {
            DebugTool.logD("RecordItem: " + recordItem.uri);
            DebugTool.logD("RecordItem CALL SID: " + recordItem.callSid);
//            ApiManager.
            ApiManager.getCall(context, recordItem.callSid, new Callback<Call>() {
                @Override
                public void onResponse(retrofit2.Call<Call> callResponse, Response<Call> response) {
                    if (response.isSuccessful()) {
                        com.android.morephone.data.entity.call.Call call = response.body();
                        if (phoneNumber.equals(call.to)) {
//                            mRecords.add(RecordDataMapper.transform(false, call.from, recordItem));
//                            mView.showRecords(mRecords);
                        } else {

                        }
                    }
                }

                @Override
                public void onFailure(retrofit2.Call<Call> call, Throwable t) {
                }
            });

//            GetCall.RequestValue requestValue = new GetCall.RequestValue(recordItem.callSid, recordItem);
//            mUseCaseHandler.execute(mGetCall, requestValue, new UseCase.UseCaseCallback<GetCall.ResponseValue>() {
//                @Override
//                public void onSuccess(GetCall.ResponseValue response) {
//                    com.android.morephone.data.entity.call.Call call = response.getCall();
//                    if (call != null) {
//                        DebugTool.logD("TO: " + call.to + " |||    " + call.from);
//                        if (phoneNumber.equals(call.to)) {
//                            mRecords.add(RecordDataMapper.transform(false, call.from, response.getRecordItem()));
//                            mView.showRecords(mRecords);
//                        } else {
//
//                        }
////                        else if(phoneNumber.equals(call.from)){
////                            mRecords.add(RecordDataMapper.transform(true, call.to, response.getRecordItem()));
////                            mView.showRecords(mRecords);
////                        }
//                    }
//                }
//
//                @Override
//                public void onError() {
//
//                }
//            });
        }

    }


    private static class DataAsync extends AsyncTask<Void, Integer, Void> {
        private final WeakReference<RecordPresenter> mWeakReference;
        private final String mPhoneNumber;
        private final String mPage;
        private final Context mContext;

        public DataAsync(Context context, RecordPresenter presenter, String phoneNumber, String page) {
            mWeakReference = new WeakReference<>(presenter);
            this.mPhoneNumber = phoneNumber;
            mPage = page;
            mContext = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            RecordPresenter presenter = mWeakReference.get();
            if(presenter != null){
                presenter.mView.showLoading(true);
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            RecordPresenter presenter = mWeakReference.get();
            if (presenter != null) {

                BaseResponse<ResourceRecord> baseResponse = ApiMorePhone.getRecords(mContext, mPhoneNumber, mPage);
                if(baseResponse != null && baseResponse.getResponse() != null) {
                    presenter.mResourceRecord = baseResponse.getResponse();
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            RecordPresenter presenter = mWeakReference.get();
            if(presenter != null){
                if(presenter.mResourceRecord != null && presenter.mResourceRecord.records != null) {
                    presenter.mView.showRecords(presenter.mResourceRecord.records);
                }
                presenter.mView.showLoading(false);
            }
        }
    }

}
