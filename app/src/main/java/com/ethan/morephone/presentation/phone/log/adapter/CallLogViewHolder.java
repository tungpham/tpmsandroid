package com.ethan.morephone.presentation.phone.log.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/16/17.
 */

public class CallLogViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public ImageView imageIcon;
    public TextView textPhoneNumber;
    public TextView textTime;
    public ImageView imageStatus;
    public Contact contact;


    public CallLogViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        imageIcon = (ImageView) itemView.findViewById(R.id.image_avatar);
        textPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        textTime = (TextView) itemView.findViewById(R.id.text_time);
        imageStatus = (ImageView) itemView.findViewById(R.id.image_item_voice_status);
    }


}
