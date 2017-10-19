package com.android.morephone.data.entity.group;

import java.util.Date;
import java.util.List;

/**
 * Created by truongnguyen on 10/6/17.
 */

public class Group {

    private String id;
    private String name;
    private String phoneNumberId;
    private List<String> groupPhone;
    private String userId;
    private long createdAt;
    private long updatedAt;

    public Group() {
    }

    private Group(Group.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.groupPhone = builder.groupPhone;
        this.phoneNumberId = builder.phoneNumberId;
        this.userId = builder.userId;
        Date date = new Date();
        createdAt = date.getTime();
        updatedAt = date.getTime();
    }

    public static Group.Builder getBuilder() {
        return new Group.Builder();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGroupPhone() {
        return groupPhone;
    }

    public void setGroupPhone(List<String> groupPhone) {
        this.groupPhone = groupPhone;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public void setPhoneNumberId(String phoneNumberId) {
        this.phoneNumberId = phoneNumberId;
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
                this.id
        );
    }


    public static class Builder {

        private String id;
        private String name;
        private List<String> groupPhone;
        private String phoneNumberId;
        private String userId;

        private Builder() {
        }


        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder groupPhone(List<String> groupPhone) {
            this.groupPhone = groupPhone;
            return this;
        }

        public Builder phoneNumberId(String phoneNumberId) {
            this.phoneNumberId = phoneNumberId;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public Group build() {
            Group build = new Group(this);
            return build;
        }
    }
}
