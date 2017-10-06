package com.android.morephone.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.android.morephone.data.repository.contact.source.local.ContactPersistenceContract;
import com.android.morephone.data.repository.messagegroup.source.local.MessageGroupPersistenceContract;
import com.android.morephone.data.repository.phonenumbers.incoming.source.local.PhoneNumberPersistenceContract;


public class DatabaseDAO extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "phone.db";

    private static final String TEXT_TYPE = " TEXT";


    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static void createTablePhoneNumber(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME
                        + " ("
                        + BaseColumns._ID + INTEGER_TYPE  + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD + INTEGER_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE + INTEGER_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL + INTEGER_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL + TEXT_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT + INTEGER_TYPE + COMMA_SEP
                        + PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT + INTEGER_TYPE
                        + ")";

        db.execSQL(sql);
    }

    private static void createTableContact(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + ContactPersistenceContract.ContactEntry.TABLE_NAME
                        + " ("
                        + BaseColumns._ID + INTEGER_TYPE  + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_ID + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_PHOTO_URI + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_ADDRESS + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_EMAIL + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_BIRTHDAY + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_NOTE + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_USER_ID + TEXT_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_CREATED_AT + INTEGER_TYPE + COMMA_SEP
                        + ContactPersistenceContract.ContactEntry.COL_UPDATED_AT + INTEGER_TYPE
                        + ")";

        db.execSQL(sql);
    }

    private static void createTableMessageGroup(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME
                        + " ("
                        + BaseColumns._ID + INTEGER_TYPE  + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_ID + TEXT_TYPE + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME + TEXT_TYPE + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE + TEXT_TYPE + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + TEXT_TYPE + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID + TEXT_TYPE + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT + INTEGER_TYPE + COMMA_SEP
                        + MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT + INTEGER_TYPE
                        + ")";

        db.execSQL(sql);
    }

    private int mOpenCounter;

    private SQLiteDatabase database;
    private static DatabaseDAO instance;
    private final static Object sDbLock = new Object();

    public static synchronized DatabaseDAO getInstance(Context appContext) {
        if (instance == null) {
            synchronized (sDbLock) {
                if (instance == null) {
                    instance = new DatabaseDAO(appContext);
                }
            }
        }
        return instance;
    }

    private DatabaseDAO(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        createTablePhoneNumber(db);
        createTableContact(db);
        createTableMessageGroup(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    // If you need to add a column
//    if (newVersion > oldVersion) {
//        db.execSQL("ALTER TABLE foo ADD COLUMN new_column INTEGER DEFAULT 0");
//    }

    public synchronized SQLiteDatabase openDatabase() {
        mOpenCounter++;
        if (mOpenCounter == 1) {
            // Opening new database
            database = instance.getWritableDatabase();
        }
        return database;
    }

    public synchronized void closeDatabase() {
        mOpenCounter--;
        if (mOpenCounter == 0) {
            // Closing database
            database.close();
        }
    }

    public Cursor getData(String sql) {
        if (database.isOpen()) {
            return database.rawQuery(sql, null);
        } else {
            return null;
        }
    }

    public void deleteTable(String table) {
        database = this.getWritableDatabase();
        database.delete(table, "", null);
    }

}
