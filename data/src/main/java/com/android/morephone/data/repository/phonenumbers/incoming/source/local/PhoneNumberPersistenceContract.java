package com.android.morephone.data.repository.phonenumbers.incoming.source.local;

import android.provider.BaseColumns;

/**
 * Created by truongnguyen on 8/24/17.
 */

public class PhoneNumberPersistenceContract {

    private PhoneNumberPersistenceContract() {
    }

    public static abstract class PhoneNumberEntry implements BaseColumns {
        public static final String TABLE_NAME = "phone_number";
        public static final String COL_ID = "id";
        public static final String COL_SID = "sid";
        public static final String COL_PHONE_NUMBER = "phone_number";
        public static final String COL_FRIENDLY_NAME = "friendly_name";
        public static final String COL_IS_FORWARD = "is_forward";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_EXPIRE = "expire";
        public static final String COL_POOL = "pool";
        public static final String COL_FORWARD_PHONE_NUMBER = "forward_phone_number";
        public static final String COL_FORWARD_EMAIL = "forward_email";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_UPDATED_AT = "updated_at";
    }

}
