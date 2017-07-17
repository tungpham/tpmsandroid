
package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

class RequestListenerWrapper<R> implements CancellableRequestListener<R> {

    @Nonnull
    protected final RequestListener<R> mListener;

    RequestListenerWrapper(@Nonnull RequestListener<R> listener) {
        mListener = listener;
    }

    @Override
    public void onSuccess(@Nonnull R result) {
        mListener.onSuccess(result);
    }

    @Override
    public void onError(int response, @Nonnull Exception e) {
        mListener.onError(response, e);
    }

    public final void cancel() {
        onCancel();
        Billing.cancel(mListener);
    }

    protected void onCancel() {
    }
}
