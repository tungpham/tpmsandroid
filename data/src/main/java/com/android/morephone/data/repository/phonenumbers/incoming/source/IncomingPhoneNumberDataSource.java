package com.android.morephone.data.repository.phonenumbers.incoming.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumbers;

/**
 * Created by Ethan on 4/25/17.
 */

public interface IncomingPhoneNumberDataSource {

    interface LoadIncomingPhoneNumbersCallback {
        void onIncomingPhoneNumbersLoaded(IncomingPhoneNumbers incomingPhoneNumbers);

        void onDataNotAvailable();
    }

    interface GetIncomingPhoneNumberCallback {
        void onIncomingPhoneNumberLoaded(IncomingPhoneNumber incomingPhoneNumber);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getIncomingPhoneNumbers(@NonNull LoadIncomingPhoneNumbersCallback callback);

    void changeFriendlyName(String accountSid, String incomingPhoneNumberSid, String friendlyName, @NonNull GetIncomingPhoneNumberCallback callback);

}
