package com.ethan.morephone.presentation.numbers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersViewHolder extends RecyclerView.ViewHolder {

    public RelativeLayout relativeNumber;
    public TextView textNumber;
    public TextView textExpire;
    public ImageView imageDelete;
    public ImageView imageMessage;
    public ImageView imageVoice;
    public ImageView imageDial;

    public TextView textMessageUnread;
    public TextView textVoiceUnread;
    public View viewChoose;

    public IncomingPhoneNumbersViewHolder(View itemView) {
        super(itemView);
        relativeNumber = (RelativeLayout) itemView.findViewById(R.id.relative_number);
        textNumber = (TextView) itemView.findViewById(R.id.text_number);
        textExpire = (TextView) itemView.findViewById(R.id.text_expire);
        imageDelete = (ImageView) itemView.findViewById(R.id.image_delete);
        imageMessage = (ImageView) itemView.findViewById(R.id.image_message);
        imageVoice = (ImageView) itemView.findViewById(R.id.image_voice);
        imageDial = (ImageView) itemView.findViewById(R.id.image_dial);
        textMessageUnread = (TextView) itemView.findViewById(R.id.text_item_number_message_unread);
        textVoiceUnread = (TextView) itemView.findViewById(R.id.text_item_number_voice_unread);
        viewChoose = itemView.findViewById(R.id.view_item_number_choose);
    }


}
