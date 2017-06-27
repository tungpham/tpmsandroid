package com.ethan.morephone.presentation.record.adapter;

import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ethan.morephone.R;
import com.ethan.morephone.widget.progress.DotProgressBar;

/**
 * Created by Ethan on 2/16/17.
 */

public class RecordsViewHolder extends RecyclerView.ViewHolder {

    public CardView cardView;
    public ImageView imageIcon;
    public TextView textPhoneNumber;
    public TextView textTime;
//    public ExpandableLayout expandableLayout;
    public ImageView imagePause;
    public ImageView imageDelete;
    public ImageView imageVolume;
    public TextView textDuration;
    public AppCompatSeekBar seekBar;
    public DotProgressBar dotProgressBar;
    public ImageView imageStatus;
    public TextView textCall;
    public TextView textMessage;
    public RelativeLayout relativeProgress;

    private int mPosition;
    private StateRecord mStateRecord = StateRecord.START;

    public RecordsViewHolder(View itemView) {
        super(itemView);
        cardView = (CardView) itemView.findViewById(R.id.card_view);
        imageIcon = (ImageView) itemView.findViewById(R.id.image_avatar);
        textPhoneNumber = (TextView) itemView.findViewById(R.id.text_phone_number);
        textTime = (TextView) itemView.findViewById(R.id.text_time);
//        expandableLayout = (ExpandableLayout) itemView.findViewById(R.id.expandable_layout);
        imagePause = (ImageView) itemView.findViewById(R.id.image_item_voice_pause);
        imageDelete = (ImageView) itemView.findViewById(R.id.image_item_voice_delete);
        imageVolume = (ImageView) itemView.findViewById(R.id.image_item_voice_volume);
        textDuration = (TextView) itemView.findViewById(R.id.text_item_voice_duration);
        seekBar = (AppCompatSeekBar) itemView.findViewById(R.id.seek_bar_item_voice);
        dotProgressBar = (DotProgressBar) itemView.findViewById(R.id.progress_bar_item_voice);
        imageStatus = (ImageView) itemView.findViewById(R.id.image_item_voice_status);

        textCall = (TextView) itemView.findViewById(R.id.text_item_voice_call);
        textMessage = (TextView) itemView.findViewById(R.id.text_item_voice_message);
        relativeProgress = (RelativeLayout) itemView.findViewById(R.id.relative_item_voice_progress);

        showLoading(false);

//        visiblePlayerControl(false);
    }

//    public void visiblePlayerControl(boolean isVisible) {
//        if (!isVisible) {
//            imagePause.setVisibility(View.INVISIBLE);
//            imageDelete.setVisibility(View.INVISIBLE);
//            imageVolume.setVisibility(View.INVISIBLE);
//            textDuration.setVisibility(View.INVISIBLE);
//            seekBar.setVisibility(View.INVISIBLE);
//            uiPlay();
//            seekBar.setProgress(0);
//        } else {
//            imagePause.setVisibility(View.VISIBLE);
//            imageDelete.setVisibility(View.VISIBLE);
//            imageVolume.setVisibility(View.VISIBLE);
//            textDuration.setVisibility(View.VISIBLE);
//            seekBar.setVisibility(View.VISIBLE);
//        }
//    }

    public void showLoading(boolean isActive) {
        if (isActive) {
            relativeProgress.setVisibility(View.VISIBLE);
        } else {
            relativeProgress.setVisibility(View.GONE);
        }
    }

    public void mute(boolean isMute) {
        if (!isMute) {
            imageVolume.setImageResource(R.drawable.ic_volume_up_black_24dp);
        } else {
            imageVolume.setImageResource(R.drawable.ic_volume_off_black_24dp);
        }
    }

    public void uiPause() {
        imagePause.setImageResource(R.drawable.ic_pause_black_24dp);
    }

    public void uiPlay() {
        imagePause.setImageResource(R.drawable.ic_play_arrow_black_24dp);
    }

    public void setStateRecord(StateRecord stateRecord) {
        mStateRecord = stateRecord;
    }

    public StateRecord getStateRecord() {
        return mStateRecord;
    }
}
