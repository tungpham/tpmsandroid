
package com.ethan.morephone.presentation.buy.payment.checkout;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

/**
 * Types of the products available in Billing API
 */
public final class ProductTypes {

    /**
     * Simple product. Might be purchased many times or only once depending on configuration. See
     * <a href="http://developer.android.com/google/play/billing/api.html#managed">Managed In-app
     * Products</a> docs
     */
    public static final String IN_APP = "inapp";
    /**
     * Subscription product. See <a href="http://developer.android.com/google/play/billing/api.html#subs">Subscriptions</a>
     * docs
     */
    public static final String SUBSCRIPTION = "subs";

    public static final List<String> ALL = Arrays.asList(IN_APP, SUBSCRIPTION);

    private ProductTypes() {
        throw new AssertionError();
    }

    static void checkSupported(@Nonnull String product) {
        Check.isTrue(ALL.contains(product), "Unsupported product: " + product);
    }
}
