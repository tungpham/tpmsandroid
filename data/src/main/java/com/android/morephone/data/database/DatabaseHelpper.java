package com.android.morephone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by truongnguyen on 8/14/17.
 */

public class DatabaseHelpper {

    public static void insert(Context context, String status){
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskPersistenceContract.TaskEntry.COL_STATUS, status);
        contentValues.put(TaskPersistenceContract.TaskEntry.COL_CREATED_AT, getTimeDate(System.currentTimeMillis()));

        long id = db.insert(TaskPersistenceContract.TaskEntry.TABLE_NAME, null, contentValues);

    }

    public static String getTimeDate(long time) {
        Format formatter = new SimpleDateFormat("dd-MM HH:mm:ss");
        return formatter.format(new Date(time));
    }

    public static String formatDate(String date) {
        SimpleDateFormat in = new SimpleDateFormat("E, d MMM yyyy HH:mm:ss Z");
        SimpleDateFormat out = new SimpleDateFormat("dd-MM HH:mm:ss");

        try {
            Date time = in.parse(date);
            return out.format(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
