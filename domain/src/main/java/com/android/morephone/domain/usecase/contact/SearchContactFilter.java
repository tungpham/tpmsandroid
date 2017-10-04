package com.android.morephone.domain.usecase.contact;

import com.android.morephone.data.entity.contact.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class SearchContactFilter implements ContactFilter {
    @Override
    public List<Contact> filter(List<Contact> contacts, String query) {
        List<Contact> filteredContacts = new ArrayList<>();

        for (Contact contact : contacts) {
//            if (task.isCompleted()) {
                filteredContacts.add(contact);
//            }
        }
        return filteredContacts;
    }
}
