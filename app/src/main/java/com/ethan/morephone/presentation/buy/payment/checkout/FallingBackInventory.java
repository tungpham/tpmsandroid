

package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

/**
 * Inventory which falls back to fallback {@link Inventory} if one of the products is not
 * supported.
 */
class FallingBackInventory extends BaseInventory {

    private class Worker implements Runnable {

        @Nonnull
        private final MainCallback mMainCallback = new MainCallback();
        @Nonnull
        private final FallbackCallback mFallbackCallback = new FallbackCallback();
        @Nonnull
        private final Task mTask;

        Worker(@Nonnull Task task) {
            mTask = task;
        }

        @Override
        public void run() {
            mMainCallback.load();
        }

        private class MainCallback implements Callback {

            @Override
            public void onLoaded(@Nonnull Products products) {
                if (mTask.onMaybeDone(products)) {
                    return;
                }
                mFallbackCallback.load();
            }

            public void load() {
                mMainInventory.load(mTask.getRequest(), this);
            }
        }

        private class FallbackCallback implements Callback {

            @Override
            public void onLoaded(@Nonnull Products products) {
                mTask.onDone(products);
            }

            public void load() {
                mFallbackInventory.load(mTask.getRequest(), this);
            }
        }
    }

    @Nonnull
    private final CheckoutInventory mMainInventory;
    @Nonnull
    private final Inventory mFallbackInventory;

    public FallingBackInventory(@Nonnull Checkout checkout, @Nonnull Inventory fallbackInventory) {
        super(checkout);
        mMainInventory = new CheckoutInventory(checkout);
        mFallbackInventory = fallbackInventory;
    }

    @Nonnull
    @Override
    protected Runnable createWorker(@Nonnull Task task) {
        return new Worker(task);
    }
}
