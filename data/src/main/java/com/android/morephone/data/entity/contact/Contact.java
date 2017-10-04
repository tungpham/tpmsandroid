package com.android.morephone.data.entity.contact;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

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

    public Contact() {
    }

    private Contact(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.phoneNumber = builder.phoneNumber;
        this.photoUri = builder.photoUri;
        this.phoneNumberId = builder.phoneNumberId;
        this.address = builder.address;
        this.email = builder.email;
        this.userId = builder.userId;
        this.birthday = builder.birthday;
        this.relationship = builder.relationship;
        this.userId = builder.userId;
        Date date = new Date();
        createdAt = date.getTime();
        updatedAt = date.getTime();
    }

    public static Contact.Builder getBuilder() {
        return new Contact.Builder();
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, displayName=%s, email=%s]",
                this.id,
                this.displayName,
                this.email
        );
    }


    public static class Builder {

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

        private Builder() {
        }


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder photoUri(String photoUri) {
            this.photoUri = photoUri;
            return this;
        }

        public Builder phoneNumberId(String phoneNumberId) {
            this.phoneNumberId = phoneNumberId;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder birthday(String birthday) {
            this.birthday = birthday;
            return this;
        }

        public Builder relationship(String relationship) {
            this.relationship = relationship;
            return this;
        }

        public Builder note(String note) {
            this.note = note;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Contact build() {
            Contact build = new Contact(this);
            return build;
        }
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


}
