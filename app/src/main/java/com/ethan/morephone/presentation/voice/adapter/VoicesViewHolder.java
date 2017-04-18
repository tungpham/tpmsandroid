package com.ethan.morephone.presentation.voice.adapter;

import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ethan.morephone.R;
import com.ethan.morephone.widget.ExpandableLayout;
import com.ethan.morephone.widget.progress.DotProgressBar;
import com.google.android.exoplayer2.ExoPlayer;

/**
 * Created by Ethan on 2/16/17.
 */

public class VoicesViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public ImageView imageIcon;
    public TextView textPhoneNumber;
    public TextView textTime;
    public ExpandableLayout expandableLayout;
    public ImageView imagePause;
    public ImageView imageDelete;
    public ImageView imageVolume;
    public TextView textDuration;
    public AppCompatSeekBar seekBar;
    public DotProgressBar dotProgressBar;
    public ImageView imageStatus;
    public TextView textCall;
    public TextView textMessage;

    public ExoPlayer exoPlayer;

    public VoicesViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        imageIcon = (ImageView) itemView.findViewById(R.id.image_avatar);
        textPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        textTime = (TextView) itemView.findViewById(R.id.text_time);
        expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
        imagePause = (ImageView) itemView.findViewById(R.id.image_item_voice_pause);
        imageDelete = (ImageView) itemView.findViewById(R.id.image_item_voice_delete);
        imageVolume = (ImageView) itemView.findViewById(R.id.image_item_voice_volume);
        textDuration = (TextView) itemView.findViewById(R.id.text_item_voice_duration);
        seekBar = (AppCompatSeekBar) itemView.findViewById(R.id.seek_bar_item_voice);
        dotProgressBar = (DotProgressBar) itemView.findViewById(R.id.progress_bar_item_voice);
        imageStatus = (ImageView) itemView.findViewById(R.id.image_item_voice_status);
        visiblePlayerControl(false);
    }

    public void visiblePlayerControl(boolean isVisible) {
        if (!isVisible) {
            imagePause.setVisibility(View.INVISIBLE);
            imageDelete.setVisibility(View.INVISIBLE);
            imageVolume.setVisibility(View.INVISIBLE);
            textDuration.setVisibility(View.INVISIBLE);
            seekBar.setVisibility(View.INVISIBLE);
        } else {
            imagePause.setVisibility(View.VISIBLE);
            imageDelete.setVisibility(View.VISIBLE);
            imageVolume.setVisibility(View.VISIBLE);
            textDuration.setVisibility(View.VISIBLE);
            seekBar.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(boolean isActive) {
        if (isActive) {
            dotProgressBar.setVisibility(View.VISIBLE);
        } else {
            dotProgressBar.setVisibility(View.GONE);
        }
    }

    public void uiPause() {
        imagePause.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    public void uiPlay() {
        imagePause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

}
