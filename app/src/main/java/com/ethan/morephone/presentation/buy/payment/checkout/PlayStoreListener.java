package com.ethan.morephone.presentation.buy.payment.checkout;

public interface PlayStoreListener {
    /**
     * Called when the Play Store notifies us about the purchase changes.
     */
    void onPurchasesChanged();
}
