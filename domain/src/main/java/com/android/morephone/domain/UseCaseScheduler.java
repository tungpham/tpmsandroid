package com.android.morephone.domain;

/**
 * Created by AnPEthan on 9/6/2016.
 */

public interface UseCaseScheduler {

    void execute(Runnable runnable);

    <V extends UseCase.ResponseValue> void notifyResponse(final V response,
                                                          final UseCase.UseCaseCallback<V> callback);

    <V extends UseCase.ResponseValue> void onError(final UseCase.UseCaseCallback<V> callback);
}
