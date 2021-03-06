package com.android.morephone.data.repository.message.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.twilio.MessageListResourceResponse;
import com.android.morephone.data.network.ApiManager;
import com.android.morephone.data.repository.message.source.MessageDataSource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Ethan on 3/3/17.
 */

public class MessageRemoteDataSource implements MessageDataSource {

    private static MessageRemoteDataSource INSTANCE;

    private Context mContext;

    private MessageRemoteDataSource(@NonNull Context context) {
        mContext = context;
    }

    public static MessageRemoteDataSource getInstance(@NonNull Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MessageRemoteDataSource(context);
        }
        return INSTANCE;
    }

    @Override
    public void getMessages(@NonNull final LoadMessagesCallback callback) {
        ApiManager.getAllMessages(mContext, new Callback<MessageListResourceResponse>() {
            @Override
            public void onResponse(Call<MessageListResourceResponse> call, Response<MessageListResourceResponse> response) {
                if (response.isSuccessful()) {
                    MessageListResourceResponse messageListResourceResponse = response.body();
                    if (messageListResourceResponse != null && messageListResourceResponse.messages != null && !messageListResourceResponse.messages.isEmpty()) {
                        callback.onMessagesLoaded(messageListResourceResponse.messages);

                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }

            }

            @Override
            public void onFailure(Call<MessageListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMessages(String to, String from, @NonNull final LoadMessagesCallback callback) {
        ApiManager.getMessages(mContext, to, from, new Callback<MessageListResourceResponse>() {
            @Override
            public void onResponse(Call<MessageListResourceResponse> call, Response<MessageListResourceResponse> response) {
                if (response.isSuccessful()) {
                    MessageListResourceResponse messageListResourceResponse = response.body();
                    if (messageListResourceResponse != null && messageListResourceResponse.messages != null && !messageListResourceResponse.messages.isEmpty()) {
                        callback.onMessagesLoaded(messageListResourceResponse.messages);

                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<MessageListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMessagesIncoming(String phoneNumberIncoming, @NonNull final LoadMessagesCallback callback) {
        ApiManager.getMessagesIncoming(mContext, phoneNumberIncoming, new Callback<MessageListResourceResponse>() {
            @Override
            public void onResponse(Call<MessageListResourceResponse> call, Response<MessageListResourceResponse> response) {
                if (response.isSuccessful()) {
                    MessageListResourceResponse messageListResourceResponse = response.body();
                    if (messageListResourceResponse != null && messageListResourceResponse.messages != null && !messageListResourceResponse.messages.isEmpty()) {
                        callback.onMessagesLoaded(messageListResourceResponse.messages);

                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<MessageListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMessagesOutgoing(String phoneNumberOutgoing, @NonNull final LoadMessagesCallback callback) {
        ApiManager.getMessagesOutgoing(mContext, phoneNumberOutgoing, new Callback<MessageListResourceResponse>() {
            @Override
            public void onResponse(Call<MessageListResourceResponse> call, Response<MessageListResourceResponse> response) {
                if (response.isSuccessful()) {
                    MessageListResourceResponse messageListResourceResponse = response.body();
                    if (messageListResourceResponse != null && messageListResourceResponse.messages != null && !messageListResourceResponse.messages.isEmpty()) {
                        callback.onMessagesLoaded(messageListResourceResponse.messages);

                    } else {
                        callback.onDataNotAvailable();
                    }
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<MessageListResourceResponse> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getMessage(String messageSid, @NonNull GetMessageCallback callback) {

    }

    @Override
    public void createMessage(String to, String from, String body, @NonNull final GetMessageCallback callback) {
        ApiManager.createMessage(mContext, to, from, body, new Callback<MessageItem>() {
            @Override
            public void onResponse(Call<MessageItem> call, Response<MessageItem> response) {
                if (response.isSuccessful()) {
                    callback.onMessageLoaded(response.body());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<MessageItem> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void deleteMessage(String messageSid) {
        ApiManager.deleteMessage(mContext, messageSid);
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
