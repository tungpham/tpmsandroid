
package com.ethan.morephone.presentation.buy.payment.checkout;

import java.util.List;

import javax.annotation.Nonnull;

public interface PurchaseVerifier {
    /**
     * Verifies a list of <var>purchases</var> passing all the verified purchases to
     * <var>listener</var>. Note that this method might be called on any thread and methods of
     * <var>listener</var> must be called on the same thread (if this method was called on the main
     * thread listener methods should be also called on the main thread).
     * The actual verification though might use a background thread for communicating with a remote
     * server. {@link BasePurchaseVerifier} can be used as a base class for purchase verifiers that
     * should be executed on a background thread.
     *
     * @param purchases purchases to be verified
     * @param listener  callback which gets a list of verified purchases
     * @see BasePurchaseVerifier
     */
    void verify(@Nonnull List<Purchase> purchases, @Nonnull RequestListener<List<Purchase>> listener);
}
