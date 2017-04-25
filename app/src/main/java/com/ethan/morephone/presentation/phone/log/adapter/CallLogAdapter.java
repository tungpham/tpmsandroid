package com.ethan.morephone.presentation.phone.log.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.call.Call;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TextDrawable;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class CallLogAdapter extends RecyclerView.Adapter<CallLogViewHolder> {

    private List<Call> mCalls;
    private TextDrawable.IBuilder mDrawableBuilder;
    private Context mContext;
    private String mPhoneNumber;
    private OnItemCallLogClickListener mOnItemCallLogClickListener;

    public CallLogAdapter(Context context, String phoneNumber, List<Call> conversationEntities, OnItemCallLogClickListener onItemCallLogClickListener) {
        mContext = context;
        mPhoneNumber = phoneNumber;
        mCalls = conversationEntities;
        mOnItemCallLogClickListener = onItemCallLogClickListener;
        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<Call> calls) {
        mCalls = calls;
        notifyDataSetChanged();
    }

    public List<Call> getData() {
        return mCalls;
    }

    @Override
    public CallLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_call_log, parent, false);
        return new CallLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CallLogViewHolder holder, final int position) {
        final Call callEntity = mCalls.get(position);
        if (mPhoneNumber.equals(callEntity.from)) {
            holder.textPhoneNumber.setText(callEntity.to);
            holder.imageStatus.setImageResource(R.drawable.ic_call_outgoing_holo_dark);
        } else {
            holder.textPhoneNumber.setText(callEntity.from);
            holder.imageStatus.setImageResource(R.drawable.ic_call_incoming_holo_dark);
        }
        holder.textTime.setText(Utils.formatDate(callEntity.dateCreated));

        holder.imageIcon.setImageDrawable(mDrawableBuilder.build(String.valueOf(callEntity.from.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnItemCallLogClickListener.onCall(callEntity);
            }
        });


    }


    @Override
    public int getItemCount() {
        return mCalls.size();
    }

    public interface OnItemCallLogClickListener{
        void onCall(Call call);
    }
}
