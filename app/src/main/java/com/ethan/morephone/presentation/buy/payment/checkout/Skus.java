
package com.ethan.morephone.presentation.buy.payment.checkout;

import android.os.Bundle;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

/**
 * List of SKUs as returned from {@link com.android.vending.billing.IInAppBillingService#getSkuDetails(int,
 * String, String, Bundle)} method.
 */
@Immutable
public final class Skus {

    @Nonnull
    static final String BUNDLE_LIST = "DETAILS_LIST";

    /**
     * Product type
     */
    @Nonnull
    public final String product;

    @Nonnull
    public final List<Sku> list;

    Skus(@Nonnull String product, @Nonnull List<Sku> list) {
        this.product = product;
        this.list = Collections.unmodifiableList(list);
    }

    @Nonnull
    static Skus fromBundle(@Nonnull Bundle bundle, @Nonnull String product) throws RequestException {
        final List<String> list = extractList(bundle);

        final List<Sku> skus = new ArrayList<>(list.size());
        for (String response : list) {
            try {
                skus.add(Sku.fromJson(response, product));
            } catch (JSONException e) {
                throw new RequestException(e);
            }

        }
        return new Skus(product, skus);
    }

    @Nonnull
    private static List<String> extractList(@Nonnull Bundle bundle) {
        final List<String> list = bundle.getStringArrayList(BUNDLE_LIST);
        return list != null ? list : Collections.<String>emptyList();
    }

    @Nullable
    public Sku getSku(@Nonnull String sku) {
        for (Sku s : list) {
            if (s.id.code.equals(sku)) {
                return s;
            }
        }
        return null;
    }

    public boolean hasSku(@Nonnull String sku) {
        return getSku(sku) != null;
    }
}