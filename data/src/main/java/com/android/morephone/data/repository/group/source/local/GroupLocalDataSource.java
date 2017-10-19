package com.android.morephone.data.repository.group.source.local;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.database.MessageGroupDatabaseHelper;
import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.repository.group.source.GroupDataSource;

import java.util.List;

/**
 * Created by truongnguyen on 10/7/17.
 */

public class GroupLocalDataSource implements GroupDataSource {

    private static GroupLocalDataSource INSTANCE;
    private Context mContext;


    // Prevent direct instantiation.
    private GroupLocalDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static GroupLocalDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GroupLocalDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMessageGroups(@NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void getMessageGroups(@NonNull String phoneNumberId, @NonNull LoadMessageGroupsCallback callback) {
        List<Group> group = MessageGroupDatabaseHelper.findAll(mContext, phoneNumberId);
        if (group != null && !group.isEmpty()) {
            callback.onMessageGroupsLoaded(group);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMessageGroup(@NonNull String messageGroupId, @NonNull GetMessageGroupCallback callback) {
        Group group = MessageGroupDatabaseHelper.findMessageGroup(mContext, messageGroupId);
        if (group != null) {
            callback.onMessageGroupLoaded(group);
        } else {
            callback.onDataNotAvailable();
        }
    }

    @Override
    public void getMessageGroupByUserId(@NonNull String userId, @NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void saveMessageGroup(@NonNull Group group, @NonNull GetMessageGroupCallback callback) {
        MessageGroupDatabaseHelper.insert(mContext, group);
    }

    @Override
    public void updateMessageGroup(@NonNull Group group) {
        MessageGroupDatabaseHelper.updateMessageGroup(mContext, group);
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
