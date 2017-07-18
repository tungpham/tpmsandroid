package com.ethan.morephone.presentation.buy.payment.fund.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;
import com.ethan.morephone.presentation.buy.payment.fund.model.SkuItem;

import javax.annotation.Nonnull;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.SUBSCRIPTION;

/**
 * Created by Ethan on 7/18/17.
 */

public class TargetSkusAdapter extends ArrayAdapter<SkuItem> implements Inventory.Callback{

    public TargetSkusAdapter(Context context) {
        super(context, R.layout.support_simple_spinner_dropdown_item);
    }

    @Override
    public void onLoaded(@Nonnull Inventory.Products products) {
        final Inventory.Product product = products.get(SUBSCRIPTION);

        setNotifyOnChange(false);
        clear();
        for (Sku sku : product.getSkus()) {
            if (!product.isPurchased(sku)) {
                add(new SkuItem(sku));
            }
        }
        notifyDataSetChanged();
    }
}
