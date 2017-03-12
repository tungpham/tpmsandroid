package com.ethan.morephone.presentation.message.list.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 2/16/17.
 */

public class MessageOutViewHolder extends RecyclerView.ViewHolder {

    public ImageView imageAvatar;
    public TextView textMessageBody;
    public TextView textMessageTime;
    public RelativeLayout relativeMessageBlock;

    public MessageOutViewHolder(View itemView) {
        super(itemView);
        imageAvatar = (ImageView) itemView.findViewById(R.id.image_icon_avatar);
        textMessageBody = (TextView) itemView.findViewById(R.id.text_message_body);
        textMessageTime = (TextView) itemView.findViewById(R.id.text_message_time);
        relativeMessageBlock = (RelativeLayout) itemView.findViewById(R.id.relative_message_block);
    }

}
