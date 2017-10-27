package com.android.morephone.data.entity.conversation;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.utils.DateUtils;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * Created by Ethan on 3/18/17.
 */

public class ConversationModel implements Comparable<ConversationModel>, Parcelable {
    @SerializedName("group_id")
    public String mGroupId;

    @SerializedName("phone_number")
    public String mPhoneNumber;

    @SerializedName("date_created")
    public String mDateCreated;

    @SerializedName("message_items")
    public List<MessageItem> mMessageItems;

    public ConversationModel(String phoneNumber, String dateCreated, List<MessageItem> messageItems) {
        this.mPhoneNumber = phoneNumber;
        this.mDateCreated = dateCreated;
        this.mMessageItems = messageItems;
    }

    public ConversationModel(String groupId, String phoneNumber, String dateCreated, List<MessageItem> messageItems) {
        this.mGroupId = groupId;
        this.mPhoneNumber = phoneNumber;
        this.mDateCreated = dateCreated;
        this.mMessageItems = messageItems;
    }

    protected ConversationModel(Parcel in) {
        mGroupId = in.readString();
        mPhoneNumber = in.readString();
        mDateCreated = in.readString();
        mMessageItems = in.createTypedArrayList(MessageItem.CREATOR);
    }

    public static final Creator<ConversationModel> CREATOR = new Creator<ConversationModel>() {
        @Override
        public ConversationModel createFromParcel(Parcel in) {
            return new ConversationModel(in);
        }

        @Override
        public ConversationModel[] newArray(int size) {
            return new ConversationModel[size];
        }
    };

    @Override
    public int compareTo(ConversationModel conversationModel) {
        if (!TextUtils.isEmpty(mDateCreated) && !TextUtils.isEmpty(conversationModel.mDateCreated)) {

            Date current = DateUtils.getDate(mDateCreated);
            Date now = DateUtils.getDate(conversationModel.mDateCreated);
            if (current != null && now != null) {
                if (current.after(now)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        return 0;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mGroupId);
        parcel.writeString(mPhoneNumber);
        parcel.writeString(mDateCreated);
        parcel.writeTypedList(mMessageItems);
    }
}
