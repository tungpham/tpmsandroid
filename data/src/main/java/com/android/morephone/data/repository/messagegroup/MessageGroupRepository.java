package com.android.morephone.data.repository.messagegroup;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.contact.source.ContactDataSource;
import com.android.morephone.data.repository.messagegroup.source.MessageGroupDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class MessageGroupRepository implements MessageGroupDataSource {

    private static MessageGroupRepository INSTANCE = null;

    private final MessageGroupDataSource mMessageGroupRemoteDataSource;

    private final MessageGroupDataSource mMessageGroupLocalDataSource;

    Map<String, MessageGroup> mCachedMessageGroups;

    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private MessageGroupRepository(@NonNull MessageGroupDataSource messageGroupRemoteDataSource,
                                   @NonNull MessageGroupDataSource messageGroupLocalDataSource) {
        mMessageGroupRemoteDataSource = messageGroupRemoteDataSource;
        mMessageGroupLocalDataSource = messageGroupLocalDataSource;
    }

    public static MessageGroupRepository getInstance(MessageGroupDataSource messageGroupRemoteDataSource,
                                                     MessageGroupDataSource messageGroupLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessageGroupRepository(messageGroupRemoteDataSource, messageGroupLocalDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

    @Override
    public void getMessageGroups(@NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void getMessageGroups(@NonNull final String phoneNumberId, @NonNull final LoadMessageGroupsCallback callback) {
        if (mCachedMessageGroups != null && !mCacheIsDirty) {
            callback.onMessageGroupsLoaded(new ArrayList<>(mCachedMessageGroups.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getContactsFromRemoteDataSource(phoneNumberId, callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mMessageGroupLocalDataSource.getMessageGroups(phoneNumberId, new LoadMessageGroupsCallback() {
                @Override
                public void onMessageGroupsLoaded(List<MessageGroup> messageGroups) {
                    refreshCache(messageGroups);
                    callback.onMessageGroupsLoaded(new ArrayList<>(mCachedMessageGroups.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getContactsFromRemoteDataSource(phoneNumberId, callback);
                }
            });
        }
    }

    @Override
    public void getMessageGroup(@NonNull String messageGroupId, @NonNull GetMessageGroupCallback callback) {

    }

    @Override
    public void getMessageGroupByUserId(@NonNull String userId, @NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void saveMessageGroup(@NonNull MessageGroup messageGroup, @NonNull GetMessageGroupCallback callback) {

    }

    @Override
    public void updateMessageGroup(@NonNull MessageGroup messageGroup) {

    }

    @Override
    public void refreshMessageGroup() {

    }

    @Override
    public void deleteAllMessageGroup() {

    }

    @Override
    public void deleteMessageGroup(@NonNull String messageGroupId) {

    }

    @Override
    public void deleteAllMessageGroup(@NonNull String phoneNumberId) {

    }

    private void getContactsFromRemoteDataSource(@NonNull final String phoneNumberId, @NonNull final LoadMessageGroupsCallback callback) {
        mMessageGroupRemoteDataSource.getMessageGroups(phoneNumberId, new LoadMessageGroupsCallback() {
            @Override
            public void onMessageGroupsLoaded(List<MessageGroup> messageGroups) {
                refreshCache(messageGroups);
                refreshLocalDataSource(phoneNumberId, messageGroups);
                callback.onMessageGroupsLoaded(new ArrayList<>(mCachedMessageGroups.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<MessageGroup> messageGroups) {
        if (mCachedMessageGroups == null) {
            mCachedMessageGroups = new LinkedHashMap<>();
        }
        mCachedMessageGroups.clear();
        for (MessageGroup messageGroup : messageGroups) {
            mCachedMessageGroups.put(messageGroup.getId(), messageGroup);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(String phoneNumberId, List<MessageGroup> messageGroups) {
        mMessageGroupLocalDataSource.deleteAllMessageGroup(phoneNumberId);
        for (MessageGroup messageGroup : messageGroups) {
            mMessageGroupLocalDataSource.saveMessageGroup(messageGroup, null);
        }
    }
}
