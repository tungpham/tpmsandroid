package com.android.morephone.data.repository.contact.source.local;

import android.provider.BaseColumns;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactPersistenceContract {

    private ContactPersistenceContract() {
    }

    public static abstract class ContactEntry implements BaseColumns {
        public static final String TABLE_NAME = "contact";
        public static final String COL_ID = "id";
        public static final String COL_DISPLAY_NAME = "display_name";
        public static final String COL_PHONE_NUMBER = "phone_number";
        public static final String COL_PHOTO_URI = "photo_uri";
        public static final String COL_PHONE_NUMBER_ID = "phone_number_id";
        public static final String COL_ADDRESS = "address";
        public static final String COL_EMAIL = "email";
        public static final String COL_BIRTHDAY = "birthday";
        public static final String COL_RELATIONSHIP = "relationship";
        public static final String COL_NOTE = "note";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_UPDATED_AT = "updated_at";
    }
}
