package com.android.morephone.data.repository.group.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.group.Group;

import java.util.List;

/**
 * Created by truongnguyen on 10/6/17.
 */

public interface GroupDataSource {

    interface LoadMessageGroupsCallback {

        void onMessageGroupsLoaded(List<Group> groups);

        void onDataNotAvailable();
    }

    interface GetMessageGroupCallback {

        void onMessageGroupLoaded(Group group);

        void onDataNotAvailable();
    }

    void getMessageGroups(@NonNull LoadMessageGroupsCallback callback);

    void getMessageGroups(@NonNull String phoneNumberId, @NonNull LoadMessageGroupsCallback callback);

    void getMessageGroup(@NonNull String messageGroupId, @NonNull GetMessageGroupCallback callback);

    void getMessageGroupByUserId(@NonNull String userId, @NonNull LoadMessageGroupsCallback callback);

    void saveMessageGroup(@NonNull Group group, @NonNull GetMessageGroupCallback callback);

    void updateMessageGroup(@NonNull Group group);

    void refreshMessageGroup();

    void deleteAllMessageGroup();

    void deleteMessageGroup(@NonNull String messageGroupId);

    void deleteAllMessageGroup(@NonNull String phoneNumberId);


}
