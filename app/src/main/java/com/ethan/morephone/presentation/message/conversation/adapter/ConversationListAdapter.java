package com.ethan.morephone.presentation.message.conversation.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.R;
import com.ethan.morephone.widget.TextDrawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class ConversationListAdapter extends RecyclerView.Adapter<ConversationListViewHolder> {

    private List<MessageItem> mMessageItems;
    private OnItemConversationClickListener mOnItemConversationClickListener;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;

    public ConversationListAdapter(Context context, List<MessageItem> conversationEntities, OnItemConversationClickListener onItemConversationClickListener) {
        mContext = context;
        mMessageItems = conversationEntities;
        mOnItemConversationClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<MessageItem> messageItems){
        mMessageItems =messageItems;
        notifyDataSetChanged();
    }

    @Override
    public ConversationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_conversation, parent, false);
        return new ConversationListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ConversationListViewHolder holder, int position) {
        final MessageItem conversationEntity = mMessageItems.get(position);

        holder.textSmsTitle.setText(conversationEntity.to);
        holder.textSmsDescription.setText(conversationEntity.body);
        holder.textSmsTime.setText(com.ethan.morephone.utils.Utils.formatDate(conversationEntity.dateSent));
        holder.relativeItemSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemConversationClickListener.onItemClick(conversationEntity);
            }
        });
        holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(conversationEntity.to.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
    }

    @Override
    public int getItemCount() {
        return mMessageItems.size();
    }

    public void setFilter(List<MessageItem> smsEntities) {
        mMessageItems = new ArrayList<>();
        mMessageItems.addAll(smsEntities);
        notifyDataSetChanged();
    }

    public interface OnItemConversationClickListener {
        void onItemClick(MessageItem messageEntity);
    }
}
