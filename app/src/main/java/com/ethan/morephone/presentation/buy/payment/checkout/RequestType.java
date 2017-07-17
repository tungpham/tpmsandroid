

package com.ethan.morephone.presentation.buy.payment.checkout;

import javax.annotation.Nonnull;

enum RequestType {
    BILLING_SUPPORTED("supported", Billing.DAY),
    GET_PURCHASES("purchases", 20L * Billing.MINUTE),
    GET_SKU_DETAILS("skus", Billing.DAY),
    PURCHASE("purchase", 0L),
    CHANGE_PURCHASE("change", 0L),
    CONSUME_PURCHASE("consume", 0L);

    final long expiresIn;
    @Nonnull
    final String cacheKeyName;

    RequestType(@Nonnull String cacheKeyName, long expiresIn) {
        this.cacheKeyName = cacheKeyName;
        this.expiresIn = expiresIn;
    }

    @Nonnull
    static String getCacheKeyName(int keyType) {
        return values()[keyType].cacheKeyName;
    }

    @Nonnull
    Cache.Key getCacheKey(@Nonnull String key) {
        final int keyType = getCacheKeyType();
        return new Cache.Key(keyType, key);
    }

    int getCacheKeyType() {
        return ordinal();
    }
}
