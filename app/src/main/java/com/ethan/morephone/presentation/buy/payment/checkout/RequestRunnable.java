
package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nullable;

/**
 * Runnable which executes a billing request.
 */
interface RequestRunnable {

    /**
     * @return associated request id
     */
    int getId();

    /**
     * @return associated request tag
     */
    @Nullable
    Object getTag();

    /**
     * Cancels request.
     * Note: nothing happens if request has already been executed
     */
    void cancel();

    /**
     * @return associated request, null if request was cancelled
     */
    @Nullable
    Request getRequest();

    /**
     * Note that implementation of this method should always check if the request was cancelled.
     *
     * @return true if request was successfully executed, false if request was not executed (and
     * should be rerun)
     */
    boolean run();
}
