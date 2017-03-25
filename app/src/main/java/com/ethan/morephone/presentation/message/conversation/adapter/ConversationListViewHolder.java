package com.ethan.morephone.presentation.message.conversation.adapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 2/16/17.
 */

public class ConversationListViewHolder extends RecyclerView.ViewHolder {

    public CardView relativeItemSms;
    public ImageView imageIcon;
    public TextView textSmsTitle;
    public TextView textSmsDescription;
    public TextView textSmsTime;

    public ConversationListViewHolder(View itemView) {
        super(itemView);
        relativeItemSms = (CardView) itemView.findViewById(R.id.relative_item_sms);
        imageIcon = (ImageView) itemView.findViewById(R.id.image_icon_sms);
        textSmsTitle = (TextView) itemView.findViewById(R.id.text_sms_title);
        textSmsDescription = (TextView) itemView.findViewById(R.id.text_sms_description);
        textSmsTime = (TextView) itemView.findViewById(R.id.text_sms_time);
    }




}
