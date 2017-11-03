package com.android.morephone.data.repository.contact.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;

import java.util.List;

/**
 * Created by truongnguyen on 9/28/17.
 */

public interface ContactDataSource {


    interface LoadContactsCallback {

        void onContactsLoaded(List<Contact> contacts);

        void onDataNotAvailable();
    }

    interface GetContactCallback {

        void onContactLoaded(Contact contact);

        void onDataNotAvailable();
    }

    void getContacts(@NonNull LoadContactsCallback callback);

    void getContacts(@NonNull String phoneNumberId, @NonNull LoadContactsCallback callback);

    void getContact(@NonNull String contactId, @NonNull GetContactCallback callback);

    void getContactBuyPhoneNumber(@NonNull String phoneNumber, @NonNull GetContactCallback callback);

    void saveContact(@NonNull Contact contact, @NonNull GetContactCallback callback);

    void updateContact(@NonNull Contact contact);

    void refreshContact();

    void deleteAllContact();

    void deleteContact(@NonNull String contactId);

    void deleteAllContact(@NonNull String phoneNumberId);

}
