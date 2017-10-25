package com.android.morephone.data.repository.message;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.repository.message.source.MessageDataSource;

/**
 * Created by Ethan on 3/3/17.
 */

public class MessageRepository implements MessageDataSource {

    private static MessageRepository INSTANCE = null;

    private final MessageDataSource mMessageRemoteDataSource;

    private MessageRepository(@NonNull MessageDataSource messageRemoteDataSource) {
        mMessageRemoteDataSource = messageRemoteDataSource;
    }

    public static MessageRepository getInstance(MessageDataSource messageRemoteDataSource) {
        if (INSTANCE == null) {
            INSTANCE = new MessageRepository(messageRemoteDataSource);
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }


    @Override
    public void getMessages(@NonNull LoadMessagesCallback callback) {
        mMessageRemoteDataSource.getMessages(callback);
    }

    @Override
    public void getMessages(String to, String from, @NonNull LoadMessagesCallback callback) {
        mMessageRemoteDataSource.getMessages(to, from, callback);
    }

    @Override
    public void getMessagesIncoming(String phoneNumberIncoming, @NonNull LoadMessagesCallback callback) {
        mMessageRemoteDataSource.getMessagesIncoming(phoneNumberIncoming, callback);
    }

    @Override
    public MessageListResourceResponse getMessagesIncoming(String phoneNumberIncoming) {
        return mMessageRemoteDataSource.getMessagesIncoming(phoneNumberIncoming);
    }

    @Override
    public void getMessagesOutgoing(String phoneNumberOutgoing, @NonNull LoadMessagesCallback callback) {
        mMessageRemoteDataSource.getMessagesOutgoing(phoneNumberOutgoing, callback);
    }

    @Override
    public MessageListResourceResponse getMessagesOutgoing(String phoneNumberOutgoing) {
        return mMessageRemoteDataSource.getMessagesOutgoing(phoneNumberOutgoing);
    }

    @Override
    public void getMessage(String messageSid, @NonNull GetMessageCallback callback) {

    }

    @Override
    public void createMessage(String userId, String groupId, long dateSent, String to, String from, String body, @NonNull LoadMessagesCallback callback) {
        mMessageRemoteDataSource.createMessage(userId, groupId, dateSent, to, from, body, callback);
    }


    @Override
    public void deleteMessage(String messageSid) {
        mMessageRemoteDataSource.deleteMessage(messageSid);
    }

    @Override
    public void modifyMessage(String messageSid, @NonNull GetMessageCallback callback) {

    }

    @Override
    public void deleteMedia(String messageSid, String mediaSid, @NonNull ResultCallback callback) {

    }

    @Override
    public void getMedia(String messageSid, String mediaSid, @NonNull ResultCallback callback) {

    }

    @Override
    public void getMedias(String messageSid, @NonNull ResultCallback callback) {

    }

    @Override
    public void createFeedback(String messageSid, @NonNull ResultCallback callback) {

    }
}
