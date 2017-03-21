package com.ethan.morephone.presentation.voice.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;
import com.ethan.morephone.widget.ExpandableLayout;

/**
 * Created by Ethan on 2/16/17.
 */

public class VoicesViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public ImageView imageIcon;
    public TextView textPhoneNumber;
    public TextView textTime;
    public ExpandableLayout expandableLayout;

    public VoicesViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        imageIcon = (ImageView) itemView.findViewById(R.id.image_avatar);
        textPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        textTime = (TextView) itemView.findViewById(R.id.text_time);
        expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
    }

}
