package com.android.morephone.domain.usecase.contact;

import android.support.annotation.NonNull;

import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by truongnguyen on 10/5/17.
 */

public class DeleteContact extends UseCase<DeleteContact.RequestValues, DeleteContact.ResponseValue> {

    private final ContactRepository mContactRepository;

    public DeleteContact(@NonNull ContactRepository contactRepository) {
        mContactRepository = contactRepository;
    }

    @Override
    protected void executeUseCase(final DeleteContact.RequestValues values) {
        mContactRepository.deleteContact(values.getContactId());
    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final String mContactId;

        public RequestValues(String contactId) {
            mContactId = contactId;
        }

        public String getContactId() {
            return mContactId;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        public ResponseValue() {

        }

    }
}
