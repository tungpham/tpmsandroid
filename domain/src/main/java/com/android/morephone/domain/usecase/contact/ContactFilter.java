package com.android.morephone.domain.usecase.contact;

import com.android.morephone.data.entity.contact.Contact;

import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public interface ContactFilter {
    List<Contact> filter(List<Contact> contacts, String query);
}
