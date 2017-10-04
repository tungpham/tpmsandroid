package com.android.morephone.domain.usecase.contact;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.data.repository.contact.source.ContactDataSource;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class GetContacts extends UseCase<GetContacts.RequestValues, GetContacts.ResponseValue> {

    private final ContactRepository mTasksRepository;

    private final ContactFactory mFilterFactory;

    public GetContacts(@NonNull ContactRepository contactRepository, @NonNull ContactFactory filterFactory) {
        mTasksRepository = contactRepository;
        mFilterFactory = filterFactory;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            mTasksRepository.refreshContact();
        }

        mTasksRepository.getContacts(values.getPhoneNumber(), new ContactDataSource.LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<Contact> tasks) {
                ContactFilterType currentFiltering = values.getCurrentFiltering();
                ContactFilter taskFilter = mFilterFactory.create(currentFiltering);

                List<Contact> tasksFiltered = taskFilter.filter(tasks, values.getQuery());
                ResponseValue responseValue = new ResponseValue(tasksFiltered);
                getUseCaseCallback().onSuccess(responseValue);
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });

    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final ContactFilterType mCurrentFiltering;
        private final boolean mForceUpdate;
        private final String mPhoneNumberId;
        private final String mQuery;

        public RequestValues(boolean forceUpdate, String phoneNumberId, String query, @NonNull ContactFilterType currentFiltering) {
            mForceUpdate = forceUpdate;
            mCurrentFiltering = currentFiltering;
            mPhoneNumberId = phoneNumberId;
            mQuery = query;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public ContactFilterType getCurrentFiltering() {
            return mCurrentFiltering;
        }

        public String getPhoneNumber(){
            return mPhoneNumberId;
        }

        public String getQuery(){
            return mQuery;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Contact> mContacts;

        public ResponseValue(@NonNull List<Contact> contacts) {
            mContacts = contacts;
        }

        public List<Contact> getContacts() {
            return mContacts;
        }
    }
}
