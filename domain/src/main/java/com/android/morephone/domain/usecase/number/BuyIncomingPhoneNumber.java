package com.android.morephone.domain.usecase.number;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.repository.phonenumbers.incoming.IncomingPhoneNumberRepository;
import com.android.morephone.data.repository.phonenumbers.incoming.source.IncomingPhoneNumberDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class BuyIncomingPhoneNumber extends UseCase<BuyIncomingPhoneNumber.RequestValue, BuyIncomingPhoneNumber.ResponseValue> {

    private final IncomingPhoneNumberRepository mIncomingPhoneNumberRepository;

    public BuyIncomingPhoneNumber(@NonNull IncomingPhoneNumberRepository incomingPhoneNumberRepository) {
        mIncomingPhoneNumberRepository = incomingPhoneNumberRepository;
    }

    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mIncomingPhoneNumberRepository.buyIncomingPhoneNumber(requestValue.getPhoneNumber(), new IncomingPhoneNumberDataSource.GetIncomingPhoneNumberCallback() {
            @Override
            public void onIncomingPhoneNumberLoaded(IncomingPhoneNumber incomingPhoneNumber) {
                getUseCaseCallback().onSuccess(new ResponseValue(incomingPhoneNumber));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mPhoneNumber;

        public RequestValue(String phoneNumber) {
            mPhoneNumber = phoneNumber;
        }

        public String getPhoneNumber() {
            return mPhoneNumber;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final IncomingPhoneNumber mIncomingPhoneNumber;

        public ResponseValue(IncomingPhoneNumber incomingPhoneNumber) {
            mIncomingPhoneNumber = incomingPhoneNumber;
        }

        public IncomingPhoneNumber getIncomingPhoneNumber() {
            return mIncomingPhoneNumber;
        }
    }
}
