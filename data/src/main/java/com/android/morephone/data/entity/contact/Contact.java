package com.android.morephone.data.entity.contact;

import android.net.Uri;

/**
 * Created by truongnguyen on 9/26/17.
 */

public class Contact {

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
    private long createdAt;
    private long updatedAt;

    public Contact(String id, String displayName, String phoneNumber, String photoUri, String phoneNumberId, String address, String email, String birthday, String relationship, String note) {
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
    }

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
}
