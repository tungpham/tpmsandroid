package com.ethan.morephone.presentation.contact.editor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.contact.CreateContact;
import com.android.morephone.domain.usecase.contact.GetContact;
import com.android.morephone.domain.usecase.contact.UpdateContact;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 9/29/17.
 */

public class ContactEditorPresenter implements ContactEditorContract.Presenter {

    private final ContactEditorContract.View mView;
    private final UseCaseHandler mUseCaseHandler;
    private final CreateContact mCreateContact;
    private final UpdateContact mUpdateContact;
    private final GetContact mGetContact;
//    private final Updat mUseCaseHandler;

    public ContactEditorPresenter(@NonNull ContactEditorContract.View view,
                                  @NonNull UseCaseHandler useCaseHandler,
                                  @NonNull CreateContact createContact,
                                  @NonNull UpdateContact updateContact,
                                  @NonNull GetContact getContact) {
        mView = view;

        mUseCaseHandler = useCaseHandler;
        mCreateContact = createContact;
        mUpdateContact = updateContact;
        mGetContact = getContact;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void createContact(Context context, Contact contact) {
        mView.showLoading(true);
        CreateContact.RequestValues requestValues = new CreateContact.RequestValues(contact);
        mUseCaseHandler.execute(mCreateContact, requestValues, new UseCase.UseCaseCallback<CreateContact.ResponseValue>() {
            @Override
            public void onSuccess(CreateContact.ResponseValue response) {
                mView.showLoading(false);
                mView.createContactSuccess(response.getContact());
            }

            @Override
            public void onError() {
                mView.showLoading(false);
                mView.createContactFail();
            }
        });
    }

    @Override
    public void updateContact(Context context, final Contact contact) {
        mView.showLoading(true);
        UpdateContact.RequestValues requestValues = new UpdateContact.RequestValues(contact);
        mUseCaseHandler.execute(mUpdateContact, requestValues, new UseCase.UseCaseCallback<UpdateContact.ResponseValue>() {
            @Override
            public void onSuccess(UpdateContact.ResponseValue response) {
                mView.showLoading(false);
                mView.updateContactSuccess(contact);
            }

            @Override
            public void onError() {
                mView.showLoading(false);
                mView.updateContactFail();
            }
        });
    }

    @Override
    public void getContact(Context context, String contactId) {
        DebugTool.logD("CONTACT ID: " + contactId);
        GetContact.RequestValues requestValues = new GetContact.RequestValues(contactId);
        mUseCaseHandler.execute(mGetContact, requestValues, new UseCase.UseCaseCallback<GetContact.ResponseValue>() {
            @Override
            public void onSuccess(GetContact.ResponseValue response) {
                DebugTool.logD("VKL: " + response.getContact().getDisplayName());
                mView.getContactSuccess(response.getContact());
            }

            @Override
            public void onError() {
                mView.getContactFail();
//                mView.createContactFail();
            }
        });
    }
}
