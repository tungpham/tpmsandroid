package com.ethan.morephone.presentation.voice;

import com.android.morephone.data.entity.twilio.record.RecordItem;
import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public interface VoiceContract {

    interface View extends BaseView<Presenter> {

        void showVoices(List<VoiceItem> voiceItems);

        void showLoading(boolean isActive);

        void initializeRecord(RecordItem recordItem, String url);

        void emptyRecord(int position);
    }

    interface Presenter extends BasePresenter {

        void loadVoicesIncoming(String phoneNumberIncoming);

        void deleteVoice(String callSid);

        void deleteRecord(String callSid, String recordSid);

        void loadRecords(String callSid, int position);

        void clearData();

    }
}
