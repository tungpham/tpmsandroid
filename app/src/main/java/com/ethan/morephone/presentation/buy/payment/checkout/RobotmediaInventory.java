
package com.ethan.morephone.presentation.buy.payment.checkout;

import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.annotation.Nonnull;

public final class RobotmediaInventory extends BaseInventory {

    private class Worker implements Runnable {

        @Nonnull
        private final Task mTask;

        Worker(@Nonnull Task task) {
            mTask = task;
        }

        @Override
        public void run() {
            if (RobotmediaDatabase.exists(mCheckout.getContext())) {
                mBackground.execute(new Loader());
            } else {
                onLoaded(RobotmediaDatabase.toInventoryProducts(ProductTypes.ALL));
            }
        }

        private class Loader implements Runnable {
            @Override
            public void run() {
                final Context context = mCheckout.getContext();
                final RobotmediaDatabase database = new RobotmediaDatabase(context);
                final Products products = database.load(mTask.getRequest());
                onLoaded(products);
            }
        }

        private void onLoaded(@Nonnull final Products products) {
            mOnLoadExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    mTask.onDone(products);
                }
            });
        }
    }

    @Nonnull
    private final Executor mBackground;
    @Nonnull
    private final Executor mOnLoadExecutor;

    public RobotmediaInventory(@Nonnull Checkout checkout, @Nonnull Executor onLoadExecutor) {
        this(checkout, Executors.newSingleThreadExecutor(), onLoadExecutor);
    }

    public RobotmediaInventory(@Nonnull Checkout checkout, @Nonnull Executor background,
            @Nonnull Executor onLoadExecutor) {
        super(checkout);
        mBackground = background;
        mOnLoadExecutor = onLoadExecutor;
    }

    @Nonnull
    @Override
    protected Runnable createWorker(@Nonnull Task task) {
        return new Worker(task);
    }
}
