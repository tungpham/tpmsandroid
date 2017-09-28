package com.android.morephone.data.repository.contact.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.usage.Usage;
import com.android.morephone.data.repository.usage.source.UsageDataSource;

/**
 * Created by truongnguyen on 9/28/17.
 */

public interface ContactDataSource {

    interface GetContactsCallback {
        void onContactLoaded(Usage usage);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }


}
