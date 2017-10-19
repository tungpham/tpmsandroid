package com.android.morephone.domain.usecase.group;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.repository.group.GroupRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by truongnguyen on 10/5/17.
 */

public class UpdateGroup extends UseCase<UpdateGroup.RequestValues, UpdateGroup.ResponseValue> {

    private final GroupRepository mMessageGroupRepository;

    public UpdateGroup(@NonNull GroupRepository messageGroupRepository) {
        mMessageGroupRepository = messageGroupRepository;
    }

    @Override
    protected void executeUseCase(final UpdateGroup.RequestValues values) {
        mMessageGroupRepository.updateMessageGroup(values.getMessageGroup());
    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final Group mGroup;

        public RequestValues( Group group) {
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

        public Group getMessageGroup() {
            return mGroup;
        }
    }
}
