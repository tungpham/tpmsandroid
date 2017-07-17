

package com.ethan.morephone.presentation.buy.payment.checkout;

/**
 * An exception that is passed in {@link RequestListener#onError(int, Exception)} if any error
 * occur. A response error code can be obtained through {@link #getResponse()} method.
 */
public final class BillingException extends Exception {

    private final int mResponse;

    BillingException(int response) {
        mResponse = response;
    }

    /**
     * @return error code for which this exception was created
     * @see ResponseCodes
     */
    public int getResponse() {
        return mResponse;
    }
}
