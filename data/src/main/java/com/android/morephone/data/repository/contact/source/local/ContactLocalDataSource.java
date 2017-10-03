package com.android.morephone.data.repository.contact.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.database.ContactDatabaseHelper;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.repository.contact.source.ContactDataSource;

import java.util.List;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactLocalDataSource implements ContactDataSource {

    private static ContactLocalDataSource INSTANCE;
    private Context mContext;


    // Prevent direct instantiation.
    private ContactLocalDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static ContactLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContactLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getContacts(@NonNull LoadContactsCallback callback) {

    }

    @Override
    public void getContacts(@NonNull String phoneNumberId, @NonNull LoadContactsCallback callback) {
        List<Contact> contacts = ContactDatabaseHelper.findAll(mContext, phoneNumberId);
        if (contacts != null && !contacts.isEmpty()) {
            callback.onContactsLoaded(contacts);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getContact(@NonNull String contactId, @NonNull GetContactCallback callback) {
        Contact contact = ContactDatabaseHelper.findContact(mContext, contactId);
        if (contact != null) {
            callback.onContactLoaded(contact);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        ContactDatabaseHelper.insert(mContext, contact);
    }

    @Override
    public void updateContact(@NonNull Contact contact) {
        ContactDatabaseHelper.updateContact(mContext, contact);
    }

    @Override
    public void refreshContact() {

    }

    @Override
    public void deleteAllContact() {

    }

    @Override
    public void deleteContact(@NonNull String contactId) {
        ContactDatabaseHelper.deleteContact(mContext, contactId);
    }

    @Override
    public void deleteAllContact(@NonNull String phoneNumberId) {
        ContactDatabaseHelper.deleteAllContact(mContext, phoneNumberId);
    }
}
