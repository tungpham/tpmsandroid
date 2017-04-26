package com.ethan.morephone.presentation.voice.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.ExpandableLayout;
import com.ethan.morephone.widget.TextDrawable;

import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class VoicesAdapter extends RecyclerView.Adapter<VoicesViewHolder> {

    private List<Record> mVoiceItems;
    private OnItemVoiceClickListener mOnItemVoiceClickListener;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;
    private String mPhoneNumber;
    private OnOtherExpandListener mOnOtherExpandListener;

    private RecyclerView mRecyclerView;


    public VoicesAdapter(Context context, String phoneNumber, List<Record> conversationEntities, OnItemVoiceClickListener onItemConversationClickListener) {
        mContext = context;
        mPhoneNumber = phoneNumber;
        mVoiceItems = conversationEntities;
        mOnItemVoiceClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<Record> messageItems) {
        mVoiceItems = messageItems;
        notifyDataSetChanged();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public List<Record> getData() {
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
        final Record record = mVoiceItems.get(position);
        holder.textPhoneNumber.setText(record.phoneNumber);
        holder.textTime.setText(Utils.formatDate(record.dateCreated));

        if(!TextUtils.isEmpty(record.phoneNumber)) {
            holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(record.phoneNumber.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
        }

        holder.expandableLayout.setExpanded(true, false);
        holder.expandableLayout.setTag(holder);
//        holder.expandableLayout.setOnExpandListener(mOnExpandListener);
        holder.expandableLayout.setPosition(position);
//        holder.expandableLayout.setAdapter(this);

        holder.textCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onCall(record);
            }
        });

        holder.textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onMessage(record);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onItemClick(holder, position, record);
            }
        });

        holder.imagePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onPauseRecord(holder, record);
            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onDeleteRecord(holder, position);
            }
        });

        holder.imageVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemVoiceClickListener.onVolumeRecord(holder);
            }
        });

    }

    public void setOnOtherExpandListener(OnOtherExpandListener onOtherExpandListener) {
        mOnOtherExpandListener = onOtherExpandListener;
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
            DebugTool.logD("bol: " + isExpanded);
//            if (view.getTag() instanceof VoicesViewHolder) {
//                final VoicesViewHolder holder = (VoicesViewHolder) view.getTag();
//                if (!isExpanded) {
//                    holder.visiblePlayerControl(false);
//                }
//                if (mOnOtherExpandListener != null) {
//                    mOnOtherExpandListener.onOtherExpand(isExpanded, holder.getAdapterPosition());
//                }
//            }

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
        void onItemClick(VoicesViewHolder holder, int pos, Record record);

        void onPauseRecord(VoicesViewHolder holder, Record record);

        void onVolumeRecord(VoicesViewHolder holder);

        void onDeleteRecord(VoicesViewHolder holder, int pos);

        void onCall(Record record);

        void onMessage(Record record);
    }

    public interface OnOtherExpandListener {
        void onOtherExpand(boolean isExpanding, int position);
    }
}
