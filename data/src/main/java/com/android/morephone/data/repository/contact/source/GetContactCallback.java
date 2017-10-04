package com.android.morephone.data.repository.contact.source;

import android.view.View;

import com.android.morephone.data.entity.contact.Contact;

/**
 * Created by truongnguyen on 10/4/17.
 */

public interface GetContactCallback {

    void onContactLoaded(View view, Contact contact);

    void onContactNotAvailable();

}
