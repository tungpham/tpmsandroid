package com.android.morephone.data.entity.phonenumbers;

/**
 * Created by Ethan on 7/26/17.
 */

public class PhoneNumber {

    private String id;
    private String sid;
    private String phoneNumber;
    private String friendlyName;
    private String userId;
    private long createdAt;
    private long updatedAt;

    public PhoneNumber() {
    }

    private PhoneNumber(Builder builder) {
        this.sid = builder.sid;
        this.phoneNumber = builder.phoneNumber;
        this.friendlyName = builder.friendlyName;
        this.userId = builder.userId;
    }

    public static Builder getBuilder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFriendlyName() {
        return friendlyName;
    }

    public void setFriendlyName(String friendlyName) {
        this.friendlyName = friendlyName;
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

//    public void update(String token) {
//        this.token = token;
//    }

    @Override
    public String toString() {
        return String.format(
                "User[id=%s, sid=%s, friendlyName=%s]",
                this.id,
                this.sid,
                this.friendlyName
        );
    }


    public static class Builder {
        private String sid;
        private String phoneNumber;
        private String friendlyName;
        private String userId;

        private Builder() {
        }


        public Builder sid(String sid) {
            this.sid = sid;
            return this;
        }

        public Builder phoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
            return this;
        }

        public Builder friendlyName(String friendlyName) {
            this.friendlyName = friendlyName;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public PhoneNumber build() {

            PhoneNumber build = new PhoneNumber(this);

            build.checkSid(build.getSid());

            return build;
        }
    }

    private void checkSid(String sid) {
    }

}
