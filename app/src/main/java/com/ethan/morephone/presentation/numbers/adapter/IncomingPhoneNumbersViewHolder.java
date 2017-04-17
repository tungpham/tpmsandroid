package com.ethan.morephone.presentation.numbers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersViewHolder extends RecyclerView.ViewHolder {

    public TextView textNumber;
    public ImageView imageDelete;
    public ImageView imageMessage;
    public ImageView imageVoice;

    public IncomingPhoneNumbersViewHolder(View itemView) {
        super(itemView);
        textNumber = (TextView) itemView.findViewById(R.id.text_number);
        imageDelete = (ImageView) itemView.findViewById(R.id.image_delete);
        imageMessage = (ImageView) itemView.findViewById(R.id.image_message);
        imageVoice = (ImageView) itemView.findViewById(R.id.image_voice);
    }
}
