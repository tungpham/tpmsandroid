package com.android.morephone.domain.usecase.group;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.repository.group.GroupRepository;
import com.android.morephone.data.repository.group.source.GroupDataSource;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class GetGroupsByUserId extends UseCase<GetGroupsByUserId.RequestValues, GetGroupsByUserId.ResponseValue> {

    private final GroupRepository mMessageGroupRepository;

    public GetGroupsByUserId(@NonNull GroupRepository messageGroupRepository) {
        mMessageGroupRepository = messageGroupRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            mMessageGroupRepository.refreshMessageGroup();
        }

        mMessageGroupRepository.getMessageGroupByUserId(values.getUserId(), new GroupDataSource.LoadMessageGroupsCallback() {

            @Override
            public void onMessageGroupsLoaded(List<Group> groups) {
                getUseCaseCallback().onSuccess(new ResponseValue(groups));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }

        });

    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final boolean mForceUpdate;
        private final String mUserId;

        public RequestValues(boolean forceUpdate, String userId) {
            mForceUpdate = forceUpdate;
            mUserId = userId;
        }

        public boolean isForceUpdate() {
            return mForceUpdate;
        }

        public String getUserId() {
            return mUserId;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final List<Group> mGroup;

        public ResponseValue(@NonNull List<Group> groups) {
            mGroup = groups;
        }

        public List<Group> getMessageGroups() {
            return mGroup;
        }
    }
}
