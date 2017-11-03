package com.android.morephone.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.contact.source.local.ContactPersistenceContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongnguyen on 10/3/17.
 */

public class ContactDatabaseHelper {

    public static void insert(Context context, Contact contact) {
//        if(existsContact(context, contact.getId())){
//            updateContact(context, contact);
//        }else {
            SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

            ContentValues contentValues = new ContentValues();
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_ID, contact.getId());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME, contact.getDisplayName());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER, contact.getPhoneNumber());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_PHOTO_URI, contact.getPhotoUri());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID, contact.getPhoneNumberId());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_ADDRESS, contact.getAddress());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_EMAIL, contact.getEmail());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_BIRTHDAY, contact.getBirthday());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP, contact.getRelationship());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_NOTE, contact.getNote());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_USER_ID, contact.getUserId());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_CREATED_AT, contact.getCreatedAt());
            contentValues.put(ContactPersistenceContract.ContactEntry.COL_UPDATED_AT, contact.getUpdatedAt());


            long id = db.insert(ContactPersistenceContract.ContactEntry.TABLE_NAME, null, contentValues);
            DebugTool.logD("INSERT OK: " + id);
//        }
    }

    public static Contact findContact(Context context, String contactId) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                ContactPersistenceContract.ContactEntry.COL_ID,
                ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME,
                ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER,
                ContactPersistenceContract.ContactEntry.COL_PHOTO_URI,
                ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID,
                ContactPersistenceContract.ContactEntry.COL_ADDRESS,
                ContactPersistenceContract.ContactEntry.COL_EMAIL,
                ContactPersistenceContract.ContactEntry.COL_BIRTHDAY,
                ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP,
                ContactPersistenceContract.ContactEntry.COL_NOTE,
                ContactPersistenceContract.ContactEntry.COL_USER_ID,
                ContactPersistenceContract.ContactEntry.COL_CREATED_AT,
                ContactPersistenceContract.ContactEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                ContactPersistenceContract.ContactEntry.TABLE_NAME, projection, ContactPersistenceContract.ContactEntry.COL_ID + " = '" + contactId + "'", null, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        Contact contact = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_ID));
                String displayName = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME));
                String phoneNumber = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER));
                String photoUri = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHOTO_URI));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID));
                String address = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_ADDRESS));
                String email = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_EMAIL));
                String birthday = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_BIRTHDAY));
                String relationship = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP));
                String note = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_NOTE));
                String userId = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_UPDATED_AT));

                contact = Contact.getBuilder()
                        .id(id)
                        .displayName(displayName)
                        .phoneNumber(phoneNumber)
                        .photoUri(photoUri)
                        .phoneNumberId(phoneNumberId)
                        .address(address)
                        .email(email)
                        .birthday(birthday)
                        .relationship(relationship)
                        .note(note)
                        .userId(userId)
                        .build();

                DebugTool.logD("DB: " + displayName);

                contact.setCreatedAt(createdAt);
                contact.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return contact;
    }

    public static Contact findContactBuyPhoneNumber(Context context, String phoneNumberInput) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                ContactPersistenceContract.ContactEntry.COL_ID,
                ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME,
                ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER,
                ContactPersistenceContract.ContactEntry.COL_PHOTO_URI,
                ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID,
                ContactPersistenceContract.ContactEntry.COL_ADDRESS,
                ContactPersistenceContract.ContactEntry.COL_EMAIL,
                ContactPersistenceContract.ContactEntry.COL_BIRTHDAY,
                ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP,
                ContactPersistenceContract.ContactEntry.COL_NOTE,
                ContactPersistenceContract.ContactEntry.COL_USER_ID,
                ContactPersistenceContract.ContactEntry.COL_CREATED_AT,
                ContactPersistenceContract.ContactEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                ContactPersistenceContract.ContactEntry.TABLE_NAME, projection, ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER + " = ?", new String[]{String.valueOf(phoneNumberInput)}, null, null, BaseColumns._ID + " DESC LIMIT 0,1");

        Contact contact = null;

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_ID));
                String displayName = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME));
                String phoneNumber = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER));
                String photoUri = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHOTO_URI));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID));
                String address = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_ADDRESS));
                String email = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_EMAIL));
                String birthday = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_BIRTHDAY));
                String relationship = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP));
                String note = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_NOTE));
                String userId = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_UPDATED_AT));

                contact = Contact.getBuilder()
                        .id(id)
                        .displayName(displayName)
                        .phoneNumber(phoneNumber)
                        .photoUri(photoUri)
                        .phoneNumberId(phoneNumberId)
                        .address(address)
                        .email(email)
                        .birthday(birthday)
                        .relationship(relationship)
                        .note(note)
                        .userId(userId)
                        .build();

                contact.setCreatedAt(createdAt);
                contact.setUpdatedAt(updatedAt);
            }
        }
        if (c != null) {
            c.close();
        }
        return contact;
    }

    public static void updateContact(Context context, Contact contact) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_ID, contact.getId());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME, contact.getDisplayName());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER, contact.getPhoneNumber());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_PHOTO_URI, contact.getPhotoUri());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID, contact.getPhoneNumberId());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_ADDRESS, contact.getAddress());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_EMAIL, contact.getEmail());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_BIRTHDAY, contact.getBirthday());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP, contact.getRelationship());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_NOTE, contact.getNote());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_USER_ID, contact.getUserId());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_CREATED_AT, contact.getCreatedAt());
        contentValues.put(ContactPersistenceContract.ContactEntry.COL_UPDATED_AT, contact.getUpdatedAt());

        int result = db.update(ContactPersistenceContract.ContactEntry.TABLE_NAME, contentValues, ContactPersistenceContract.ContactEntry.COL_ID + " = ? ", new String[]{String.valueOf(contact.getId())});

        DebugTool.logD("UPDATE SQL: " + result);
    }

    public static List<Contact> findAll(Context context, String phoneNumberIdInput) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                ContactPersistenceContract.ContactEntry.COL_ID,
                ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME,
                ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER,
                ContactPersistenceContract.ContactEntry.COL_PHOTO_URI,
                ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID,
                ContactPersistenceContract.ContactEntry.COL_ADDRESS,
                ContactPersistenceContract.ContactEntry.COL_EMAIL,
                ContactPersistenceContract.ContactEntry.COL_BIRTHDAY,
                ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP,
                ContactPersistenceContract.ContactEntry.COL_NOTE,
                ContactPersistenceContract.ContactEntry.COL_USER_ID,
                ContactPersistenceContract.ContactEntry.COL_CREATED_AT,
                ContactPersistenceContract.ContactEntry.COL_UPDATED_AT
        };


        Cursor c = db.query(
                ContactPersistenceContract.ContactEntry.TABLE_NAME, projection, ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberIdInput)}, null, null, ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME + " ASC");

        List<Contact> contacts = new ArrayList<>();

        if (c != null && c.getCount() > 0) {
            while (c.moveToNext()) {
                String id = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_ID));
                String displayName = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_DISPLAY_NAME));
                String phoneNumber = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER));
                String photoUri = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHOTO_URI));
                String phoneNumberId = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID));
                String address = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_ADDRESS));
                String email = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_EMAIL));
                String birthday = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_BIRTHDAY));
                String relationship = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_RELATIONSHIP));
                String note = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_NOTE));
                String userId = c.getString(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_USER_ID));
                long createdAt = c.getLong(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_CREATED_AT));
                long updatedAt = c.getLong(c.getColumnIndexOrThrow(ContactPersistenceContract.ContactEntry.COL_UPDATED_AT));

                Contact contact = Contact.getBuilder()
                        .id(id)
                        .displayName(displayName)
                        .phoneNumber(phoneNumber)
                        .photoUri(photoUri)
                        .phoneNumberId(phoneNumberId)
                        .address(address)
                        .email(email)
                        .birthday(birthday)
                        .relationship(relationship)
                        .note(note)
                        .userId(userId)
                        .build();


                contact.setCreatedAt(createdAt);
                contact.setUpdatedAt(updatedAt);

                contacts.add(contact);
            }
        }
        if (c != null) {
            c.close();
        }
        DebugTool.logD("SIZE PHONE SQL: " + contacts.size());
        return contacts;
    }

    public static boolean existsContact(Context context, String id) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getReadableDatabase();

        String[] projection = {
                BaseColumns._ID,
                ContactPersistenceContract.ContactEntry.COL_ID,
        };


        Cursor cursor = db.query(
                ContactPersistenceContract.ContactEntry.TABLE_NAME, projection, ContactPersistenceContract.ContactEntry.COL_ID + " = ?", new String[]{String.valueOf(id)}, null, null, BaseColumns._ID + " DESC LIMIT 0,1");
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public static void deleteContact(Context context, String id) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        db.delete(ContactPersistenceContract.ContactEntry.TABLE_NAME, ContactPersistenceContract.ContactEntry.COL_ID + " = ?", new String[]{String.valueOf(id)});
    }

    public static void deleteAllContact(Context context, String phoneNumberId) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        db.delete(ContactPersistenceContract.ContactEntry.TABLE_NAME, ContactPersistenceContract.ContactEntry.COL_PHONE_NUMBER_ID + " = ?", new String[]{String.valueOf(phoneNumberId)});
    }

    public static void deleteAllContact(Context context) {
        SQLiteDatabase db = DatabaseDAO.getInstance(context).getWritableDatabase();
        db.delete(ContactPersistenceContract.ContactEntry.TABLE_NAME, null, null);
    }


}
