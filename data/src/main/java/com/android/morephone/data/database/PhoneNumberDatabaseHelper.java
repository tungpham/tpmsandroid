package com.android.morephone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.phonenumbers.incoming.source.local.PhoneNumberPersistenceContract;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by truongnguyen on 8/14/17.
 */

public class PhoneNumberDatabaseHelper {

    public static void insert(Context context, PhoneNumber phoneNumber) {
        if (existsPhoneNumber(context, phoneNumber.getSid())) {
            DebugTool.logD("EXISTS");
            updatePhoneNumber(context, phoneNumber);
        } else {
            SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID, phoneNumber.getSid());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID, phoneNumber.getId());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID, phoneNumber.getUserId());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE, phoneNumber.getExpire());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL, phoneNumber.getForwardEmail());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER, phoneNumber.getForwardPhoneNumber());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME, phoneNumber.getFriendlyName());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER, phoneNumber.getPhoneNumber());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL, phoneNumber.isPool() ? 1 : 0);
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD, phoneNumber.isForward() ? 1 : 0);
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT, phoneNumber.getCreatedAt());
            contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT, phoneNumber.getUpdatedAt());


            long id = db.insert(PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, null, contentValues);
            DebugTool.logD("INSERT OK: " + id);
        }
    }

    public static PhoneNumber findPhoneNumber(Context context, String phoneNumber) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, projection, PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER + " = '" + phoneNumber + "'", null, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        PhoneNumber number = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID));
                String sid = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID));
                String userId = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID));
                long expire = c.getLong(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE));
                String forwardEmail = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL));
                String forwardPhoneNumber = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER));
                String friendlyName = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME));
                String phone = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER));
                boolean pool = c.getInt(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL)) == 1;
                boolean isForward = c.getInt(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD)) == 1;
                long createdAt = c.getLong(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT));

                number = PhoneNumber.getBuilder()
                        .phoneNumber(phone)
                        .forwardPhoneNumber(forwardPhoneNumber)
                        .forwardEmail(forwardEmail)
                        .expire(expire)
                        .sid(sid)
                        .userId(userId)
                        .friendlyName(friendlyName)
                        .pool(pool)
                        .isForward(isForward)
                        .build();
                number.setId(id);
                number.setCreatedAt(createdAt);
                number.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return number;
    }

    public static boolean existsPhoneNumber(Context context, String sid) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID
        };


        Cursor cursor = db.query(
                PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, projection, PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID + " = '" + sid + "'", null, null, null, BaseColumns._ID + " DESC LIMIT 0,1");
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public static void updatePhoneNumber(Context context, PhoneNumber phoneNumber) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID, phoneNumber.getSid());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID, phoneNumber.getId());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID, phoneNumber.getUserId());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE, phoneNumber.getExpire());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL, phoneNumber.getForwardEmail());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER, phoneNumber.getForwardPhoneNumber());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME, phoneNumber.getFriendlyName());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER, phoneNumber.getPhoneNumber());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL, phoneNumber.isPool() ? 1 : 0);
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD, phoneNumber.isForward() ? 1 : 0);
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT, phoneNumber.getCreatedAt());
        contentValues.put(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT, phoneNumber.getUpdatedAt());


        int result = db.update(PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, contentValues, PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID + " = ? ", new String[]{String.valueOf(phoneNumber.getSid())});

        DebugTool.logD("UPDATE SQL: " + result);
    }

    public static List<PhoneNumber> findAll(Context context) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT,
                PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, projection, null, null, null, null, null);

        List<PhoneNumber> phoneNumbers = new ArrayList<>();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_ID));
                String sid = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID));
                String userId = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_USER_ID));
                long expire = c.getLong(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_EXPIRE));
                String forwardEmail = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_EMAIL));
                String forwardPhoneNumber = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FORWARD_PHONE_NUMBER));
                String friendlyName = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_FRIENDLY_NAME));
                String phone = c.getString(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_PHONE_NUMBER));
                boolean pool = c.getInt(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_POOL)) == 1;
                boolean isForward = c.getInt(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_IS_FORWARD)) == 1;
                long createdAt = c.getLong(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(PhoneNumberPersistenceContract.PhoneNumberEntry.COL_UPDATED_AT));

                PhoneNumber number = PhoneNumber.getBuilder()
                        .phoneNumber(phone)
                        .forwardPhoneNumber(forwardPhoneNumber)
                        .forwardEmail(forwardEmail)
                        .expire(expire)
                        .sid(sid)
                        .userId(userId)
                        .friendlyName(friendlyName)
                        .pool(pool)
                        .isForward(isForward)
                        .build();
                number.setId(id);
                number.setCreatedAt(createdAt);
                number.setUpdatedAt(updatedAt);

                phoneNumbers.add(number);
            }
        }
        if (c != null) {
            c.close();
        }
        DebugTool.logD("SIZE PHONE SQL: " + phoneNumbers.size());
        return phoneNumbers;
    }

    public static void deletePhoneNumber(Context context, String sid) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        int result = db.delete(PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, PhoneNumberPersistenceContract.PhoneNumberEntry.COL_SID + " = ?", new String[]{String.valueOf(sid)});
    }

    public static void deleteAllPhoneNumber(Context context) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        int result = db.delete(PhoneNumberPersistenceContract.PhoneNumberEntry.TABLE_NAME, null, null);
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
