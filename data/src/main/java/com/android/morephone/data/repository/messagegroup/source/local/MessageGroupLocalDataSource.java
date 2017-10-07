package com.android.morephone.data.repository.messagegroup.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.database.MessageGroupDatabaseHelper;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.repository.contact.source.local.ContactLocalDataSource;
import com.android.morephone.data.repository.messagegroup.source.MessageGroupDataSource;

import java.util.List;

/**
 * Created by truongnguyen on 10/7/17.
 */

public class MessageGroupLocalDataSource implements MessageGroupDataSource {

    private static MessageGroupLocalDataSource INSTANCE;
    private Context mContext;


    // Prevent direct instantiation.
    private MessageGroupLocalDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static MessageGroupLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MessageGroupLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMessageGroups(@NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void getMessageGroups(@NonNull String phoneNumberId, @NonNull LoadMessageGroupsCallback callback) {
        List<MessageGroup> messageGroup = MessageGroupDatabaseHelper.findAll(mContext, phoneNumberId);
        if (messageGroup != null && !messageGroup.isEmpty()) {
            callback.onMessageGroupsLoaded(messageGroup);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMessageGroup(@NonNull String messageGroupId, @NonNull GetMessageGroupCallback callback) {
        MessageGroup messageGroup = MessageGroupDatabaseHelper.findMessageGroup(mContext, messageGroupId);
        if (messageGroup != null) {
            callback.onMessageGroupLoaded(messageGroup);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMessageGroupByUserId(@NonNull String userId, @NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void saveMessageGroup(@NonNull MessageGroup messageGroup, @NonNull GetMessageGroupCallback callback) {
        MessageGroupDatabaseHelper.insert(mContext, messageGroup);
    }

    @Override
    public void updateMessageGroup(@NonNull MessageGroup messageGroup) {
        MessageGroupDatabaseHelper.updateMessageGroup(mContext, messageGroup);
    }

    @Override
    public void refreshMessageGroup() {

    }

    @Override
    public void deleteAllMessageGroup() {
        MessageGroupDatabaseHelper.deleteAllMessageGroup(mContext);
    }

    @Override
    public void deleteMessageGroup(@NonNull String messageGroupId) {
        MessageGroupDatabaseHelper.deleteMessageGroup(mContext, messageGroupId);
    }

    @Override
    public void deleteAllMessageGroup(@NonNull String phoneNumberId) {
        MessageGroupDatabaseHelper.deleteAllMessageGroup(mContext, phoneNumberId);
    }
}
