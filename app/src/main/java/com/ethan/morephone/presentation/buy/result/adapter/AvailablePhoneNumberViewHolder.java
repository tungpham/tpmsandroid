package com.ethan.morephone.presentation.buy.result.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 4/2/17.
 */

public class AvailablePhoneNumberViewHolder extends RecyclerView.ViewHolder {

    public TextView textPhoneNumber;
    public TextView textRegion;

    public AvailablePhoneNumberViewHolder(View itemView) {
        super(itemView);
        textPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        textRegion = (TextView) itemView.findViewById(R.id.text_region);
    }


}
