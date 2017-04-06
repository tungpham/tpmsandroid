package com.ethan.morephone.presentation.voice.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.ExpandableLayout;
import com.ethan.morephone.widget.TextDrawable;

import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class VoicesAdapter extends RecyclerView.Adapter<VoicesViewHolder> {

    private List<VoiceItem> mVoiceItems;
    private OnItemVoiceClickListener mOnItemVoiceClickListener;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;
    private String mPhoneNumber;

    private RecyclerView mRecyclerView;

    public VoicesAdapter(Context context, String phoneNumber, List<VoiceItem> conversationEntities, OnItemVoiceClickListener onItemConversationClickListener) {
        mContext = context;
        mPhoneNumber = phoneNumber;
        mVoiceItems = conversationEntities;
        mOnItemVoiceClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<VoiceItem> messageItems) {
        mVoiceItems = messageItems;
        notifyDataSetChanged();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public List<VoiceItem> getData() {
        return mVoiceItems;
    }

    @Override
    public VoicesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voice, parent, false);
        return new VoicesViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VoicesViewHolder holder, final int position) {
        final VoiceItem callEntity = mVoiceItems.get(position);
        if (mPhoneNumber.equals(callEntity.from)) {
            holder.textPhoneNumber.setText(callEntity.to);
        } else {
            holder.textPhoneNumber.setText(callEntity.from);
        }
        holder.textTime.setText(Utils.formatDate(callEntity.dateCreated));

        holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(callEntity.from.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));

        holder.expandableLayout.setExpanded(false, false);
        holder.expandableLayout.setTag(holder);
        holder.expandableLayout.setOnExpandListener(mOnExpandListener);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onItemClick(holder.expandableLayout, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVoiceItems.size();
    }

    private ExpandableLayout.OnExpandListener mOnExpandListener = new ExpandableLayout.OnExpandListener() {

        private boolean isScrollingToBottom = false;

        @Deprecated
        @Override
        public void onToggle(ExpandableLayout view, View child,
                             boolean isExpanded) {
        }

        @Override
        public void onExpandOffset(ExpandableLayout view, View child,
                                   float offset, boolean isExpanding) {
            if (view.getTag() instanceof VoicesViewHolder) {
                final VoicesViewHolder holder = (VoicesViewHolder) view.getTag();
                if (holder.getAdapterPosition() == getData().size() - 1) {
                    if (!isScrollingToBottom) {
                        isScrollingToBottom = true;
                        mRecyclerView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isScrollingToBottom = false;
                                mRecyclerView.scrollToPosition(holder.getAdapterPosition());
                            }
                        }, 100);
                    }
                }
            }
        }
    };

    public interface OnItemVoiceClickListener {
        void onItemClick(View view, int pos);
    }
}
