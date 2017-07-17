
package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

class SameThreadExecutor implements CancellableExecutor {

    @Nonnull
    public static final SameThreadExecutor INSTANCE = new SameThreadExecutor();

    private SameThreadExecutor() {
    }

    @Override
    public void execute(@Nonnull Runnable command) {
        command.run();
    }

    @Override
    public void cancel(@Nonnull Runnable runnable) {
    }
}
