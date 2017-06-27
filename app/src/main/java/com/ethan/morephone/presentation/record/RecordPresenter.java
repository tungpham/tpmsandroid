package com.ethan.morephone.presentation.record;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.morephone.data.entity.record.Records;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.record.DeleteRecord;
import com.android.morephone.domain.usecase.record.GetRecords;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 2/17/17.
 */

public class RecordPresenter implements RecordContract.Presenter {

    private final RecordContract.View mView;
    private final UseCaseHandler mUseCaseHandler;

    private final GetRecords mGetRecords;
    private final DeleteRecord mDeleteRecord;

//    private List<VoiceItem> mVoiceItems;

    public RecordPresenter(@NonNull RecordContract.View view,
                           @NonNull UseCaseHandler useCaseHandler,
                           @NonNull GetRecords getRecords,
                           @NonNull DeleteRecord deleteRecord) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetRecords = getRecords;
        mDeleteRecord = deleteRecord;

//        mVoiceItems = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void deleteRecord(String callSid, String recordSid) {
        DeleteRecord.RequestValue requestValue = new DeleteRecord.RequestValue(Constant.ACCOUNT_SID, callSid, recordSid);
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
    public void loadRecords(String callSid) {
        GetRecords.RequestValue requestValue = new GetRecords.RequestValue(Constant.ACCOUNT_SID, callSid);
        mUseCaseHandler.execute(mGetRecords, requestValue, new UseCase.UseCaseCallback<GetRecords.ResponseValue>() {
            @Override
            public void onSuccess(GetRecords.ResponseValue response) {
                RecordListResourceResponse recordListResourceResponse = response.getRecordListResourceResponse();
                if (recordListResourceResponse.recordings != null && !recordListResourceResponse.recordings.isEmpty()) {
                    RecordItem recordItem = recordListResourceResponse.recordings.get(0);
                    if(!TextUtils.isEmpty(recordItem.uri)) {
                        String url = recordItem.uri.replace("json", "mp3");
//                        mView.initializeRecord(recordItem, Constant.API_ROOT + url);
                    }
                }else{
                }
            }

            @Override
            public void onError() {
            }
        });
    }

    @Override
    public void loadRecords(Context context) {
        mView.showLoading(true);
        ApiMorePhone.getRecords(context, Constant.ACCOUNT_SID, MyPreference.getPhoneNumberSid(context), new Callback<Records>() {
            @Override
            public void onResponse(Call<Records> call, Response<Records> response) {
                mView.showRecords(response.body().recordings);
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<Records> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void clearData() {
//        mVoiceItems.clear();
    }

}
