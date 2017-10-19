package com.android.morephone.data.repository.group;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.group.source.GroupDataSource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class GroupRepository implements GroupDataSource {

    private static GroupRepository INSTANCE = null;

    private final GroupDataSource mMessageGroupRemoteDataSource;

    private final GroupDataSource mMessageGroupLocalDataSource;

    Map<String, Group> mCachedMessageGroups;

    boolean mCacheIsDirty = false;

    // Prevent direct instantiation.
    private GroupRepository(@NonNull GroupDataSource messageGroupRemoteDataSource,
                            @NonNull GroupDataSource messageGroupLocalDataSource) {
        mMessageGroupRemoteDataSource = messageGroupRemoteDataSource;
        mMessageGroupLocalDataSource = messageGroupLocalDataSource;
    }

    public static GroupRepository getInstance(GroupDataSource messageGroupRemoteDataSource,
                                              GroupDataSource messageGroupLocalDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new GroupRepository(messageGroupRemoteDataSource, messageGroupLocalDataSource);
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
                public void onMessageGroupsLoaded(List<Group> groups) {
                    refreshCache(groups);
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
        Group cachedTask = getMessageGroupWithId(messageGroupId);

        // Respond immediately with cache if available
        if (cachedTask != null) {
            callback.onMessageGroupLoaded(cachedTask);
            return;
        }

        // Load from server/persisted if needed.

        DebugTool.logD("KQ: " + messageGroupId);
        mMessageGroupLocalDataSource.getMessageGroup(messageGroupId, new GetMessageGroupCallback() {
            @Override
            public void onMessageGroupLoaded(Group group) {
                callback.onMessageGroupLoaded(group);
            }

            @Override
            public void onDataNotAvailable() {
                mMessageGroupRemoteDataSource.getMessageGroup(messageGroupId, new GetMessageGroupCallback() {
                    @Override
                    public void onMessageGroupLoaded(Group group) {
                        callback.onMessageGroupLoaded(group);
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
                public void onMessageGroupsLoaded(List<Group> groups) {
                    refreshCache(groups);
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
    public void saveMessageGroup(@NonNull final Group group, @NonNull final GetMessageGroupCallback callback) {
        mMessageGroupRemoteDataSource.saveMessageGroup(group, new GetMessageGroupCallback() {
            @Override
            public void onMessageGroupLoaded(Group group) {
                mMessageGroupLocalDataSource.saveMessageGroup(group, null);

                // Do in memory cache update to keep the app UI up to date
                if (mCachedMessageGroups == null) {
                    mCachedMessageGroups = new LinkedHashMap<>();
                }
                mCachedMessageGroups.put(group.getId(), group);
                callback.onMessageGroupLoaded(group);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void updateMessageGroup(@NonNull Group group) {
        mMessageGroupRemoteDataSource.updateMessageGroup(group);
        mMessageGroupLocalDataSource.updateMessageGroup(group);

        // Do in memory cache update to keep the app UI up to date
        if (mCachedMessageGroups == null) {
            mCachedMessageGroups = new LinkedHashMap<>();
        }
        mCachedMessageGroups.put(group.getId(), group);
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
        Iterator<Map.Entry<String, Group>> it = mCachedMessageGroups.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Group> entry = it.next();
            if (entry.getValue().getPhoneNumberId().equals(phoneNumberId)) {
                it.remove();
            }
        }
    }

    @Nullable
    private Group getMessageGroupWithId(@NonNull String id) {
        if (mCachedMessageGroups == null || mCachedMessageGroups.isEmpty()) {
            return null;
        } else {
            return mCachedMessageGroups.get(id);
        }
    }

    private void getContactsFromRemoteDataSource(@NonNull final String phoneNumberId, @NonNull final LoadMessageGroupsCallback callback) {
        mMessageGroupRemoteDataSource.getMessageGroups(phoneNumberId, new LoadMessageGroupsCallback() {
            @Override
            public void onMessageGroupsLoaded(List<Group> groups) {
                refreshCache(groups);
                refreshLocalDataSource(phoneNumberId, groups);
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
            public void onMessageGroupsLoaded(List<Group> groups) {
                refreshCache(groups);
                refreshLocalDataSource(userId, groups);
                callback.onMessageGroupsLoaded(new ArrayList<>(mCachedMessageGroups.values()));
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    private void refreshCache(List<Group> groups) {
        if (mCachedMessageGroups == null) {
            mCachedMessageGroups = new LinkedHashMap<>();
        }
        mCachedMessageGroups.clear();
        for (Group group : groups) {
            mCachedMessageGroups.put(group.getId(), group);
        }
        mCacheIsDirty = false;
    }

    private void refreshLocalDataSource(String phoneNumberId, List<Group> groups) {
        mMessageGroupLocalDataSource.deleteAllMessageGroup(phoneNumberId);
        for (Group group : groups) {
            mMessageGroupLocalDataSource.saveMessageGroup(group, null);
        }
    }

    private void refreshLocalDataSourceByUserId(List<Group> groups) {
        mMessageGroupLocalDataSource.deleteAllMessageGroup();
        for (Group group : groups) {
            mMessageGroupLocalDataSource.saveMessageGroup(group, null);
        }
    }
}
