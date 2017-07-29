package com.ethan.morephone.presentation.message.compose;

import android.support.annotation.NonNull;

import com.android.morephone.domain.UseCase;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.message.CreateMessage;

/**
 * Created by Ethan on 4/5/17.
 */

public class ComposePresenter implements ComposeContract.Presenter {

    private final ComposeContract.View mView;

    private final UseCaseHandler mUseCaseHandler;
    private final CreateMessage mCreateMessage;

    public ComposePresenter(@NonNull ComposeContract.View view,
                            @NonNull UseCaseHandler useCaseHandler,
                            @NonNull CreateMessage createMessage) {
        mView = view;
        mUseCaseHandler = useCaseHandler;
        mCreateMessage = createMessage;

        mView.setPresenter(this);
    }

    @Override
    public void start() {

    }

    @Override
    public void createMessage(String userId, String to, String from, String body, int position) {
        mView.showLoading(true);
        CreateMessage.RequestValue requestValue = new CreateMessage.RequestValue(userId, to, from, body);
        mUseCaseHandler.execute(mCreateMessage, requestValue, new UseCase.UseCaseCallback<CreateMessage.ResponseValue>() {
            @Override
            public void onSuccess(CreateMessage.ResponseValue response) {
//                mView.showProgress(false, position);
                mView.showLoading(false);
                mView.createMessageSuccess(response.getMessageItem());
            }

            @Override
            public void onError() {
//                mView.showProgress(false, position);
                mView.showLoading(false);
                mView.createMessageError();
            }
        });
    }
}
