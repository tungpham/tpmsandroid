package com.ethan.morephone.presentation.buy.payment.fund.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;
import com.ethan.morephone.presentation.buy.payment.fund.model.SkuItem;

import java.util.List;

import javax.annotation.Nonnull;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.IN_APP;

/**
 * Created by Ethan on 7/20/17.
 */

public class AddFundAdapter extends RecyclerView.Adapter<AddFundViewHolder> implements Inventory.Callback {

    private List<SkuItem> mSkuItems;
    private final OnItemPurchaseClickListener mOnItemPurchaseClickListener;


    public AddFundAdapter(Context context, List<SkuItem> skuItems, OnItemPurchaseClickListener mOnItemPurchaseClickListener) {
        mSkuItems = skuItems;
        this.mOnItemPurchaseClickListener = mOnItemPurchaseClickListener;
    }

    public void replaceData(List<SkuItem> skuItems) {
        mSkuItems = skuItems;
        notifyDataSetChanged();
    }


    public List<SkuItem> getData() {
        return mSkuItems;
    }

    @Override
    public AddFundViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_add_fund, parent, false);
        return new AddFundViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddFundViewHolder holder, final int position) {
        final SkuItem skuItem = mSkuItems.get(position);
        holder.textPurchaseTitle.setText(skuItem.getSku().title);
        holder.textPurchaseDescription.setText(skuItem.getSku().description);
        holder.textPurchasePrice.setText(skuItem.getSku().price);
        holder.relativePurchaseAddFund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemPurchaseClickListener != null) {
                    mOnItemPurchaseClickListener.onItemClick(skuItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSkuItems.size();
    }

    @Override
    public void onLoaded(@Nonnull Inventory.Products products) {

        for(Inventory.Product product : products){
            for (Sku sku : product.getSkus()) {
                DebugTool.logD("SKU PRODUCT: " + sku.getDisplayTitle());
            }
            DebugTool.logD("PRODUCTS:" + product.getSkus().size());
        }
        final Inventory.Product product = products.get(IN_APP);
        for (Inventory.Product pro : products) {
            for (Purchase purchase : pro.getPurchases()) {
                DebugTool.logD("purchase: " + purchase.sku);
            }

            for (Sku sku : pro.getSkus()) {
                DebugTool.logD("SKU: " + sku.getDisplayTitle());
            }

        }

        mSkuItems.clear();
        for (Sku sku : product.getSkus()) {
            mSkuItems.add(new SkuItem(sku));
        }
        replaceData(mSkuItems);
    }

    public interface OnItemPurchaseClickListener {
        void onItemClick(SkuItem skuItem);
    }
}
