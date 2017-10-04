package com.android.morephone.domain.usecase.contact;

import com.android.morephone.data.entity.contact.Contact;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class AllContactFilter implements ContactFilter {
    @Override
    public List<Contact> filter(List<Contact> contacts, String query) {
        return new ArrayList<>(contacts);
    }
}