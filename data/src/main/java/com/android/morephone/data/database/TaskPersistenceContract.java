package com.android.morephone.data.database;

import android.provider.BaseColumns;

/**
 * Created by truongnguyen on 8/14/17.
 */

public class TaskPersistenceContract {

    private TaskPersistenceContract(){}

    public static abstract class TaskEntry implements BaseColumns {
        public static final String TABLE_NAME = "device";
        public static final String COL_STATUS = "status";
        public static final String COL_CREATED_AT = "created_at";
    }

}
