package com.ethan.morephone.presentation.record;

import android.content.Context;

import com.android.morephone.data.entity.record.Record;
import com.ethan.morephone.presentation.BasePresenter;
import com.ethan.morephone.presentation.BaseView;

import java.util.List;

/**
 * Created by Ethan on 2/17/17.
 */

public interface RecordContract {

    interface View extends BaseView<Presenter> {

        void showLoading(boolean isActive);

        void initializeRecord(Record record, String url);

        void showRecords(List<Record> records);

    }

    interface Presenter extends BasePresenter {

        void deleteRecord(Context context, String callSid, String recordSid);

        void loadRecords(Context context, String phoneNumber);

        void loadRecords(Context context);

        void clearData();

    }
}
