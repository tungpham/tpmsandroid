package com.ethan.morephone.presentation.buy.payment.fund.model;

import com.ethan.morephone.presentation.buy.payment.checkout.Sku;

/**
 * Created by Ethan on 7/18/17.
 */

public class SkuItem {

    private final Sku mSku;

    public SkuItem(Sku sku) {
        mSku = sku;
    }

    public Sku getSku(){
        return mSku;
    }

    @Override
    public String toString() {
        return mSku.getDisplayTitle();
    }

}
