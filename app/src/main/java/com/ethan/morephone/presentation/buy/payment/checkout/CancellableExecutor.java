

package com.ethan.morephone.presentation.buy.payment.checkout;

import java.util.concurrent.Executor;

import javax.annotation.Nonnull;

interface CancellableExecutor extends Executor {

    @Override
    void execute(@Nonnull Runnable runnable);

    void cancel(@Nonnull Runnable runnable);
}
