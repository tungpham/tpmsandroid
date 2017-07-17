

package com.ethan.morephone.presentation.buy.payment.checkout;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.GuardedBy;

/**
 * Default Billing v.3 {@link Inventory}. Loads the purchase information from the {@link Checkout}.
 */
final class CheckoutInventory extends BaseInventory {

    private class Worker implements Checkout.Listener, Runnable {

        @Nonnull
        private final Task mTask;
        @GuardedBy("mLock")
        private int mCount;
        @GuardedBy("mLock")
        private final Products mProducts = new Products();

        Worker(@Nonnull Task task) {
            mTask = task;
        }

        @Override
        public void run() {
            // for each product we wait for:
            // 1. onReady to be called
            // 2. loadPurchased to be finished
            // 3. loadSkus to be finished
            mCount = ProductTypes.ALL.size() * 3;
            mCheckout.whenReady(this);
        }

        @Override
        public void onReady(@Nonnull BillingRequests requests) {
        }

        @Override
        public void onReady(@Nonnull BillingRequests requests, @Nonnull String productId,
                boolean billingSupported) {
            final Product product = new Product(productId, billingSupported);
            synchronized (mLock) {
                countDown();
                mProducts.add(product);
                if (!mTask.isCancelled() && product.supported && mTask.getRequest().shouldLoadPurchases(productId)) {
                    loadPurchases(requests, product);
                } else {
                    countDown(1);
                }
                if (!mTask.isCancelled() && product.supported && mTask.getRequest().shouldLoadSkus(productId)) {
                    loadSkus(requests, product);
                } else {
                    countDown(1);
                }
            }
        }

        private void countDown() {
            Check.isTrue(Thread.holdsLock(mLock), "Must be synchronized");
            countDown(1);
        }

        private void countDown(int count) {
            Check.isTrue(Thread.holdsLock(mLock), "Must be synchronized");
            mCount -= count;
            Check.isTrue(mCount >= 0, "Can't be negative");
            if (mCount == 0) {
                mTask.onDone(mProducts);
            }
        }

        private void loadPurchases(@Nonnull final BillingRequests requests,
                @Nonnull final Product product) {
            requests.getAllPurchases(product.id, synchronizedListener(new RequestListener<Purchases>() {
                @Override
                public void onSuccess(@Nonnull Purchases purchases) {
                    product.setPurchases(purchases.list);
                    countDown();
                }

                @Override
                public void onError(int response, @Nonnull Exception e) {
                    countDown();
                }
            }));
        }

        private void loadSkus(@Nonnull BillingRequests requests, @Nonnull final Product product) {
            final List<String> skuIds = mTask.getRequest().getSkus(product.id);
            if (skuIds.isEmpty()) {
                Billing.warning("There are no SKUs for \"" + product.id
                        + "\" product. No SKU information will be loaded");
                synchronized (mLock) {
                    countDown();
                }
                return;
            }
            requests.getSkus(product.id, skuIds, synchronizedListener(new RequestListener<Skus>() {
                @Override
                public void onSuccess(@Nonnull Skus skus) {
                    product.setSkus(skus.list);
                    countDown();
                }

                @Override
                public void onError(int response, @Nonnull Exception e) {
                    countDown();
                }
            }));
        }
    }

    CheckoutInventory(@Nonnull Checkout checkout) {
        super(checkout);
    }

    @Nonnull
    @Override
    protected Runnable createWorker(@Nonnull Task task) {
        return new Worker(task);
    }
}
