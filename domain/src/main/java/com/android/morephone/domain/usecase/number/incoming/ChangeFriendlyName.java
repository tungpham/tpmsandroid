package com.android.morephone.domain.usecase.number.incoming;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.repository.phonenumbers.incoming.IncomingPhoneNumberRepository;
import com.android.morephone.data.repository.phonenumbers.incoming.source.IncomingPhoneNumberDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 4/25/17.
 */

public class ChangeFriendlyName extends UseCase<ChangeFriendlyName.RequestValue, ChangeFriendlyName.ResponseValue> {

    private IncomingPhoneNumberRepository mIncomingPhoneNumberRepository;

    public ChangeFriendlyName(@NonNull IncomingPhoneNumberRepository incomingPhoneNumberRepository) {
        mIncomingPhoneNumberRepository = incomingPhoneNumberRepository;
    }


    @Override
    protected void executeUseCase(RequestValue requestValue) {
        mIncomingPhoneNumberRepository.changeFriendlyName(requestValue.getAccountSid(), requestValue.getIncomingPhoneNumbersid(), requestValue.getFriendlyName(), new IncomingPhoneNumberDataSource.GetIncomingPhoneNumberCallback() {
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

        private final String mAccountSid;
        private final String mIncomingPhoneNumberSid;
        private final String mFriendlyName;

        public RequestValue(String accountSid, String incomingPhoneNumberSid, String friendlyName) {
            mAccountSid = accountSid;
            mIncomingPhoneNumberSid = incomingPhoneNumberSid;
            mFriendlyName = friendlyName;
        }

        public String getAccountSid(){
            return mAccountSid;
        }

        public String getIncomingPhoneNumbersid(){
            return mIncomingPhoneNumberSid;
        }

        public String getFriendlyName(){
            return mFriendlyName;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final IncomingPhoneNumber mIncomingPhoneNumber;

        public ResponseValue(@NonNull IncomingPhoneNumber incomingPhoneNumber) {
            mIncomingPhoneNumber = incomingPhoneNumber;
        }

        public IncomingPhoneNumber getIncomingPhoneNumber() {
            return mIncomingPhoneNumber;
        }
    }
}
