package com.ethan.morephone.presentation.contact.editor;

import android.content.Context;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.network.ApiMorePhone;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 9/29/17.
 */

public class ContactEditorPresenter implements ContactEditorContract.Presenter {

    private final ContactEditorContract.View mView;

    public ContactEditorPresenter(ContactEditorContract.View view) {
        mView = view;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void createContact(Context context, Contact contact) {
        mView.showLoading(true);
        ApiMorePhone.createContact(context, contact, new Callback<BaseResponse<Contact>>() {
            @Override
            public void onResponse(Call<BaseResponse<Contact>> call, Response<BaseResponse<Contact>> response) {
                mView.showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    mView.createContactSuccess(response.body().getResponse());
                } else {
                    mView.createContactFail();
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<Contact>> call, Throwable t) {
                mView.createContactFail();
                mView.showLoading(false);
            }
        });
    }

    @Override
    public void updateContact(Context context, Contact contact) {
        mView.showLoading(true);
        ApiMorePhone.updateContact(context, contact, new Callback<BaseResponse<Contact>>() {
            @Override
            public void onResponse(Call<BaseResponse<Contact>> call, Response<BaseResponse<Contact>> response) {
                mView.showLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    mView.updateContactSuccess(response.body().getResponse());
                } else {
                    mView.updateContactFail();
                }

            }

            @Override
            public void onFailure(Call<BaseResponse<Contact>> call, Throwable t) {
                mView.updateContactFail();
                mView.showLoading(false);
            }
        });
    }
}