package com.android.morephone.domain.usecase.contact;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class ContactFactory {

    private static final Map<ContactFilterType, ContactFilter> mFilters = new HashMap<>();

    public ContactFactory() {
        mFilters.put(ContactFilterType.ALL_CONTACTS, new AllContactFilter());
        mFilters.put(ContactFilterType.SEARCH_CONTACTS, new SearchContactFilter());
    }

    public ContactFilter create(ContactFilterType filterType) {
        return mFilters.get(filterType);
    }

}
