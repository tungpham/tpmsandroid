package com.ethan.morephone.presentation.voice;

import com.android.morephone.data.entity.CallEntity;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public interface VoiceContract {

    interface View extends BaseView<Presenter> {

        void showVoices(List<CallEntity> voiceItems);

        void showLoading(boolean isActive);

    }

    interface Presenter extends BasePresenter {

        void loadVoicesResource();

        void loadVoicesOutgoing(String phoneNumberOutgoing);

        void loadVoicesIncoming(String phoneNumberIncoming);

        void deleteVoice(String callSid);

        void createVoice(String phoneNumberIncoming, String phoneNumberOutgoing, String applicationSid, String sipAuthUsername, String sipAuthPassword);

    }
}
