package com.android.morephone.domain.usecase.contact;

/**
 * Created by truongnguyen on 10/4/17.
 */

public enum ContactFilterType {
    /**
     * Do not filter tasks.
     */
    ALL_CONTACTS,

    /**
     * Filters only the active (not completed yet) tasks.
     */
    SEARCH_CONTACTS,

}
