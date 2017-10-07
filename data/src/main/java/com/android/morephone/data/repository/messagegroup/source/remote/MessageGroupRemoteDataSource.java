package com.android.morephone.data.repository.messagegroup.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.repository.contact.source.remote.ContactRemoteDataSource;
import com.android.morephone.data.repository.messagegroup.source.MessageGroupDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 10/7/17.
 */

public class MessageGroupRemoteDataSource implements MessageGroupDataSource {


    private static MessageGroupRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Contact> TASKS_SERVICE_DATA;

    private Context mContext;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
    }

    public static MessageGroupRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new MessageGroupRemoteDataSource(context);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private MessageGroupRemoteDataSource(Context context) {
        mContext = context;
    }


    @Override
    public void getMessageGroups(@NonNull LoadMessageGroupsCallback callback) {

    }

    @Override
    public void getMessageGroups(@NonNull String phoneNumberId, @NonNull final LoadMessageGroupsCallback callback) {

    }

    @Override
    public void getMessageGroup(@NonNull String messageGroupId, @NonNull GetMessageGroupCallback callback) {

    }

    @Override
    public void getMessageGroupByUserId(@NonNull String userId, @NonNull final LoadMessageGroupsCallback callback) {
        ApiMorePhone.loadMessageGroupByUser(mContext, userId, new Callback<BaseResponse<List<MessageGroup>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<MessageGroup>>> call, Response<BaseResponse<List<MessageGroup>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMessageGroupsLoaded(response.body().getResponse());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<MessageGroup>>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveMessageGroup(@NonNull MessageGroup messageGroup, @NonNull final GetMessageGroupCallback callback) {
        ApiMorePhone.createMessageGroup(mContext, messageGroup, new Callback<BaseResponse<MessageGroup>>() {
            @Override
            public void onResponse(Call<BaseResponse<MessageGroup>> call, Response<BaseResponse<MessageGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMessageGroupLoaded(response.body().getResponse());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<MessageGroup>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void updateMessageGroup(@NonNull MessageGroup messageGroup) {
        ApiMorePhone.updateMessageGroup(mContext, messageGroup, new Callback<BaseResponse<MessageGroup>>() {
            @Override
            public void onResponse(Call<BaseResponse<MessageGroup>> call, Response<BaseResponse<MessageGroup>> response) {
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<MessageGroup>> call, Throwable t) {

            }
        });
    }

    @Override
    public void refreshMessageGroup() {

    }

    @Override
    public void deleteAllMessageGroup() {

    }

    @Override
    public void deleteMessageGroup(@NonNull String messageGroupId) {
        ApiMorePhone.deleteMessageGroup(mContext, messageGroupId, new Callback<com.android.morephone.data.entity.Response>() {
            @Override
            public void onResponse(Call<com.android.morephone.data.entity.Response> call, Response<com.android.morephone.data.entity.Response> response) {

            }

            @Override
            public void onFailure(Call<com.android.morephone.data.entity.Response> call, Throwable t) {

            }
        });
    }

    @Override
    public void deleteAllMessageGroup(@NonNull String phoneNumberId) {
    }
}
