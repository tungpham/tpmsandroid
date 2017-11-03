package com.ethan.morephone.utils;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.android.morephone.data.repository.contact.source.GetContactCallback;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.contact.GetContactByPhoneNumber;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class ContactUtil {

    public static void getContactDisplay(@Nullable final View view, @NonNull UseCaseHandler useCaseHandler, @NonNull GetContactByPhoneNumber getContactByPhoneNumber, @NonNull String phoneNumber,  @NonNull final GetContactCallback getContactCallback){
        GetContactByPhoneNumber.RequestValues requestValues = new GetContactByPhoneNumber.RequestValues(phoneNumber);
        useCaseHandler.execute(getContactByPhoneNumber, requestValues, new UseCase.UseCaseCallback<GetContactByPhoneNumber.ResponseValue>() {
            @Override
            public void onSuccess(GetContactByPhoneNumber.ResponseValue response) {
                getContactCallback.onContactLoaded(view, response.getContact());
            }

            @Override
            public void onError() {
                getContactCallback.onContactNotAvailable();
            }
        });
    }
}
