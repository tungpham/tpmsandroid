package com.android.morephone.domain.usecase.contact;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.data.repository.contact.source.ContactDataSource;
import com.android.morephone.domain.UseCase;


/**
 * Created by truongnguyen on 10/4/17.
 */

public class GetContact extends UseCase<GetContact.RequestValues, GetContact.ResponseValue> {

    private final ContactRepository mContactRepository;

    public GetContact(@NonNull ContactRepository contactRepository) {
        mContactRepository = contactRepository;
    }

    @Override
    protected void executeUseCase(final GetContact.RequestValues values) {

        mContactRepository.getContact(values.getContactId(), new ContactDataSource.GetContactCallback() {
            @Override
            public void onContactLoaded(Contact contact) {
                GetContact.ResponseValue responseValue = new GetContact.ResponseValue(contact);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final String mContactId;

        public RequestValues( String contactId) {
            mContactId = contactId;
        }

        public String getContactId(){
            return mContactId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Contact mContact;

        public ResponseValue(@NonNull Contact contact) {
            mContact = contact;
        }

        public Contact getContact() {
            return mContact;
        }
    }
}
