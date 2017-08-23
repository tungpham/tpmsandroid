package com.ethan.morephone.presentation.buy.pool.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 4/2/17.
 */

public class PoolPhoneNumberViewHolder extends RecyclerView.ViewHolder {

    public TextView textPhoneNumber;
    public TextView textRegion;
    public ImageView imageBuy;

    public PoolPhoneNumberViewHolder(View itemView) {
        super(itemView);
        textPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        textRegion = (TextView) itemView.findViewById(R.id.text_region);
        imageBuy = (ImageView) itemView.findViewById(R.id.image_buy);
    }


}
