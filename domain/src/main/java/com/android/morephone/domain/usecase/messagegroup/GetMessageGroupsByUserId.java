package com.android.morephone.domain.usecase.messagegroup;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.repository.contact.source.ContactDataSource;
import com.android.morephone.data.repository.messagegroup.MessageGroupRepository;
import com.android.morephone.data.repository.messagegroup.source.MessageGroupDataSource;
import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.usecase.contact.ContactFilter;
import com.android.morephone.domain.usecase.contact.ContactFilterType;

import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class GetMessageGroupsByUserId extends UseCase<GetMessageGroupsByUserId.RequestValues, GetMessageGroupsByUserId.ResponseValue> {

    private final MessageGroupRepository mMessageGroupRepository;

    public GetMessageGroupsByUserId(@NonNull MessageGroupRepository messageGroupRepository) {
        mMessageGroupRepository = messageGroupRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {
        if (values.isForceUpdate()) {
            mMessageGroupRepository.refreshMessageGroup();
        }

        mMessageGroupRepository.getMessageGroupByUserId(values.getUserId(), new MessageGroupDataSource.LoadMessageGroupsCallback() {

            @Override
            public void onMessageGroupsLoaded(List<MessageGroup> messageGroups) {
                getUseCaseCallback().onSuccess(new ResponseValue(messageGroups));
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

        private final List<MessageGroup> mMessageGroup;

        public ResponseValue(@NonNull List<MessageGroup> messageGroups) {
            mMessageGroup = messageGroups;
        }

        public List<MessageGroup> getMessageGroups() {
            return mMessageGroup;
        }
    }
}
