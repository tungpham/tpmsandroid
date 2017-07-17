
package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

/**
 * Listener associated with a request. Its {@link #onSuccess(Object)} method is called when the
 * result is ready and {@link #onError(int, Exception)} in case of any error.
 * Listener methods might be called either on a background thread or on the main application
 * thread. See {@link Billing} for more information.<p/>
 * <b>Note</b>: if a listener references an activity/context the associated request should
 * be cancelled through {@link Billing#cancel(int)} or {@link Billing#cancelAll()} methods when
 * the references activity/context is destroyed. Otherwise, the request will continue holding the
 * reference and the activity/context will leak.
 */
public interface RequestListener<R> {
    /**
     * Called when the request has finished successfully.
     *
     * @param result request result
     */
    void onSuccess(@Nonnull R result);

    /**
     * Called when the request has finished with an error (for example, exception was raised).
     *
     * @param response response code
     * @param e        raised exception
     */
    void onError(int response, @Nonnull Exception e);
}
