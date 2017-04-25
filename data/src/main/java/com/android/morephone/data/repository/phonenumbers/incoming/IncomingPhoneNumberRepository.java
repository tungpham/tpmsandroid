package com.android.morephone.data.repository.phonenumbers.incoming;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.phonenumbers.incoming.source.IncomingPhoneNumberDataSource;

/**
 * Created by Ethan on 4/25/17.
 */

public class IncomingPhoneNumberRepository implements IncomingPhoneNumberDataSource {

    private static IncomingPhoneNumberRepository INSTANCE = null;

    private final IncomingPhoneNumberDataSource mIncomingPhoneNumberDataSource;

    private IncomingPhoneNumberRepository(@NonNull IncomingPhoneNumberDataSource incomingPhoneNumberRemoteDataSource) {
        mIncomingPhoneNumberDataSource = incomingPhoneNumberRemoteDataSource;
    }

    public static IncomingPhoneNumberRepository getInstance(IncomingPhoneNumberDataSource incomingPhoneNumberRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new IncomingPhoneNumberRepository(incomingPhoneNumberRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getIncomingPhoneNumbers(@NonNull LoadIncomingPhoneNumbersCallback callback) {
        mIncomingPhoneNumberDataSource.getIncomingPhoneNumbers(callback);
    }

    @Override
    public void changeFriendlyName(String accountSid, String incomingPhoneNumberSid, String friendlyName, @NonNull GetIncomingPhoneNumberCallback callback) {
        mIncomingPhoneNumberDataSource.changeFriendlyName(accountSid, incomingPhoneNumberSid, friendlyName, callback);
    }
}
