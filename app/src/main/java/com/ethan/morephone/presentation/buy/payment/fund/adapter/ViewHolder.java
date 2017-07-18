package com.ethan.morephone.presentation.buy.payment.fund.adapter;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.buy.payment.checkout.Sku;

/**
 * Created by Ethan on 7/18/17.
 */

public class ViewHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {

    private final PurchasedSkusAdapter mAdapter;
    CheckBox mSubscription;
    @Nullable
    private Sku mSku;

    ViewHolder(View view, PurchasedSkusAdapter adapter) {
        super(view);
        mAdapter = adapter;

        mSubscription = (CheckBox) view.findViewById(R.id.subscription);
        mSubscription.setOnCheckedChangeListener(this);
    }

    public void onBind(Sku sku) {
        mSku = sku;
        mSubscription.setText(sku.getDisplayTitle());
        mSubscription.setChecked(mAdapter.isChecked(sku));
    }

    @Override
    public void onCheckedChanged(CompoundButton v, boolean checked) {
        if (mSku == null) {
            return;
        }
        mAdapter.onChecked(mSku, checked);
    }
}
