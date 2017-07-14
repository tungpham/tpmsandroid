package com.android.morephone.domain.usecase.usage;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.usage.Usage;
import com.android.morephone.data.repository.usage.UsageRepository;
import com.android.morephone.data.repository.usage.source.UsageDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 7/14/17.
 */

public class GetUsageAllTime extends UseCase<GetUsageAllTime.RequestValue, GetUsageAllTime.ResponseValue> {

    private final UsageRepository mUsageRepository;

    public GetUsageAllTime(@NonNull UsageRepository usageRepository) {
        mUsageRepository = usageRepository;
    }

    @Override
    protected void executeUseCase(RequestValue requestValue) {
       mUsageRepository.getUsageAllTime(requestValue.getCategory(), requestValue.getPage(), requestValue.getPageToken(), new UsageDataSource.GetUsageCallback() {
           @Override
           public void onUsageLoaded(Usage usage) {
               getUseCaseCallback().onSuccess(new ResponseValue(usage));
           }

           @Override
           public void onDataNotAvailable() {
                getUseCaseCallback().onError();
           }
       });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mCategory;
        private final int mPage;
        private final String mPageToken;

        public RequestValue(String category, int page, String pageToken) {
            mCategory = category;
            mPage = page;
            mPageToken = pageToken;
        }

        public String getCategory() {
            return mCategory;
        }

        public int getPage() {
            return mPage;
        }

        public String getPageToken(){
            return mPageToken;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Usage mUsage;

        public ResponseValue(@NonNull Usage usage) {
            mUsage = usage;
        }

        public Usage getUsage() {
            return mUsage;
        }
    }
}
