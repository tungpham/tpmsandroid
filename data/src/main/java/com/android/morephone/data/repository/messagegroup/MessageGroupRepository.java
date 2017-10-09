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
    public void getMessageGroup(@NonNull final String messageGroupId, @NonNull final GetMessageGroupCallback callback) {
        MessageGroup cachedTask = getMessageGroupWithId(messageGroupId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onMessageGroupLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        DebugTool.logD("KQ: " + messageGroupId);
        mMessageGroupLocalDataSource.getMessageGroup(messageGroupId, new GetMessageGroupCallback() {
            @Override
            public void onMessageGroupLoaded(MessageGroup messageGroup) {
                callback.onMessageGroupLoaded(messageGroup);
            }

            @Override
            public void onDataNotAvailable() {
                mMessageGroupRemoteDataSource.getMessageGroup(messageGroupId, new GetMessageGroupCallback() {
                    @Override
                    public void onMessageGroupLoaded(MessageGroup messageGroup) {
                        callback.onMessageGroupLoaded(messageGroup);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });

    }

    @Override
    public void getMessageGroupByUserId(@NonNull final String userId, @NonNull final LoadMessageGroupsCallback callback) {
        if (mCachedMessageGroups != null && !mCacheIsDirty) {
            callback.onMessageGroupsLoaded(new ArrayList<>(mCachedMessageGroups.values()));
            return;
        }

        if (mCacheIsDirty) {
            // If the cache is dirty we need to fetch new data from the network.
            getMessageGroupByUserIdFromRemoteDataSource(userId, callback);
        } else {
            // Query the local storage if available. If not, query the network.
            mMessageGroupLocalDataSource.getMessageGroupByUserId(userId, new LoadMessageGroupsCallback() {
                @Override
                public void onMessageGroupsLoaded(List<MessageGroup> messageGroups) {
                    refreshCache(messageGroups);
                    callback.onMessageGroupsLoaded(new ArrayList<>(mCachedMessageGroups.values()));
                }

                @Override
                public void onDataNotAvailable() {
                    getMessageGroupByUserIdFromRemoteDataSource(userId, callback);
                }
            });
        }
    }

    @Override
    public void saveMessageGroup(@NonNull final MessageGroup messageGroup, @NonNull final GetMessageGroupCallback callback) {
        mMessageGroupRemoteDataSource.saveMessageGroup(messageGroup, new GetMessageGroupCallback() {
            @Override
            public void onMessageGroupLoaded(MessageGroup messageGroup) {
                mMessageGroupLocalDataSource.saveMessageGroup(messageGroup, null);

                // Do in memory cache update to keep the app UI up to date
                if (mCachedMessageGroups == null) {
                    mCachedMessageGroups = new LinkedHashMap<>();
                }
                mCachedMessageGroups.put(messageGroup.getId(), messageGroup);
                callback.onMessageGroupLoaded(messageGroup);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void updateMessageGroup(@NonNull MessageGroup messageGroup) {
        mMessageGroupRemoteDataSource.updateMessageGroup(messageGroup);
        mMessageGroupLocalDataSource.updateMessageGroup(messageGroup);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedMessageGroups == null) {
            mCachedMessageGroups = new LinkedHashMap<>();
        }
        mCachedMessageGroups.put(messageGroup.getId(), messageGroup);
    }

    @Override
    public void refreshMessageGroup() {
        mCacheIsDirty = true;
    }

    @Override
    public void deleteAllMessageGroup() {

    }

    @Override
    public void deleteMessageGroup(@NonNull String messageGroupId) {
        mMessageGroupLocalDataSource.deleteMessageGroup(messageGroupId);
        mMessageGroupRemoteDataSource.deleteMessageGroup(messageGroupId);

        if (mCachedMessageGroups == null) {
            mCachedMessageGroups = new LinkedHashMap<>();
        }
        mCachedMessageGroups.remove(messageGroupId);
    }

    @Override
    public void deleteAllMessageGroup(@NonNull String phoneNumberId) {
        mMessageGroupLocalDataSource.deleteAllMessageGroup(phoneNumberId);
        mMessageGroupRemoteDataSource.deleteAllMessageGroup(phoneNumberId);

        if (mCachedMessageGroups == null) {
            mCachedMessageGroups = new LinkedHashMap<>();
        }
        Iterator<Map.Entry<String, MessageGroup>> it = mCachedMessageGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, MessageGroup> entry = it.next();
            if (entry.getValue().getPhoneNumberId().equals(phoneNumberId)) {
                it.remove();
            }
        }
    }

    @Nullable
    private MessageGroup getMessageGroupWithId(@NonNull String id) {
        if (mCachedMessageGroups == null || mCachedMessageGroups.isEmpty()) {
            return null;
        } else {
            return mCachedMessageGroups.get(id);
        }
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

    private void getMessageGroupByUserIdFromRemoteDataSource(@NonNull final String userId, @NonNull final LoadMessageGroupsCallback callback) {
        mMessageGroupRemoteDataSource.getMessageGroupByUserId(userId, new LoadMessageGroupsCallback() {
            @Override
            public void onMessageGroupsLoaded(List<MessageGroup> messageGroups) {
                refreshCache(messageGroups);
                refreshLocalDataSource(userId, messageGroups);
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

    private void refreshLocalDataSourceByUserId(List<MessageGroup> messageGroups) {
        mMessageGroupLocalDataSource.deleteAllMessageGroup();
        for (MessageGroup messageGroup : messageGroups) {
            mMessageGroupLocalDataSource.saveMessageGroup(messageGroup, null);
        }
    }
}
