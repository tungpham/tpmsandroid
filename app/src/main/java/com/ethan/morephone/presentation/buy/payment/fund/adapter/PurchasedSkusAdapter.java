package com.ethan.morephone.presentation.buy.payment.fund.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.buy.payment.checkout.Inventory;
import com.ethan.morephone.presentation.buy.payment.checkout.Purchase;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;

import static com.ethan.morephone.presentation.buy.payment.checkout.ProductTypes.SUBSCRIPTION;

/**
 * Created by Ethan on 7/18/17.
 */

public class PurchasedSkusAdapter extends RecyclerView.Adapter<ViewHolder> implements Inventory.Callback {
    private final LayoutInflater mInflater;
    private final Listener mListener;
    private final List<Sku> mSkus = new ArrayList<>();
    private final Set<Sku> mChecked = new HashSet<>();
    private final List<String> mRestoredChecked = new ArrayList<>();
    private boolean mLoaded;

    public PurchasedSkusAdapter(Activity activity, Listener listener) {
        mInflater = LayoutInflater.from(activity);
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = mInflater.inflate(R.layout.item_subscription, parent, false);
        return new ViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Sku sku = mSkus.get(position);
        holder.onBind(sku);
    }

    @Override
    public int getItemCount() {
        return mSkus.size();
    }

    private boolean updateCheckedSkus() {
        if (mLoaded && !mRestoredChecked.isEmpty()) {
            for (String restored : mRestoredChecked) {
                final Sku sku = getSku(restored);
                if (sku != null) {
                    mChecked.add(sku);
                }
            }
            mRestoredChecked.clear();
            return !mChecked.isEmpty();
        }

        boolean changed = false;
        final Iterator<Sku> it = mChecked.iterator();
        while (it.hasNext()) {
            final Sku checked = it.next();
            if (!mSkus.contains(checked)) {
                it.remove();
                changed = true;
            }
        }
        return changed;
    }

    @Nullable
    private Sku getSku(String code) {
        for (Sku sku : mSkus) {
            if (sku.id.code.equals(code)) {
                return sku;
            }
        }
        return null;
    }

    public void onChecked(Sku sku, boolean checked) {
        final int before = mChecked.size();
        if (checked) {
            mChecked.add(sku);
        } else {
            mChecked.remove(sku);
        }
        final int after = mChecked.size();
        if (before != after) {
            mListener.onCheckedChanged();
        }
    }

    public boolean isChecked(Sku sku) {
        return mChecked.contains(sku);
    }

    @Override
    public void onLoaded(@Nonnull Inventory.Products products) {
        mLoaded = true;

        // update the list
        final Inventory.Product product = products.get(SUBSCRIPTION);
        mSkus.clear();
        for (Purchase purchase : product.getPurchases()) {
            if (purchase.state != Purchase.State.PURCHASED) {
                continue;
            }
            final Sku sku = product.getSku(purchase.sku);
            if (sku != null) {
                mSkus.add(sku);
            }
        }
        // keep checked status if possible & do initial restore if needed
        final boolean checkedChanged = updateCheckedSkus();

        notifyDataSetChanged();
        if (checkedChanged) {
            mListener.onCheckedChanged();
        }
    }

    public Set<Sku> getChecked() {
        return mChecked;
    }

    public void saveSate(Bundle out) {
        if (mChecked.isEmpty()) {
            return;
        }
        final ArrayList<String> checked = new ArrayList<>();
        for (Sku sku : mChecked) {
            checked.add(sku.id.code);
        }
        out.putStringArrayList("checked", checked);
    }

    public void restoreState(Bundle in) {
//        mRestoredChecked.clear();
//        final ArrayList<String> checked = in.getStringArrayList("checked");
//        if (checked != null) {
//            mRestoredChecked.addAll(checked);
//            updateCheckedSkus();
//        }
    }

    public interface Listener {
        void onCheckedChanged();
    }
}
