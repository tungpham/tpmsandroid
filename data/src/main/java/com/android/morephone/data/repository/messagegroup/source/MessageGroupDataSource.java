package com.android.morephone.data.repository.messagegroup.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.repository.contact.source.ContactDataSource;

import java.util.List;

/**
 * Created by truongnguyen on 10/6/17.
 */

public interface MessageGroupDataSource {

    interface LoadMessageGroupsCallback {

        void onMessageGroupsLoaded(List<MessageGroup> messageGroups);

        void onDataNotAvailable();
    }

    interface GetMessageGroupCallback {

        void onMessageGroupLoaded(MessageGroup messageGroup);

        void onDataNotAvailable();
    }

    void getMessageGroups(@NonNull LoadMessageGroupsCallback callback);

    void getMessageGroups(@NonNull String phoneNumberId, @NonNull LoadMessageGroupsCallback callback);

    void getMessageGroup(@NonNull String messageGroupId, @NonNull GetMessageGroupCallback callback);

    void getMessageGroupByUserId(@NonNull String userId, @NonNull LoadMessageGroupsCallback callback);

    void saveMessageGroup(@NonNull MessageGroup messageGroup, @NonNull GetMessageGroupCallback callback);

    void updateMessageGroup(@NonNull MessageGroup messageGroup);

    void refreshMessageGroup();

    void deleteAllMessageGroup();

    void deleteMessageGroup(@NonNull String messageGroupId);

    void deleteAllMessageGroup(@NonNull String phoneNumberId);


}
