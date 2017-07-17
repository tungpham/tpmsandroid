

package com.ethan.morephone.presentation.buy.payment.checkout;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import static android.text.TextUtils.isEmpty;

/**
 * Checks a purchase signature using the default Android implementation - {@link Security} class.
 */
class DefaultPurchaseVerifier implements PurchaseVerifier {

    @Nonnull
    private final String mPublicKey;

    public DefaultPurchaseVerifier(@Nonnull String publicKey) {
        mPublicKey = publicKey;
    }

    @Override
    public void verify(@Nonnull List<Purchase> purchases, @Nonnull RequestListener<List<Purchase>> listener) {
        final List<Purchase> verifiedPurchases = new ArrayList<Purchase>(purchases.size());
        for (Purchase purchase : purchases) {
            if (Security.verifyPurchase(mPublicKey, purchase.data, purchase.signature)) {
                verifiedPurchases.add(purchase);
            } else {
                if (isEmpty(purchase.signature)) {
                    Billing.error("Cannot verify purchase: " + purchase + ". Signature is empty");
                } else {
                    Billing.error("Cannot verify purchase: " + purchase + ". Wrong signature");
                }
            }
        }
        listener.onSuccess(verifiedPurchases);
    }
}
