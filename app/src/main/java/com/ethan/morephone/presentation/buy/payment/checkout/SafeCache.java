
package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Crash-safe wrapper around the cache
 */
final class SafeCache implements Cache {
    @Nonnull
    private final Cache mCache;

    SafeCache(@Nonnull Cache cache) {
        mCache = cache;
    }

    @Override
    @Nullable
    public Entry get(@Nonnull Key key) {
        try {
            return mCache.get(key);
        } catch (Exception e) {
            Billing.error(e);
            return null;
        }
    }

    @Override
    public void put(@Nonnull Key key, @Nonnull Entry entry) {
        try {
            mCache.put(key, entry);
        } catch (Exception e) {
            Billing.error(e);
        }
    }

    @Override
    public void init() {
        try {
            mCache.init();
        } catch (Exception e) {
            Billing.error(e);
        }
    }

    @Override
    public void remove(@Nonnull Key key) {
        try {
            mCache.remove(key);
        } catch (Exception e) {
            Billing.error(e);
        }
    }

    @Override
    public void removeAll(int type) {
        try {
            mCache.removeAll(type);
        } catch (Exception e) {
            Billing.error(e);
        }
    }

    @Override
    public void clear() {
        try {
            mCache.clear();
        } catch (Exception e) {
            Billing.error(e);
        }
    }
}
