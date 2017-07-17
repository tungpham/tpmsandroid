

package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

/**
 * Empty implementation of {@link RequestListener}. Any custom listener that cares only
 * about a subset of the methods of {@link RequestListener} can subclass this class and
 * implement only the methods it is interested in.
 */
public class EmptyRequestListener<R> implements RequestListener<R> {
    @Override
    public void onSuccess(@Nonnull R result) {
    }

    @Override
    public void onError(int response, @Nonnull Exception e) {
    }
}
