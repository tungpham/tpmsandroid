package com.ethan.morephone.presentation.phone.log.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.CallEntity;
import com.ethan.morephone.R;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.TextDrawable;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class CallLogAdapter extends RecyclerView.Adapter<CallLogViewHolder> {

    private List<CallEntity> mCallEntities;
    private CallLogAdapter.OnItemNumberClickListener mOnItemNumberClickListener;

    private TextDrawable.IBuilder mDrawableBuilder;

    private Context mContext;

    private String mPhoneNumber;

    public CallLogAdapter(Context context, String phoneNumber, List<CallEntity> numberEntities, CallLogAdapter.OnItemNumberClickListener onItemNumberClickListener) {
        mContext = context;
        mPhoneNumber = phoneNumber;

        mCallEntities = numberEntities;
        mOnItemNumberClickListener = onItemNumberClickListener;

        mDrawableBuilder = TextDrawable.builder().round();
    }

    public void replaceData(List<CallEntity> numberEntities) {
        mCallEntities = numberEntities;
        notifyDataSetChanged();
    }

    public List<CallEntity> getData() {
        return mCallEntities;
    }

    @Override
    public CallLogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) .inflate(R.layout.item_call_log, parent, false);
        return new CallLogViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CallLogViewHolder holder, final int position) {
        final CallEntity callEntity = mCallEntities.get(position);
        if(mPhoneNumber.equals(callEntity.phoneNumberIncoming)) {
            holder.textNumber.setText(callEntity.phoneNumberOutgoing);
        }else{
            holder.textNumber.setText(callEntity.phoneNumberIncoming);
        }
        holder.textTime.setText(Utils.formatDate(callEntity.dateCreated));

        holder.imageAvatar.setImageDrawable(mDrawableBuilder.build(String.valueOf(callEntity.phoneNumberIncoming.charAt(0)), ContextCompat.getColor(mContext, R.color.colorBackgroundAvatar)));
    }

    @Override
    public int getItemCount() {
        return mCallEntities.size();
    }

    public interface OnItemNumberClickListener {
        void onItemClick(int pos);

    }
}
