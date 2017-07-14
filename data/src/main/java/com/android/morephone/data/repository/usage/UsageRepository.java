package com.android.morephone.data.repository.usage;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.usage.source.UsageDataSource;

/**
 * Created by Ethan on 7/14/17.
 */

public class UsageRepository implements UsageDataSource{

    private static UsageRepository INSTANCE = null;

    private final UsageDataSource mUsageRemoteDataSource;

    private UsageRepository(@NonNull UsageDataSource usageDataSource) {
        mUsageRemoteDataSource = usageDataSource;
    }

    public static UsageRepository getInstance(UsageDataSource usageDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new UsageRepository(usageDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getUsageAllTime(String category, int page, String pageToken, @NonNull GetUsageCallback callback) {
        mUsageRemoteDataSource.getUsageAllTime(category, page, pageToken, callback);
    }
}