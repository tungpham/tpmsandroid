package com.android.morephone.data.repository.usage.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.usage.Usage;

/**
 * Created by Ethan on 7/14/17.
 */

public interface UsageDataSource {

    interface GetUsageCallback {
        void onUsageLoaded(Usage usage);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getUsageAllTime(String category, int page, String pageToken, @NonNull GetUsageCallback callback);

}
