package com.ethan.morephone.presentation.voice;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.morephone.data.entity.record.Records;
import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.record.RecordListResourceResponse;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.record.DeleteCallRecord;
import com.android.morephone.domain.usecase.record.GetCallRecords;
import com.android.morephone.domain.usecase.voice.DeleteVoice;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 2/17/17.
 */

public class VoicePresenter implements VoiceContract.Presenter {

    private final VoiceContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final DeleteVoice mDeleteVoice;

    private final GetCallRecords mGetCallRecords;
    private final DeleteCallRecord mDeleteCallRecord;

//    private List<VoiceItem> mVoiceItems;

    public VoicePresenter(@NonNull VoiceContract.View view,
                          @NonNull UseCaseHandler useCaseHandler,
                          @NonNull DeleteVoice deleteVoice,
                          @NonNull GetCallRecords getCallRecords,
                          @NonNull DeleteCallRecord deleteCallRecord) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mDeleteVoice = deleteVoice;
        mGetCallRecords = getCallRecords;
        mDeleteCallRecord = deleteCallRecord;

//        mVoiceItems = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void deleteRecord(String callSid, String recordSid) {
        DeleteCallRecord.RequestValue requestValue = new DeleteCallRecord.RequestValue(Constant.ACCOUNT_SID, callSid, recordSid);
        mUseCaseHandler.execute(mDeleteCallRecord, requestValue, new UseCase.UseCaseCallback<DeleteCallRecord.ResponseValue>() {
            @Override
            public void onSuccess(DeleteCallRecord.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void loadRecords(String callSid) {
        GetCallRecords.RequestValue requestValue = new GetCallRecords.RequestValue(Constant.ACCOUNT_SID, callSid);
        mUseCaseHandler.execute(mGetCallRecords, requestValue, new UseCase.UseCaseCallback<GetCallRecords.ResponseValue>() {
            @Override
            public void onSuccess(GetCallRecords.ResponseValue response) {
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
