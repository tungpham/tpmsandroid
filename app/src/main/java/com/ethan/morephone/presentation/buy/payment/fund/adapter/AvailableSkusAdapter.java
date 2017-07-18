package com.ethan.morephone.presentation.buy.payment.fund.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;
import com.ethan.morephone.presentation.buy.payment.fund.model.SkuItem;

import javax.annotation.Nonnull;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.SUBSCRIPTION;

/**
 * Created by Ethan on 7/18/17.
 */

public class AvailableSkusAdapter extends ArrayAdapter<SkuItem> implements Inventory.Callback {

    public AvailableSkusAdapter(Context context) {
        super(context, R.layout.support_simple_spinner_dropdown_item);
    }

    @Override
    public void onLoaded(@Nonnull Inventory.Products products) {
        final Inventory.Product product = products.get(SUBSCRIPTION);
        for (Inventory.Product pro : products) {
            for(Purchase purchase : pro.getPurchases()){
                DebugTool.logD("purchase: " + purchase.sku);
            }

            for(Sku sku : pro.getSkus()){
                DebugTool.logD("SKU: " + sku.getDisplayTitle());
            }

        }
        setNotifyOnChange(false);
        clear();
        DebugTool.logD("PRODUCT:" + product.toString());
        for (Sku sku : product.getSkus()) {
            DebugTool.logD("ALL: " + sku.getDisplayTitle());
            if (!product.isPurchased(sku)) {
                DebugTool.logD("SKU: " + sku.getDisplayTitle());
                add(new SkuItem(sku));
            }
        }
        notifyDataSetChanged();
    }
}
