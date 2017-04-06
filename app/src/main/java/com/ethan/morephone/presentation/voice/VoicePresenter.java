package com.ethan.morephone.presentation.voice;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.voice.CreateVoice;
import com.android.morephone.domain.usecase.voice.DeleteVoice;
import com.android.morephone.domain.usecase.voice.GetAllVoices;
import com.android.morephone.domain.usecase.voice.GetVoices;
import com.android.morephone.domain.usecase.voice.GetVoicesIncoming;
import com.android.morephone.domain.usecase.voice.GetVoicesOutgoing;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public class VoicePresenter implements VoiceContract.Presenter {

    private final VoiceContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetAllVoices mGetAllVoices;
    private final GetVoices mGetVoices;
    private final GetVoicesIncoming mGetVoicesIncoming;
    private final GetVoicesOutgoing mGetVoicesOutgoing;
    private final DeleteVoice mDeleteVoice;
    private final CreateVoice mCreateVoice;

    private List<VoiceItem> mVoiceItems;

    public VoicePresenter(@NonNull VoiceContract.View view,
                          @NonNull UseCaseHandler useCaseHandler,
                          @NonNull GetAllVoices getAllVoices,
                          @NonNull GetVoices getVoices,
                          @NonNull GetVoicesIncoming getVoicesIncoming,
                          @NonNull GetVoicesOutgoing getVoicesOutgoing,
                          @NonNull DeleteVoice deleteVoice,
                          @NonNull CreateVoice createVoice) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetAllVoices = getAllVoices;
        mGetVoices = getVoices;
        mGetVoicesIncoming = getVoicesIncoming;
        mGetVoicesOutgoing = getVoicesOutgoing;
        mDeleteVoice = deleteVoice;
        mCreateVoice = createVoice;

        mVoiceItems = new ArrayList<>();

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadVoicesResource() {
        mView.showLoading(true);
        GetAllVoices.RequestValue requestValue = new GetAllVoices.RequestValue();
        mUseCaseHandler.execute(mGetAllVoices, requestValue, new UseCase.UseCaseCallback<GetAllVoices.ResponseValue>() {
            @Override
            public void onSuccess(GetAllVoices.ResponseValue response) {
                List<VoiceItem> voiceItems = response.getVoiceItems();
                mView.showVoices(voiceItems);
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void loadVoicesOutgoing(String phoneNumberOutgoing) {
        mView.showLoading(true);
        GetVoicesOutgoing.RequestValue requestValue = new GetVoicesOutgoing.RequestValue(phoneNumberOutgoing);
        mUseCaseHandler.execute(mGetVoicesOutgoing, requestValue, new UseCase.UseCaseCallback<GetVoicesOutgoing.ResponseValue>() {
            @Override
            public void onSuccess(GetVoicesOutgoing.ResponseValue response) {
                List<VoiceItem> voiceItems = response.getVoiceItems();
                mVoiceItems.addAll(voiceItems);
                mView.showVoices(mVoiceItems);
                mView.showLoading(false);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void loadVoicesIncoming(String phoneNumberIncoming) {
        mView.showLoading(true);
        GetVoicesIncoming.RequestValue requestValue = new GetVoicesIncoming.RequestValue(phoneNumberIncoming);
        mUseCaseHandler.execute(mGetVoicesIncoming, requestValue, new UseCase.UseCaseCallback<GetVoicesIncoming.ResponseValue>() {
            @Override
            public void onSuccess(GetVoicesIncoming.ResponseValue response) {
                List<VoiceItem> voiceItems = response.getVoiceItems();
                mVoiceItems.addAll(voiceItems);
                mView.showVoices(mVoiceItems);
                mView.showLoading(false);
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void deleteVoice(String callSid) {
        DeleteVoice.RequestValue requestValue = new DeleteVoice.RequestValue(callSid);
        mUseCaseHandler.execute(mDeleteVoice, requestValue, new UseCase.UseCaseCallback<DeleteVoice.ResponseValue>() {
            @Override
            public void onSuccess(DeleteVoice.ResponseValue response) {

            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void clearData() {
        mVoiceItems.clear();
    }

    @Override
    public void createVoice(String phoneNumberIncoming,
                            String phoneNumberOutgoing,
                            String applicationSid,
                            String sipAuthUsername,
                            String sipAuthPassword) {
        CreateVoice.RequestValue requestValue = new CreateVoice.RequestValue(phoneNumberIncoming, phoneNumberOutgoing, applicationSid, sipAuthUsername, sipAuthPassword);
        mUseCaseHandler.execute(mCreateVoice, requestValue, new UseCase.UseCaseCallback<CreateVoice.ResponseValue>() {
            @Override
            public void onSuccess(CreateVoice.ResponseValue response) {
                VoiceItem voiceItem = response.getVoiceItem();
                DebugTool.logD(voiceItem.duration);
            }

            @Override
            public void onError() {

            }
        });
    }

}
