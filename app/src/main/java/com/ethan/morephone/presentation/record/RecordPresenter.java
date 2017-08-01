package com.ethan.morephone.presentation.record;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.entity.record.mapper.RecordDataMapper;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.utils.TwilioManager;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCall;
import com.android.morephone.domain.usecase.record.DeleteRecord;
import com.android.morephone.domain.usecase.record.GetRecords;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public class RecordPresenter implements RecordContract.Presenter {

    private final RecordContract.View mView;
    private final UseCaseHandler mUseCaseHandler;

    private final GetRecords mGetRecords;
    private final GetCall mGetCall;
    private final DeleteRecord mDeleteRecord;

    private List<Record> mRecords;

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

        mRecords = new ArrayList<>();

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
    public void loadRecords(final String phoneNumber) {
        mView.showLoading(true);
        GetRecords.RequestValue requestValue = new GetRecords.RequestValue();
        mUseCaseHandler.execute(mGetRecords, requestValue, new UseCase.UseCaseCallback<GetRecords.ResponseValue>() {
            @Override
            public void onSuccess(GetRecords.ResponseValue response) {
                RecordListResourceResponse recordListResourceResponse = response.getRecordListResourceResponse();
                if (recordListResourceResponse.recordings != null && !recordListResourceResponse.recordings.isEmpty()) {
                    executeData(phoneNumber, recordListResourceResponse.recordings);
                } else {

                }

                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadRecords(Context context) {
    }

    @Override
    public void clearData() {
        mRecords.clear();
    }

    private void executeData(final String phoneNumber, List<RecordItem> records) {
        for (RecordItem recordItem : records) {

            GetCall.RequestValue requestValue = new GetCall.RequestValue(recordItem.callSid, recordItem);
            mUseCaseHandler.execute(mGetCall, requestValue, new UseCase.UseCaseCallback<GetCall.ResponseValue>() {
                @Override
                public void onSuccess(GetCall.ResponseValue response) {
                    com.android.morephone.data.entity.call.Call call = response.getCall();
                    if (call != null) {
                        if (phoneNumber.equals(call.to)) {
                            mRecords.add(RecordDataMapper.transform(false, call.from, response.getRecordItem()));
                            mView.showRecords(mRecords);
                        } else if(phoneNumber.equals(call.from)){
                            mRecords.add(RecordDataMapper.transform(true, call.to, response.getRecordItem()));
                            mView.showRecords(mRecords);
                        }
                    }
                }

                @Override
                public void onError() {

                }
            });
        }

    }

}
