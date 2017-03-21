package com.ethan.morephone.presentation.phone.log.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/16/17.
 */

public class CallLogViewHolder extends RecyclerView.ViewHolder {

    public TextView textNumber;
    public ImageView imageAvatar;
    public TextView textTime;

    public CallLogViewHolder(View itemView) {
        super(itemView);
        textNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        imageAvatar = (ImageView) itemView.findViewById(R.id.image_avatar);
        textTime = (TextView) itemView.findViewById(R.id.text_time);
    }
}
