package com.android.morephone.data.repository.contact.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.repository.contact.source.ContactDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactRemoteDataSource implements ContactDataSource {

    private static ContactRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Contact> TASKS_SERVICE_DATA;

    private Context mContext;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
    }

    public static ContactRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new ContactRemoteDataSource(context);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private ContactRemoteDataSource(Context context) {
        mContext = context;
    }

    @Override
    public void getContacts(@NonNull LoadContactsCallback callback) {

    }

    @Override
    public void getContacts(@NonNull String phoneNumberId, @NonNull final LoadContactsCallback callback) {
        ApiMorePhone.loadContacts(mContext, phoneNumberId, new Callback<BaseResponse<List<Contact>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Contact>>> call, Response<BaseResponse<List<Contact>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Contact contact : response.body().getResponse()) {
                        TASKS_SERVICE_DATA.put(contact.getId(), contact);
                    }
                    callback.onContactsLoaded(response.body().getResponse());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Contact>>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getContact(@NonNull String contactId, @NonNull GetContactCallback callback) {
        Contact contact = TASKS_SERVICE_DATA.get(contactId);
        if (contact != null)
            callback.onContactLoaded(contact);
        else callback.onDataNotAvailable();
    }

    @Override
    public void saveContact(@NonNull Contact contact) {
        ApiMorePhone.createContact(mContext, contact, new Callback<BaseResponse<Contact>>() {
            @Override
            public void onResponse(Call<BaseResponse<Contact>> call, Response<BaseResponse<Contact>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TASKS_SERVICE_DATA.put(response.body().getResponse().getId(), response.body().getResponse());
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Contact>> call, Throwable t) {

            }
        });
    }

    @Override
    public void updateContact(@NonNull final Contact contact) {
        ApiMorePhone.updateContact(mContext, contact, new Callback<BaseResponse<Contact>>() {
            @Override
            public void onResponse(Call<BaseResponse<Contact>> call, Response<BaseResponse<Contact>> response) {
                if(response.isSuccessful() && response.body() != null){
                    TASKS_SERVICE_DATA.put(contact.getId(), contact);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Contact>> call, Throwable t) {

            }
        });
    }

    @Override
    public void refreshContact() {

    }

    @Override
    public void deleteAllContact() {

    }

    @Override
    public void deleteContact(@NonNull final String contactId) {
        ApiMorePhone.deleteContact(mContext, contactId, new Callback<com.android.morephone.data.entity.Response>() {
            @Override
            public void onResponse(Call<com.android.morephone.data.entity.Response> call, Response<com.android.morephone.data.entity.Response> response) {
                if (response.isSuccessful() && response.body() != null) {
                    TASKS_SERVICE_DATA.remove(contactId);
                }
            }

            @Override
            public void onFailure(Call<com.android.morephone.data.entity.Response> call, Throwable t) {

            }
        });

    }

    @Override
    public void deleteAllContact(@NonNull String phoneNumberId) {
        for (String key : TASKS_SERVICE_DATA.keySet()) {
            if (TASKS_SERVICE_DATA.get(key).getPhoneNumberId().equals(phoneNumberId)) {
                TASKS_SERVICE_DATA.remove(key);
            }
        }
    }
}
