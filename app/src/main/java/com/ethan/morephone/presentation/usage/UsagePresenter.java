package com.ethan.morephone.presentation.usage;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.usage.UsageItem;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.network.HTTPStatus;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.usage.GetUsageAllTime;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 4/26/17.
 */

public class UsagePresenter implements UsageContract.Presenter {

    private final UsageContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetUsageAllTime mGetUsageAllTime;

    public UsagePresenter(@NonNull UsageContract.View view,
                          @NonNull UseCaseHandler useCaseHandler,
                          @NonNull GetUsageAllTime getUsageAllTime) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetUsageAllTime = getUsageAllTime;

        mView.setPresenter(this);
    }

    @Override
    public void loadUsage(Context context, int page, String pageToken) {
        mView.showLoading(true);

        ApiMorePhone.usage(context, MyPreference.getUserId(context), new Callback<BaseResponse<UsageItem>>() {
            @Override
            public void onResponse(Call<BaseResponse<UsageItem>> call, Response<BaseResponse<UsageItem>> response) {
                if(response.isSuccessful()){
                    if(response.body().getStatus() == HTTPStatus.OK.getStatusCode()){
                        mView.showUsage(response.body().getResponse());
                    }
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<UsageItem>> call, Throwable t) {
                mView.showLoading(false);
            }
        });

//        GetUsageAllTime.RequestValue requestCall = new GetUsageAllTime.RequestValue(UsageFragment.CATEGORY_CALL, page, pageToken);
//        mUseCaseHandler.execute(mGetUsageAllTime, requestCall, new UseCase.UseCaseCallback<GetUsageAllTime.ResponseValue>() {
//            @Override
//            public void onSuccess(GetUsageAllTime.ResponseValue response) {
//                mView.showUsage(response.getUsage());
//                mView.showLoading(false);
//            }
//
//            @Override
//            public void onError() {
//                mView.showLoading(false);
//            }
//        });
//
//
//        GetUsageAllTime.RequestValue requestCallInbound = new GetUsageAllTime.RequestValue(UsageFragment.CATEGORY_CALL_INBOUND, page, pageToken);
//        mUseCaseHandler.execute(mGetUsageAllTime, requestCallInbound, new UseCase.UseCaseCallback<GetUsageAllTime.ResponseValue>() {
//            @Override
//            public void onSuccess(GetUsageAllTime.ResponseValue response) {
//                mView.showUsage(response.getUsage());
//            }
//
//            @Override
//            public void onError() {
//                mView.showLoading(false);
//            }
//        });
//
////        GetUsageAllTime.RequestValue requestCallOutbound = new GetUsageAllTime.RequestValue(UsageFragment.CATEGORY_CALL_OUTBOUND, page, pageToken);
////        mUseCaseHandler.execute(mGetUsageAllTime, requestCallOutbound, new UseCase.UseCaseCallback<GetUsageAllTime.ResponseValue>() {
////            @Override
////            public void onSuccess(GetUsageAllTime.ResponseValue response) {
////                mView.showUsage(response.getUsage());
////            }
////
////            @Override
////            public void onError() {
////                mView.showLoading(false);
////            }
////        });
//
//        GetUsageAllTime.RequestValue requestSms = new GetUsageAllTime.RequestValue(UsageFragment.CATEGORY_SMS, page, pageToken);
//        mUseCaseHandler.execute(mGetUsageAllTime, requestSms, new UseCase.UseCaseCallback<GetUsageAllTime.ResponseValue>() {
//            @Override
//            public void onSuccess(GetUsageAllTime.ResponseValue response) {
//                mView.showUsage(response.getUsage());
//            }
//
//            @Override
//            public void onError() {
//                mView.showLoading(false);
//            }
//        });
//
//        GetUsageAllTime.RequestValue requestSmsInbound = new GetUsageAllTime.RequestValue(UsageFragment.CATEGORY_SMS_INBOUND, page, pageToken);
//        mUseCaseHandler.execute(mGetUsageAllTime, requestSmsInbound, new UseCase.UseCaseCallback<GetUsageAllTime.ResponseValue>() {
//            @Override
//            public void onSuccess(GetUsageAllTime.ResponseValue response) {
//                mView.showUsage(response.getUsage());
//            }
//
//            @Override
//            public void onError() {
//                mView.showLoading(false);
//            }
//        });

//        GetUsageAllTime.RequestValue requestSmsOutbound = new GetUsageAllTime.RequestValue(UsageFragment.CATEGORY_SMS_OUTBOUND, page, pageToken);
//        mUseCaseHandler.execute(mGetUsageAllTime, requestSmsOutbound, new UseCase.UseCaseCallback<GetUsageAllTime.ResponseValue>() {
//            @Override
//            public void onSuccess(GetUsageAllTime.ResponseValue response) {
//                mView.showUsage(response.getUsage());
//            }
//
//            @Override
//            public void onError() {
//                mView.showLoading(false);
//            }
//        });

    }

    @Override
    public void start() {

    }
}
