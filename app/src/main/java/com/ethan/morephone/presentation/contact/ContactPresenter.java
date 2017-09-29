package com.ethan.morephone.presentation.contact;

import android.content.Context;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.network.ApiMorePhone;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 9/29/17.
 */

public class ContactPresenter implements ContactContract.Presenter {

    private final ContactContract.View mView;

    public ContactPresenter(ContactContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void loadContact(Context context, String phoneNumberId) {
        mView.showLoading(true);
        ApiMorePhone.loadContacts(context, phoneNumberId, new Callback<BaseResponse<List<Contact>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Contact>>> call, Response<BaseResponse<List<Contact>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mView.showAllContact(response.body().getResponse());
                }
                mView.showLoading(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Contact>>> call, Throwable t) {
                mView.showLoading(false);
            }
        });
    }
}
