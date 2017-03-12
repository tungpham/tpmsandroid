package com.ethan.morephone.presentation.voice.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 2/16/17.
 */

public class VoicesViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativeItemSms;
    public ImageView imageIcon;
    public TextView textFrom;
    public TextView textTo;
    public TextView textSmsTime;

    public VoicesViewHolder(View itemView) {
        super(itemView);
        relativeItemSms = (RelativeLayout) itemView.findViewById(R.id.relative_item_sms);
        imageIcon = (ImageView) itemView.findViewById(R.id.image_icon_sms);
        textFrom = (TextView) itemView.findViewById(R.id.text_outgoing);
        textTo = (TextView) itemView.findViewById(R.id.text_incoming);
        textSmsTime = (TextView) itemView.findViewById(R.id.text_sms_time);
    }

}
