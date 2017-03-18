package com.ethan.morephone.model;

import com.android.morephone.data.entity.MessageItem;

import java.util.List;

/**
 * Created by Ethan on 3/18/17.
 */

public class ConversationModel {

    private String mPhoneNumber;
    private List<MessageItem> mMessageItems;

    public ConversationModel(String phoneNumber, List<MessageItem> messageItems) {
        this.mPhoneNumber = phoneNumber;
        this.mMessageItems = messageItems;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    public List<MessageItem> getMessageItems() {
        return mMessageItems;
    }

    public void setMessageItems(List<MessageItem> messageItems) {
        this.mMessageItems = messageItems;
    }
}
