package com.ethan.morephone.presentation.contact;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.contact.ContactFilterType;
import com.android.morephone.domain.usecase.contact.GetContacts;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 9/29/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final GetContacts mGetContacts;

    public ContactPresenter(@NonNull ContactContract.View view,
                            @NonNull UseCaseHandler useCaseHandler,
                            @NonNull GetContacts getContacts) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mGetContacts = getContacts;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadContact(Context context, String phoneNumberId) {
        mView.showLoading(true);
        GetContacts.RequestValues requestValues = new GetContacts.RequestValues(true, phoneNumberId, "", ContactFilterType.ALL_CONTACTS);
        mUseCaseHandler.execute(mGetContacts, requestValues, new UseCase.UseCaseCallback<GetContacts.ResponseValue>() {
            @Override
            public void onSuccess(GetContacts.ResponseValue response) {
                mView.showAllContact(response.getContacts());
                mView.showLoading(false);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
            }
        });
    }
}
