package com.android.morephone.domain.usecase.contact;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by truongnguyen on 10/5/17.
 */

public class UpdateContact extends UseCase<UpdateContact.RequestValues, UpdateContact.ResponseValue> {

    private final ContactRepository mContactRepository;

    public UpdateContact(@NonNull ContactRepository contactRepository) {
        mContactRepository = contactRepository;
    }

    @Override
    protected void executeUseCase(final UpdateContact.RequestValues values) {

        mContactRepository.updateContact(values.getContact());

    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final Contact mContact;

        public RequestValues( Contact contact) {
            mContact = contact;
        }

        public Contact getContact(){
            return mContact;
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
