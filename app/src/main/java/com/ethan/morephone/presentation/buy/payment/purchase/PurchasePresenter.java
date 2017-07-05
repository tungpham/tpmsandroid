package com.ethan.morephone.presentation.buy.payment.purchase;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.number.BuyIncomingPhoneNumber;

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
    public void buyIncomingPhoneNumber(Context context, String phoneNumber) {
        BuyIncomingPhoneNumber.RequestValue requestValue = new BuyIncomingPhoneNumber.RequestValue(phoneNumber);
        mUseCaseHandler.execute(mBuyIncomingPhoneNumber, requestValue, new UseCase.UseCaseCallback<BuyIncomingPhoneNumber.ResponseValue>() {
            @Override
            public void onSuccess(BuyIncomingPhoneNumber.ResponseValue response) {
                IncomingPhoneNumber incomingPhoneNumber = response.getIncomingPhoneNumber();
                if (incomingPhoneNumber != null) {
                    mView.buyIncomingPhoneNumberSuccess(incomingPhoneNumber);
                } else {
                    mView.buyIncomingPhoneNumberFail();
                }
            }

            @Override
            public void onError() {
                mView.buyIncomingPhoneNumberFail();
            }
        });
    }

    @Override
    public void start() {

    }
}
