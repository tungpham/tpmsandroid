package com.android.morephone.domain.usecase.group;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.repository.group.GroupRepository;
import com.android.morephone.data.repository.group.source.GroupDataSource;
import com.android.morephone.domain.UseCase;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class CreateGroup extends UseCase<CreateGroup.RequestValues, CreateGroup.ResponseValue> {

    private final GroupRepository mMessageGroupRepository;

    public CreateGroup(@NonNull GroupRepository messageGroupRepository) {
        mMessageGroupRepository = messageGroupRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mMessageGroupRepository.saveMessageGroup(values.getMessageGroup(), new GroupDataSource.GetMessageGroupCallback() {
            @Override
            public void onMessageGroupLoaded(Group group) {
                getUseCaseCallback().onSuccess(new ResponseValue(group));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final Group mGroup;

        public RequestValues(Group group) {
            mGroup = group;
        }

        public Group getMessageGroup(){
            return mGroup;
        }
    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final Group mGroup;

        public ResponseValue(@NonNull Group group) {
            mGroup = group;
        }

        public Group getMessageGroups() {
            return mGroup;
        }
    }
}
