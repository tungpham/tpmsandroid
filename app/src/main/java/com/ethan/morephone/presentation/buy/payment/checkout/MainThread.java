
package com.ethan.morephone.presentation.buy.payment.checkout;

import android.os.Handler;
import android.os.Looper;

import javax.annotation.Nonnull;

/**
 * Utility class that executes runnables on the main application thread
 */
final class MainThread implements CancellableExecutor {

    @Nonnull
    private final Handler mHandler;

    MainThread(@Nonnull Handler handler) {
        Check.isTrue(handler.getLooper() == Looper.getMainLooper(), "Should be main application thread handler");
        mHandler = handler;
    }

    static boolean isMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    /**
     * Method executes <var>runnable</var> on the main application thread. If method is called on
     * the main application thread then the passed <var>runnable</var> is executed synchronously.
     * Otherwise, it is posted to be executed on the next iteration of the main thread looper.
     *
     * @param runnable runnable to be executed on the main application thread
     */
    @Override
    public void execute(@Nonnull Runnable runnable) {
        if (MainThread.isMainThread()) {
            runnable.run();
        } else {
            mHandler.post(runnable);
        }
    }

    @Override
    public void cancel(@Nonnull Runnable runnable) {
        mHandler.removeCallbacks(runnable);
    }
}
