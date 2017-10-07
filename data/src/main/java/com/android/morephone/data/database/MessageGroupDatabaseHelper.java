package com.android.morephone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.contact.source.local.ContactPersistenceContract;
import com.android.morephone.data.repository.messagegroup.source.local.MessageGroupPersistenceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongnguyen on 10/3/17.
 */

public class MessageGroupDatabaseHelper {

    public static void insert(Context context, MessageGroup messageGroup) {
//        if(existsContact(context, contact.getId())){
//            updateContact(context, contact);
//        }else {
            SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_ID, messageGroup.getId());
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME, messageGroup.getName());
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE, messageGroup.getGroupPhone());
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID, messageGroup.getPhoneNumberId());
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID, messageGroup.getUserId());
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT, messageGroup.getCreatedAt());
            contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT, messageGroup.getUpdatedAt());


            long id = db.insert(MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, null, contentValues);
            DebugTool.logD("INSERT OK: " + id);
//        }
    }

    public static MessageGroup findMessageGroup(Context context, String messageGroupId) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, MessageGroupPersistenceContract.MessageGroupEntry.COL_ID + " = '" + messageGroupId + "'", null, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        MessageGroup messageGroup = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_ID));
                String name = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME));
                String groupPhone = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID));
                String userId = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT));

                messageGroup = MessageGroup.getBuilder()
                        .id(id)
                        .name(name)
                        .groupPhone(groupPhone)
                        .phoneNumberId(phoneNumberId)
                        .userId(userId)
                        .build();

                messageGroup.setCreatedAt(createdAt);
                messageGroup.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return messageGroup;
    }

    public static MessageGroup findMessageGroupPhoneNumber(Context context, String phoneNumberIdInput) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberIdInput)}, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        MessageGroup messageGroup = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_ID));
                String name = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME));
                String groupPhone = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID));
                String userId = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT));

                messageGroup = MessageGroup.getBuilder()
                        .id(id)
                        .name(name)
                        .groupPhone(groupPhone)
                        .phoneNumberId(phoneNumberId)
                        .userId(userId)
                        .build();

                messageGroup.setCreatedAt(createdAt);
                messageGroup.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return messageGroup;
    }

    public static void updateMessageGroup(Context context, MessageGroup messageGroup) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_ID, messageGroup.getId());
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME, messageGroup.getName());
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE, messageGroup.getGroupPhone());
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID, messageGroup.getPhoneNumberId());
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID, messageGroup.getUserId());
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT, messageGroup.getCreatedAt());
        contentValues.put(MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT, messageGroup.getUpdatedAt());

        int result = db.update(MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, contentValues, MessageGroupPersistenceContract.MessageGroupEntry.COL_ID + " = ? ", new String[]{String.valueOf(messageGroup.getId())});

        DebugTool.logD("UPDATE SQL: " + result);
    }

    public static List<MessageGroup> findAll(Context context, String phoneNumberIdInput) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberIdInput)}, null, null, ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME + " ASC");

        List<MessageGroup> messageGroups = new ArrayList<>();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_ID));
                String name = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_NAME));
                String groupPhone = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID));
                String userId = c.getString(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(MessageGroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT));

                MessageGroup messageGroup = MessageGroup.getBuilder()
                        .id(id)
                        .name(name)
                        .groupPhone(groupPhone)
                        .phoneNumberId(phoneNumberId)
                        .userId(userId)
                        .build();

                messageGroup.setCreatedAt(createdAt);
                messageGroup.setUpdatedAt(updatedAt);

                messageGroups.add(messageGroup);
            }
        }
        if (c != null) {
            c.close();
        }
        DebugTool.logD("SIZE PHONE SQL: " + messageGroups.size());
        return messageGroups;
    }

    public static boolean existsMessageGroup(Context context, String id) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                MessageGroupPersistenceContract.MessageGroupEntry.COL_ID,
        };


        Cursor cursor = db.query(
                MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, MessageGroupPersistenceContract.MessageGroupEntry.COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, BaseColumns._ID + " DESC LIMIT 0,1");
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public static void deleteMessageGroup(Context context, String id) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        int result = db.delete(MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, MessageGroupPersistenceContract.MessageGroupEntry.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public static void deleteAllMessageGroup(Context context, String phoneNumberId) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        int result = db.delete(MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, MessageGroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberId)});
    }

    public static void deleteAllMessageGroup(Context context) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        int result = db.delete(MessageGroupPersistenceContract.MessageGroupEntry.TABLE_NAME, null, null);
    }


}
