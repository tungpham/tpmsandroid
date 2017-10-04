package com.android.morephone.data.repository.contact;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.repository.contact.source.ContactDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactRepository implements ContactDataSource {

    private static ContactRepository INSTANCE = null;

    private final ContactDataSource mContactsRemoteDataSource;

    private final ContactDataSource mContactLocalDataSource;

    Map<String, Contact> mCachedContacts;

    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private ContactRepository(@NonNull ContactDataSource contactsRemoteDataSource,
                            @NonNull ContactDataSource contactsLocalDataSource) {
        mContactsRemoteDataSource = contactsRemoteDataSource;
        mContactLocalDataSource = contactsLocalDataSource;
    }

    public static ContactRepository getInstance(ContactDataSource contactsRemoteDataSource,
                                                ContactDataSource contactsLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new ContactRepository(contactsRemoteDataSource, contactsLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getContacts(@NonNull LoadContactsCallback callback) {

    }

    @Override
    public void getContacts(@NonNull final String phoneNumberId, @NonNull final LoadContactsCallback callback) {
// Respond immediately with cache if available and not dirty
        if (mCachedContacts != null && !mCacheIsDirty) {
            callback.onContactsLoaded(new ArrayList<>(mCachedContacts.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getContactsFromRemoteDataSource(phoneNumberId, callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mContactLocalDataSource.getContacts(new LoadContactsCallback() {
                @Override
                public void onContactsLoaded(List<Contact> tasks) {
                    refreshCache(tasks);
                    callback.onContactsLoaded(new ArrayList<>(mCachedContacts.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getContactsFromRemoteDataSource(phoneNumberId, callback);
                }
            });
        }
    }

    @Override
    public void getContact(@NonNull final String contactId, @NonNull final GetContactCallback callback) {
        Contact cachedTask = getContactWithId(contactId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onContactLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mContactLocalDataSource.getContact(contactId, new GetContactCallback() {
            @Override
            public void onContactLoaded(Contact task) {
                callback.onContactLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mContactsRemoteDataSource.getContact(contactId, new GetContactCallback() {
                    @Override
                    public void onContactLoaded(Contact contact) {
                        callback.onContactLoaded(contact);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getContactBuyPhoneNumber(@NonNull final String phoneNumber, @NonNull final GetContactCallback callback) {
        Contact cachedTask = getContactBuyPhoneNumber(phoneNumber);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onContactLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        // Is the task in the local data source? If not, query the network.
        mContactLocalDataSource.getContactBuyPhoneNumber(phoneNumber, new GetContactCallback() {
            @Override
            public void onContactLoaded(Contact task) {
                callback.onContactLoaded(task);
            }

            @Override
            public void onDataNotAvailable() {
                mContactsRemoteDataSource.getContact(phoneNumber, new GetContactCallback() {
                    @Override
                    public void onContactLoaded(Contact contact) {
                        callback.onContactLoaded(contact);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        mContactsRemoteDataSource.saveContact(contact);
        mContactLocalDataSource.saveContact(contact);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedContacts == null) {
            mCachedContacts = new LinkedHashMap<>();
        }
        mCachedContacts.put(contact.getId(), contact);
    }

    @Override
    public void updateContact(@NonNull Contact contact) {
        mContactsRemoteDataSource.updateContact(contact);
        mContactLocalDataSource.updateContact(contact);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedContacts == null) {
            mCachedContacts = new LinkedHashMap<>();
        }
        mCachedContacts.put(contact.getId(), contact);
    }

    @Override
    public void refreshContact() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllContact() {

    }

    @Override
    public void deleteContact(@NonNull String contactId) {
        mContactLocalDataSource.deleteContact(contactId);
        mContactsRemoteDataSource.deleteContact(contactId);

        if (mCachedContacts == null) {
            mCachedContacts = new LinkedHashMap<>();
        }
        mCachedContacts.remove(contactId);
    }

    @Override
    public void deleteAllContact(@NonNull String phoneNumberId) {
        mContactLocalDataSource.deleteAllContact(phoneNumberId);
        mContactsRemoteDataSource.deleteAllContact(phoneNumberId);

        if (mCachedContacts == null) {
            mCachedContacts = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, Contact>> it = mCachedContacts.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Contact> entry = it.next();
            if (entry.getValue().getPhoneNumberId().equals(phoneNumberId)) {
                it.remove();
            }
        }
    }

    private void getContactsFromRemoteDataSource(@NonNull final String phoneNumberId, @NonNull final LoadContactsCallback callback) {
        mContactsRemoteDataSource.getContacts(phoneNumberId, new LoadContactsCallback() {
            @Override
            public void onContactsLoaded(List<Contact> contacts) {
                refreshCache(contacts);
                refreshLocalDataSource(phoneNumberId, contacts);
                callback.onContactsLoaded(new ArrayList<>(mCachedContacts.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Contact> contacts) {
        if (mCachedContacts == null) {
            mCachedContacts = new LinkedHashMap<>();
        }
        mCachedContacts.clear();
        for (Contact contact : contacts) {
            mCachedContacts.put(contact.getId(), contact);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(String phoneNumberId, List<Contact> contacts) {
        mContactLocalDataSource.deleteAllContact(phoneNumberId);
        for (Contact contact : contacts) {
            mContactLocalDataSource.saveContact(contact);
        }
    }

    @Nullable
    private Contact getContactWithId(@NonNull String id) {
        if (mCachedContacts == null || mCachedContacts.isEmpty()) {
            return null;
        } else {
            return mCachedContacts.get(id);
        }
    }

    @Nullable
    private Contact getContactBuyPhoneNumber(@NonNull String phoneNumber) {
        if (mCachedContacts == null || mCachedContacts.isEmpty()) {
            return null;
        } else {
            Iterator<Map.Entry<String, Contact>> it = mCachedContacts.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Contact> entry = it.next();
                if (entry.getValue().getPhoneNumber().equals(phoneNumber)) {
                   return entry.getValue();
                }
            }
        }
        return null;
    }
}
