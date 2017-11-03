package com.android.morephone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.contact.source.local.ContactPersistenceContract;
import com.android.morephone.data.repository.group.source.local.GroupPersistenceContract;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by truongnguyen on 10/3/17.
 */

public class MessageGroupDatabaseHelper {

    public static void insert(Context context, Group group) {
//        if(existsContact(context, contact.getId())){
//            updateContact(context, contact);
//        }else {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

        String groupPhone = TextUtils.join(",", group.getGroupPhone());

        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_ID, group.getId());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_NAME, group.getName());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE, groupPhone);
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID, group.getPhoneNumberId());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_USER_ID, group.getUserId());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT, group.getCreatedAt());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT, group.getUpdatedAt());


        long id = db.insert(GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, null, contentValues);
        DebugTool.logD("INSERT OK: " + id);
//        }
    }

    public static Group findMessageGroup(Context context, String messageGroupId) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                GroupPersistenceContract.MessageGroupEntry.COL_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_NAME,
                GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE,
                GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_USER_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT,
                GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, GroupPersistenceContract.MessageGroupEntry.COL_ID + " = '" + messageGroupId + "'", null, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        Group group = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_NAME));
                String groupPhone = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID));
                String userId = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT));

                Group.Builder builder = Group.getBuilder();
                builder.id(id)
                        .name(name)
                        .phoneNumberId(phoneNumberId)
                        .userId(userId);


                if (!TextUtils.isEmpty(groupPhone) && groupPhone.contains(",")) {
                    String[] text = groupPhone.split(",");
                    builder.groupPhone(Arrays.asList(text));
                }
                group = builder.build();

                group.setCreatedAt(createdAt);
                group.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return group;
    }

    public static Group findMessageGroupPhoneNumber(Context context, String phoneNumberIdInput) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                GroupPersistenceContract.MessageGroupEntry.COL_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_NAME,
                GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE,
                GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_USER_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT,
                GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberIdInput)}, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        Group group = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_NAME));
                String groupPhone = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID));
                String userId = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT));

                Group.Builder builder = Group.getBuilder();
                builder.id(id)
                        .name(name)
                        .phoneNumberId(phoneNumberId)
                        .userId(userId);


                if (!TextUtils.isEmpty(groupPhone) && groupPhone.contains(",")) {
                    String[] text = groupPhone.split(",");
                    builder.groupPhone(Arrays.asList(text));
                }
                group = builder.build();


                group.setCreatedAt(createdAt);
                group.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return group;
    }

    public static void updateMessageGroup(Context context, Group group) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

        String groupPhone = TextUtils.join(",", group.getGroupPhone());

        ContentValues contentValues = new ContentValues();
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_ID, group.getId());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_NAME, group.getName());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE, groupPhone);
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID, group.getPhoneNumberId());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_USER_ID, group.getUserId());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT, group.getCreatedAt());
        contentValues.put(GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT, group.getUpdatedAt());

        int result = db.update(GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, contentValues, GroupPersistenceContract.MessageGroupEntry.COL_ID + " = ? ", new String[]{String.valueOf(group.getId())});

        DebugTool.logD("UPDATE SQL: " + result);
    }

    public static List<Group> findAll(Context context, String phoneNumberIdInput) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                GroupPersistenceContract.MessageGroupEntry.COL_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_NAME,
                GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE,
                GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_USER_ID,
                GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT,
                GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberIdInput)}, null, null, ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME + " ASC");

        List<Group> groups = new ArrayList<>();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_ID));
                String name = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_NAME));
                String groupPhone = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_GROUP_PHONE));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID));
                String userId = c.getString(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(GroupPersistenceContract.MessageGroupEntry.COL_UPDATED_AT));

                Group.Builder builder = Group.getBuilder();
                builder.id(id)
                        .name(name)
                        .phoneNumberId(phoneNumberId)
                        .userId(userId);


                if (!TextUtils.isEmpty(groupPhone) && groupPhone.contains(",")) {
                    String[] text = groupPhone.split(",");
                    builder.groupPhone(Arrays.asList(text));
                }
                Group group = builder.build();

                group.setCreatedAt(createdAt);
                group.setUpdatedAt(updatedAt);

                groups.add(group);
            }
        }
        if (c != null) {
            c.close();
        }
        DebugTool.logD("SIZE PHONE SQL: " + groups.size());
        return groups;
    }

    public static boolean existsMessageGroup(Context context, String id) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                GroupPersistenceContract.MessageGroupEntry.COL_ID,
        };


        Cursor cursor = db.query(
                GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, projection, GroupPersistenceContract.MessageGroupEntry.COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, BaseColumns._ID + " DESC LIMIT 0,1");
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public static void deleteMessageGroup(Context context, String id) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        db.delete(GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, GroupPersistenceContract.MessageGroupEntry.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public static void deleteAllMessageGroup(Context context, String phoneNumberId) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        db.delete(GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, GroupPersistenceContract.MessageGroupEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberId)});
    }

    public static void deleteAllMessageGroup(Context context) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        db.delete(GroupPersistenceContract.MessageGroupEntry.TABLE_NAME, null, null);
    }


}
