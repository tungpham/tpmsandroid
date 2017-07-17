

package com.ethan.morephone.presentation.buy.payment.checkout;

/**
 * Request listener which can be cancelled
 *
 * @param <R> type of result
 */
interface CancellableRequestListener<R> extends RequestListener<R> {

    /**
     * Should be called to notify the listener that associates request was cancelled
     */
    void cancel();
}
