package com.android.morephone.data.repository.group.source.remote;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.network.ApiMorePhone;
import com.android.morephone.data.repository.group.source.GroupDataSource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 10/7/17.
 */

public class GroupRemoteDataSource implements GroupDataSource {


    private static GroupRemoteDataSource INSTANCE;

    private static final int SERVICE_LATENCY_IN_MILLIS = 5000;

    private static final Map<String, Contact> TASKS_SERVICE_DATA;

    private Context mContext;

    static {
        TASKS_SERVICE_DATA = new LinkedHashMap<>(2);
    }

    public static GroupRemoteDataSource getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new GroupRemoteDataSource(context);
        }
        return INSTANCE;
    }

    // Prevent direct instantiation.
    private GroupRemoteDataSource(Context context) {
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
        ApiMorePhone.loadGroupByUser(mContext, userId, new Callback<BaseResponse<List<Group>>>() {
            @Override
            public void onResponse(Call<BaseResponse<List<Group>>> call, Response<BaseResponse<List<Group>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMessageGroupsLoaded(response.body().getResponse());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<List<Group>>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void saveMessageGroup(@NonNull Group group, @NonNull final GetMessageGroupCallback callback) {
        ApiMorePhone.createGroup(mContext, group, new Callback<BaseResponse<Group>>() {
            @Override
            public void onResponse(Call<BaseResponse<Group>> call, Response<BaseResponse<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onMessageGroupLoaded(response.body().getResponse());
                } else {
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Group>> call, Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void updateMessageGroup(@NonNull Group group) {
        ApiMorePhone.updateGroup(mContext, group, new Callback<BaseResponse<Group>>() {
            @Override
            public void onResponse(Call<BaseResponse<Group>> call, Response<BaseResponse<Group>> response) {
                if (response.isSuccessful() && response.body() != null) {

                }
            }

            @Override
            public void onFailure(Call<BaseResponse<Group>> call, Throwable t) {

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
        ApiMorePhone.deleteGroup(mContext, messageGroupId, new Callback<com.android.morephone.data.entity.Response>() {
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
