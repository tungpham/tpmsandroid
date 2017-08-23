package com.ethan.morephone.presentation.buy.payment.purchase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.utils.TwilioManager;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.BuyIncomingPhoneNumber;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.presentation.phone.service.PhoneService;

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

        mView.showLoading(true);
        DebugTool.logD("APP SID FOR BUY PHONE: " + TwilioManager.getApplicationSid(context));
        PhoneNumber phoneNumber = PhoneNumber.getBuilder().userId(MyPreference.getUserId(context))
                .accountSid(TwilioManager.getSid(context))
                .authToken(TwilioManager.getAuthCode(context))
                .applicationSid(TwilioManager.getApplicationSid(context))
                .phoneNumber(buyPhoneNumber).build();

        ApiMorePhone.createPhoneNumber(context, phoneNumber, new Callback<BaseResponse<PhoneNumber>>() {
            @Override
            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                if (response.isSuccessful()) {
                    PhoneService.startServiceWithAction(context, PhoneService.ACTION_REGISTER_PHONE_NUMBER, buyPhoneNumber, "");
                    mView.buyIncomingPhoneNumberSuccess(buyPhoneNumber);
                    mView.showLoading(false);
                }else {
                    mView.showLoading(false);
                    mView.buyIncomingPhoneNumberFail();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                mView.showLoading(false);
                mView.buyIncomingPhoneNumberFail();
            }
        });

    }

    @Override
    public void buyPoolPhoneNumber(final Context context, final String buyPhoneNumber, long expire) {
        PhoneNumber phoneNumber = PhoneNumber.getBuilder().userId(MyPreference.getUserId(context))
                .accountSid(TwilioManager.getSid(context))
                .authToken(TwilioManager.getAuthCode(context))
                .applicationSid(TwilioManager.getApplicationSid(context))
                .expire(expire)
                .phoneNumber(buyPhoneNumber).build();

        ApiMorePhone.buyPoolPhoneNumber(context, phoneNumber, new Callback<BaseResponse<PhoneNumber>>() {
            @Override
            public void onResponse(Call<BaseResponse<PhoneNumber>> call, Response<BaseResponse<PhoneNumber>> response) {
                if (response.isSuccessful()) {
                    PhoneService.startServiceWithAction(context, PhoneService.ACTION_REGISTER_PHONE_NUMBER, buyPhoneNumber, "");
                    mView.buyIncomingPhoneNumberSuccess(buyPhoneNumber);
                    mView.showLoading(false);
                }else {
                    mView.showLoading(false);
                    mView.buyIncomingPhoneNumberFail();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<PhoneNumber>> call, Throwable t) {
                mView.showLoading(false);
                mView.buyIncomingPhoneNumberFail();
            }
        });
    }

    @Override
    public void start() {

    }

}
