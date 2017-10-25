package com.android.morephone.data.repository.message.source;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;

import java.util.List;

/**
 * Created by Ethan on 3/3/17.
 */

public interface MessageDataSource {

    interface LoadMessagesCallback {
        void onMessagesLoaded(List<MessageItem> messageItems, int statusCode);

        void onDataNotAvailable();
    }

    interface GetMessageCallback {
        void onMessageLoaded(MessageItem messageItem, int statusCode);

        void onDataNotAvailable();
    }

    interface ResultCallback {
        void onResult(boolean isResult);
    }

    void getMessages(@NonNull LoadMessagesCallback callback);

    void getMessages(String phoneNumberIncoming, String phoneNumberOutgoing, @NonNull LoadMessagesCallback callback);

    void getMessagesIncoming(String phoneNumberIncoming, @NonNull LoadMessagesCallback callback);

    MessageListResourceResponse getMessagesIncoming(String phoneNumberIncoming);

    void getMessagesOutgoing(String phoneNumberOutgoing, @NonNull LoadMessagesCallback callback);

    MessageListResourceResponse getMessagesOutgoing(String phoneNumberOutgoing);

    void getMessage(String messageSid, @NonNull GetMessageCallback callback);

    void createMessage(String userId, String groupId, long dateSent, String to, String from, String body, @NonNull LoadMessagesCallback callback);

    void deleteMessage(String messageSid);

    void modifyMessage(String messageSid, @NonNull GetMessageCallback callback);

    void deleteMedia(String messageSid, String mediaSid, @NonNull ResultCallback callback);

    void getMedia(String messageSid, String mediaSid, @NonNull ResultCallback callback);

    void getMedias(String messageSid, @NonNull ResultCallback callback);

    void createFeedback(String messageSid, @NonNull ResultCallback callback);


}
