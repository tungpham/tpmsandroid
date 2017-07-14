package com.ethan.morephone.presentation.record.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.record.Record;
import com.android.morephone.domain.UseCaseHandler;
import com.android.morephone.domain.usecase.call.GetCall;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.ExpandableLayout;
import com.ethan.morephone.widget.TextDrawable;

import java.util.Collections;
import java.util.List;

/**
 * Created by Ethan on 2/16/17.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordsViewHolder> {

    private List<Record> mRecords;
    private OnItemRecordClickListener mOnItemRecordClickListener;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;
    private String mPhoneNumber;
    private OnOtherExpandListener mOnOtherExpandListener;

    private RecyclerView mRecyclerView;

    private UseCaseHandler mUseCaseHandler;
    private GetCall mGetCall;


    public RecordAdapter(Context context, String phoneNumber, List<Record> conversationEntities, OnItemRecordClickListener onItemConversationClickListener) {
        mContext = context;
        mPhoneNumber = phoneNumber;
        mRecords = conversationEntities;
        mOnItemRecordClickListener = onItemConversationClickListener;
        mDrawableBuilder = TextDrawable.builder().round();

        mUseCaseHandler = Injection.providerUseCaseHandler();
        mGetCall = Injection.providerGetCall(context);
    }

    public void replaceData(List<Record> records) {
        Collections.sort(records);
        mRecords = records;
        notifyDataSetChanged();
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    public List<Record> getData() {
        return mRecords;
    }

    @Override
    public RecordsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_voice, parent, false);
        return new RecordsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final RecordsViewHolder holder, final int position) {
        final Record record = mRecords.get(position);
        holder.textPhoneNumber.setText(record.phoneNumber);
        holder.textTime.setText(Utils.formatDate(record.dateCreated));

        if (!TextUtils.isEmpty(record.phoneNumber)) {
            holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(record.phoneNumber.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
        }

        holder.showLoading(false);

        holder.textCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemRecordClickListener.onCall(record);
            }
        });

        holder.textMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemRecordClickListener.onMessage(record);
            }
        });

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemRecordClickListener.onItemClick(holder, position, record);
            }
        });

        holder.imagePause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemRecordClickListener.onPauseRecord(holder, record);
            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemRecordClickListener.onDeleteRecord(holder, position);
            }
        });

        holder.imageVolume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemRecordClickListener.onVolumeRecord(holder);
            }
        });

    }

    public void setOnOtherExpandListener(OnOtherExpandListener onOtherExpandListener) {
        mOnOtherExpandListener = onOtherExpandListener;
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    private ExpandableLayout.OnExpandListener mOnExpandListener = new ExpandableLayout.OnExpandListener() {

        private boolean isScrollingToBottom = false;

        @Deprecated
        @Override
        public void onToggle(ExpandableLayout view, View child,
                             boolean isExpanded) {
//            if (view.getTag() instanceof RecordsViewHolder) {
//                final RecordsViewHolder holder = (RecordsViewHolder) view.getTag();
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
            if (view.getTag() instanceof RecordsViewHolder) {
                final RecordsViewHolder holder = (RecordsViewHolder) view.getTag();
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

    public interface OnItemRecordClickListener {
        void onItemClick(RecordsViewHolder holder, int pos, Record record);

        void onPauseRecord(RecordsViewHolder holder, Record record);

        void onVolumeRecord(RecordsViewHolder holder);

        void onDeleteRecord(RecordsViewHolder holder, int pos);

        void onCall(Record record);

        void onMessage(Record record);
    }

    public interface OnOtherExpandListener {
        void onOtherExpand(boolean isExpanding, int position);
    }
}
