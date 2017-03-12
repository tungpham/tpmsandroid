package com.ethan.morephone.presentation.voice.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.ethan.morephone.R;
import com.ethan.morephone.widget.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class VoicesAdapter extends RecyclerView.Adapter<VoicesViewHolder> {

    private List<VoiceItem> mVoiceItems;
    private OnItemVoiceClickListener mOnItemVoiceClickListener;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;

    public VoicesAdapter(Context context, List<VoiceItem> conversationEntities, OnItemVoiceClickListener onItemConversationClickListener) {
        mContext = context;
        mVoiceItems = conversationEntities;
        mOnItemVoiceClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<VoiceItem> messageItems){
        mVoiceItems =messageItems;
        notifyDataSetChanged();
    }

    public List<VoiceItem> getData(){
        return mVoiceItems;
    }

    @Override
    public VoicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voice, parent, false);
        return new VoicesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(VoicesViewHolder holder, final int position) {
        final VoiceItem voiceItem = mVoiceItems.get(position);

        holder.textFrom.setText(voiceItem.fromFormatted);
        holder.textTo.setText(voiceItem.toFormatted);
        holder.textSmsTime.setText(com.ethan.morephone.utils.Utils.formatDate(voiceItem.dateCreated));
        holder.relativeItemSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onItemClick(position);
            }
        });
//        holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(conversationEntity.to.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
    }

    @Override
    public int getItemCount() {
        return mVoiceItems.size();
    }

    public void setFilter(List<VoiceItem> smsEntities) {
        mVoiceItems = new ArrayList<>();
        mVoiceItems.addAll(smsEntities);
        notifyDataSetChanged();
    }

    public interface OnItemVoiceClickListener {
        void onItemClick(int pos);
    }
}
