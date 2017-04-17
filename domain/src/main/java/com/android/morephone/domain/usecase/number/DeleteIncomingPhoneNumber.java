package com.android.morephone.domain.usecase.number;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.network.ApiManager;
import com.android.morephone.domain.UseCase;

/**
 * Created by Ethan on 3/3/17.
 */

public class DeleteIncomingPhoneNumber extends UseCase<DeleteIncomingPhoneNumber.RequestValue, DeleteIncomingPhoneNumber.ResponseValue>{

    private final Context mContext;

    public DeleteIncomingPhoneNumber(@NonNull Context context) {
        mContext = context;
    }

    @Override
    protected void executeUseCase(RequestValue requestValue) {
        ApiManager.deleteIncomingPhoneNumber(mContext, requestValue.getAccountSid(), requestValue.getIncomingPhoneNumberSid());
    }

    public static final class RequestValue implements UseCase.RequestValue {

        private final String mAccountSid;
        private final String mIncomingPhoneNumberSid;

        public RequestValue(String accountSid, String incomingPhoneNumberSid) {
            mAccountSid = accountSid;
            mIncomingPhoneNumberSid = incomingPhoneNumberSid;
        }

        public String getIncomingPhoneNumberSid() {
            return mIncomingPhoneNumberSid;
        }

        public String getAccountSid() {
            return mAccountSid;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {
        }
    }
}
