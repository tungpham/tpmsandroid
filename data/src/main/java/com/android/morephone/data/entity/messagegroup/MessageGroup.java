package com.android.morephone.data.entity.messagegroup;

import java.util.Date;
import java.util.List;

/**
 * Created by truongnguyen on 10/6/17.
 */

public class MessageGroup {

    private String id;
    private String name;
    private String groupPhone;
    private String phoneNumberId;
    private List<String> messagesSid;
    private String userId;
    private long createdAt;
    private long updatedAt;

    public MessageGroup() {
    }

    private MessageGroup(MessageGroup.Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.groupPhone = builder.groupPhone;
        this.phoneNumberId = builder.phoneNumberId;
        this.messagesSid = builder.messagesSid;
        this.userId = builder.userId;
        Date date = new Date();
        createdAt = date.getTime();
        updatedAt = date.getTime();
    }

    public static MessageGroup.Builder getBuilder() {
        return new MessageGroup.Builder();
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

    public String getGroupPhone() {
        return groupPhone;
    }

    public void setGroupPhone(String groupPhone) {
        this.groupPhone = groupPhone;
    }

    public String getPhoneNumberId() {
        return phoneNumberId;
    }

    public List<String> getMessagesSid() {
        return messagesSid;
    }

    public void setMessagesSid(List<String> messagesSid) {
        this.messagesSid = messagesSid;
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
        private String groupPhone;
        private String phoneNumberId;
        private List<String> messagesSid;
        private String userId;
        private long createdAt;
        private long updatedAt;

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

        public Builder groupPhone(String groupPhone) {
            this.groupPhone = groupPhone;
            return this;
        }

        public Builder phoneNumberId(String phoneNumberId) {
            this.phoneNumberId = phoneNumberId;
            return this;
        }

        public Builder messagesSid(List<String> messagesSid) {
            this.messagesSid = messagesSid;
            return this;
        }

        public Builder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public MessageGroup build() {
            MessageGroup build = new MessageGroup(this);
            return build;
        }
    }
}
