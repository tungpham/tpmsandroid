package com.ethan.morephone.presentation.numbers.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersViewHolder extends RecyclerView.ViewHolder {

    public TextView textNumber;
    public ImageView imageDelete;
    public ImageView imageMessage;
    public ImageView imageVoice;

    public TextView textMessageUnread;
    public TextView textVoiceUnread;
    public View viewChoose;

    public IncomingPhoneNumbersViewHolder(View itemView) {
        super(itemView);
        textNumber = (TextView) itemView.findViewById(R.id.text_number);
        imageDelete = (ImageView) itemView.findViewById(R.id.image_delete);
        imageMessage = (ImageView) itemView.findViewById(R.id.image_message);
        imageVoice = (ImageView) itemView.findViewById(R.id.image_voice);
        textMessageUnread = (TextView) itemView.findViewById(R.id.text_item_number_message_unread);
        textVoiceUnread = (TextView) itemView.findViewById(R.id.text_item_number_voice_unread);
        viewChoose = itemView.findViewById(R.id.view_item_number_choose);
    }


}
