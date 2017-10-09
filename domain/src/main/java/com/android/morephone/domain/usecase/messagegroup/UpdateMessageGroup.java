package com.android.morephone.domain.usecase.messagegroup;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.repository.contact.ContactRepository;
import com.android.morephone.data.repository.messagegroup.MessageGroupRepository;
import com.android.morephone.domain.UseCase;

/**
 * Created by truongnguyen on 10/5/17.
 */

public class UpdateMessageGroup extends UseCase<UpdateMessageGroup.RequestValues, UpdateMessageGroup.ResponseValue> {

    private final MessageGroupRepository mMessageGroupRepository;

    public UpdateMessageGroup(@NonNull MessageGroupRepository messageGroupRepository) {
        mMessageGroupRepository = messageGroupRepository;
    }

    @Override
    protected void executeUseCase(final UpdateMessageGroup.RequestValues values) {
        mMessageGroupRepository.updateMessageGroup(values.getMessageGroup());
    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final MessageGroup mMessageGroup;

        public RequestValues( MessageGroup messageGroup) {
            mMessageGroup = messageGroup;
        }

        public MessageGroup getMessageGroup(){
            return mMessageGroup;
        }

    }

    public static final class ResponseValue implements UseCase.ResponseValue {

        private final MessageGroup mMessageGroup;

        public ResponseValue(@NonNull MessageGroup messageGroup) {
            mMessageGroup = messageGroup;
        }

        public MessageGroup getMessageGroup() {
            return mMessageGroup;
        }
    }
}
