package com.android.morephone.domain.usecase.contact;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.data.repository.contact.source.ContactDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class GetContactByPhoneNumber extends UseCase<GetContactByPhoneNumber.RequestValues, GetContactByPhoneNumber.ResponseValue> {

    private final ContactRepository mContactRepository;

    public GetContactByPhoneNumber(@NonNull ContactRepository contactRepository) {
        mContactRepository = contactRepository;
    }

    @Override
    protected void executeUseCase(final GetContactByPhoneNumber.RequestValues values) {

        mContactRepository.getContactBuyPhoneNumber(values.getPhoneNumber(), new ContactDataSource.GetContactCallback() {
            @Override
            public void onContactLoaded(Contact contact) {
                GetContactByPhoneNumber.ResponseValue responseValue = new GetContactByPhoneNumber.ResponseValue(contact);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final String mPhoneNumber;

        public RequestValues( String phoneNumber) {
            mPhoneNumber = phoneNumber;
        }

        public String getPhoneNumber(){
            return mPhoneNumber;
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
