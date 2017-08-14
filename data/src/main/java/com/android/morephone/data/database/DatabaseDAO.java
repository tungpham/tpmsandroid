package com.android.morephone.data.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;


/**
 * Created by aspsine on 15-4-19.
 */
public class DatabaseDAO extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "phone.db";

    private static final String TEXT_TYPE = " TEXT";

    private static final String INTEGER_TYPE = " INTEGER";

    private static final String COMMA_SEP = ",";

    private static void createTableTask(SQLiteDatabase db) {
        String sql =
                "CREATE TABLE " + TaskPersistenceContract.TaskEntry.TABLE_NAME
                        + " ("
                        + BaseColumns._ID + INTEGER_TYPE  + " PRIMARY KEY AUTOINCREMENT " + COMMA_SEP
                        + TaskPersistenceContract.TaskEntry.COL_STATUS + TEXT_TYPE + COMMA_SEP
                        + TaskPersistenceContract.TaskEntry.COL_CREATED_AT + TEXT_TYPE
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
        createTableTask(db);
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
