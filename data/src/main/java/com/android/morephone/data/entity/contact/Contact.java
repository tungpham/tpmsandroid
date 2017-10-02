package com.android.morephone.data.entity.contact;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by truongnguyen on 9/26/17.
 */

public class Contact implements Parcelable {

    private String id;
    private String displayName;
    private String phoneNumber;
    private String photoUri;
    private String phoneNumberId;
    private String address;
    private String email;
    private String birthday;
    private String relationship;
    private String note;
    private String userId;
    private long createdAt;
    private long updatedAt;

    public Contact(String id, String displayName, String phoneNumber, String photoUri, String phoneNumberId, String address, String email, String birthday, String relationship, String note, String userId) {
        this.id = id;
        this.displayName = displayName;
        this.phoneNumber = phoneNumber;
        this.photoUri = photoUri;
        this.phoneNumberId = phoneNumberId;
        this.address = address;
        this.email = email;
        this.birthday = birthday;
        this.relationship = relationship;
        this.note = note;
        this.userId = userId;
    }

    protected Contact(Parcel in) {
        id = in.readString();
        displayName = in.readString();
        phoneNumber = in.readString();
        photoUri = in.readString();
        phoneNumberId = in.readString();
        address = in.readString();
        email = in.readString();
        birthday = in.readString();
        relationship = in.readString();
        note = in.readString();
        userId = in.readString();
        createdAt = in.readLong();
        updatedAt = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(displayName);
        dest.writeString(phoneNumber);
        dest.writeString(photoUri);
        dest.writeString(phoneNumberId);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(birthday);
        dest.writeString(relationship);
        dest.writeString(note);
        dest.writeString(userId);
        dest.writeLong(createdAt);
        dest.writeLong(updatedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
