package com.android.morephone.data.repository.group.source.local;

import android.provider.BaseColumns;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class GroupPersistenceContract {

    private GroupPersistenceContract() {
    }

    public static abstract class MessageGroupEntry implements BaseColumns {
        public static final String TABLE_NAME = "message_group";
        public static final String COL_ID = "id";
        public static final String COL_NAME = "name";
        public static final String COL_GROUP_PHONE = "group_phone";
        public static final String COL_PHONE_NUMBER_ID = "phone_number_id";
        public static final String COL_USER_ID = "user_id";
        public static final String COL_CREATED_AT = "created_at";
        public static final String COL_UPDATED_AT = "updated_at";
    }
}
