package com.ethan.morephone.presentation.buy.payment.fund.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 7/20/17.
 */

public class AddFundViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativePurchaseAddFund;
    public TextView textPurchaseTitle;
    public TextView textPurchaseDescription;
    public TextView textPurchasePrice;

    public AddFundViewHolder(View itemView) {
        super(itemView);
        textPurchaseTitle = (TextView) itemView.findViewById(R.id.text_purchase_title);
        textPurchaseDescription = (TextView) itemView.findViewById(R.id.text_purchase_description);
        textPurchasePrice = (TextView) itemView.findViewById(R.id.text_purchase_price);
        relativePurchaseAddFund = (RelativeLayout) itemView.findViewById(R.id.relative_purchase_add_fund);
    }
}
