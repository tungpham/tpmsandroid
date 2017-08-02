package com.ethan.morephone.presentation.buy.payment.purchase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.TwilioManager;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.BuyIncomingPhoneNumber;
import com.ethan.morephone.MyPreference;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 7/5/17.
 */

public class PurchasePresenter implements PurchaseContract.Presenter {

    private final PurchaseContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final BuyIncomingPhoneNumber mBuyIncomingPhoneNumber;

    public PurchasePresenter(@NonNull PurchaseContract.View view,
                             @NonNull UseCaseHandler useCaseHandler,
                             @NonNull BuyIncomingPhoneNumber buyIncomingPhoneNumber) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mBuyIncomingPhoneNumber = buyIncomingPhoneNumber;

        mView.setPresenter(this);
    }

    @Override
    public void buyIncomingPhoneNumber(final Context context, final String buyPhoneNumber) {
//        mView.showLoading(true);
//        BuyIncomingPhoneNumber.RequestValue requestValue = new BuyIncomingPhoneNumber.RequestValue(phoneNumber);
//        mUseCaseHandler.execute(mBuyIncomingPhoneNumber, requestValue, new UseCase.UseCaseCallback<BuyIncomingPhoneNumber.ResponseValue>() {
//            @Override
//            public void onSuccess(BuyIncomingPhoneNumber.ResponseValue response) {
//                IncomingPhoneNumber incomingPhoneNumber = response.getIncomingPhoneNumber();
//                if (incomingPhoneNumber != null) {
//                    createPhoneNumber(context, incomingPhoneNumber);
//                } else {
//                    mView.buyIncomingPhoneNumberFail();
//                    mView.showLoading(false);
//                }
//            }
//
//            @Override
//            public void onError() {
//                mView.buyIncomingPhoneNumberFail();
//                mView.showLoading(false);
//            }
//        });

        mView.showLoading(true);
        PhoneNumber phoneNumber = PhoneNumber.getBuilder().userId(MyPreference.getUserId(context))
                .accountSid(TwilioManager.getSid(context))
                .authToken(TwilioManager.getAuthCode(context))
                .applicationSid(TwilioManager.getApplicationSid(context))
                .phoneNumber(buyPhoneNumber).build();

        ApiMorePhone.createPhoneNumber(context, phoneNumber, new Callback<BaseResponse<PhoneNumber>>() {
            @Override
            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                if (response.isSuccessful()) {
                    mView.buyIncomingPhoneNumberSuccess(buyPhoneNumber);
                    mView.showLoading(false);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                mView.showLoading(false);
                mView.buyIncomingPhoneNumberFail();
            }
        });

    }

    //    @Override
    public void createPhoneNumber(Context context, final IncomingPhoneNumber incomingPhoneNumber) {
//        PhoneNumber phoneNumber = PhoneNumber.getBuilder().userId(MyPreference.getUserId(context))
//                .friendlyName(incomingPhoneNumber.friendlyName)
//                .sid(incomingPhoneNumber.sid)
//                .accountSid(TwilioManager.getSid(context))
//                .authToken(TwilioManager.getAuthCode(context))
//                .applicationSid(TwilioManager.getApplicationSid(context))
//                .phoneNumber(incomingPhoneNumber.phoneNumber).build();
//        ApiMorePhone.createPhoneNumber(context, phoneNumber, new Callback<BaseResponse<PhoneNumber>>() {
//            @Override
//            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
//                if (response.isSuccessful()) {
//                    mView.buyIncomingPhoneNumberSuccess(incomingPhoneNumber);
//                    mView.showLoading(false);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
//                mView.showLoading(false);
//            }
//        });
    }

    @Override
    public void start() {

    }

}
