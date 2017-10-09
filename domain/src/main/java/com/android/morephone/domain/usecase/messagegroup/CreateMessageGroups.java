package com.android.morephone.domain.usecase.messagegroup;

import android.support.annotation.NonNull;

import com.android.morephone.data.entity.messagegroup.MessageGroup;
import com.android.morephone.data.repository.messagegroup.MessageGroupRepository;
import com.android.morephone.data.repository.messagegroup.source.MessageGroupDataSource;
import com.android.morephone.domain.UseCase;

import java.util.List;

/**
 * Created by truongnguyen on 10/4/17.
 */

public class CreateMessageGroups extends UseCase<CreateMessageGroups.RequestValues, CreateMessageGroups.ResponseValue> {

    private final MessageGroupRepository mMessageGroupRepository;

    public CreateMessageGroups(@NonNull MessageGroupRepository messageGroupRepository) {
        mMessageGroupRepository = messageGroupRepository;
    }

    @Override
    protected void executeUseCase(final RequestValues values) {

        mMessageGroupRepository.saveMessageGroup(values.getMessageGroup(), new MessageGroupDataSource.GetMessageGroupCallback() {
            @Override
            public void onMessageGroupLoaded(MessageGroup messageGroup) {
                getUseCaseCallback().onSuccess(new ResponseValue(messageGroup));
            }

            @Override
            public void onDataNotAvailable() {
                getUseCaseCallback().onError();
            }
        });
    }

    public static final class RequestValues implements UseCase.RequestValue {

        private final MessageGroup mMessageGroup;

        public RequestValues(MessageGroup messageGroup) {
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

        public MessageGroup getMessageGroups() {
            return mMessageGroup;
        }
    }
}
