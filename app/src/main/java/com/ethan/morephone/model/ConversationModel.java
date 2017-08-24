//package com.ethan.morephone.model;
//
//import android.text.TextUtils;
//
//import com.android.morephone.data.entity.MessageItem;
//import com.android.morephone.data.log.DebugTool;
//import com.android.morephone.data.utils.DateUtils;
//
//import java.util.Date;
//import java.util.List;
//
///**
// * Created by Ethan on 3/18/17.
// */
//
//public class ConversationModel implements Comparable<ConversationModel> {
//
//    private String mPhoneNumber;
//    private String mDateCreated;
//    private List<MessageItem> mMessageItems;
//
//    public ConversationModel(String phoneNumber, String dateCreated, List<MessageItem> messageItems) {
//        this.mPhoneNumber = phoneNumber;
//        this.mDateCreated = dateCreated;
//        this.mMessageItems = messageItems;
//
//    }
//
//    public String getPhoneNumber() {
//        return mPhoneNumber;
//    }
//
//    public void setPhoneNumber(String phoneNumber) {
//        this.mPhoneNumber = phoneNumber;
//    }
//
//    public List<MessageItem> getMessageItems() {
//        return mMessageItems;
//    }
//
//    public void setMessageItems(List<MessageItem> messageItems) {
//        this.mMessageItems = messageItems;
//    }
//
//    public String getDateCreated() {
//        return mDateCreated;
//    }
//
//    public void setDateCreated(String mDateCreated) {
//        this.mDateCreated = mDateCreated;
//    }
//
//    @Override
//    public int compareTo(ConversationModel conversationModel) {
//        if (!TextUtils.isEmpty(mDateCreated) && !TextUtils.isEmpty(conversationModel.getDateCreated())) {
//
//            Date current = DateUtils.getDate(mDateCreated);
//            Date now = DateUtils.getDate(conversationModel.getDateCreated());
//            DebugTool.logD("CURRENT: " + mDateCreated);
//            if (current.after(now)) {
//                return -1;
//            } else {
//                return 1;
//            }
//        }
//
//        return 0;
//
//    }
//}
