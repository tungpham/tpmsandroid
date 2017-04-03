package com.ethan.morephone.presentation.numbers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersAdapter extends RecyclerView.Adapter<IncomingPhoneNumbersViewHolder> {

    private List<IncomingPhoneNumber> mNumberEntities;
    private IncomingPhoneNumbersAdapter.OnItemNumberClickListener mOnItemNumberClickListener;

    public IncomingPhoneNumbersAdapter(List<IncomingPhoneNumber> numberEntities, IncomingPhoneNumbersAdapter.OnItemNumberClickListener onItemNumberClickListener) {
        mNumberEntities = numberEntities;
        mOnItemNumberClickListener = onItemNumberClickListener;
    }

    public void replaceData(List<IncomingPhoneNumber> numberEntities) {
        mNumberEntities = numberEntities;
        notifyDataSetChanged();
    }


    public List<IncomingPhoneNumber> getData() {
        return mNumberEntities;
    }

    @Override
    public IncomingPhoneNumbersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_number, parent, false);
        return new IncomingPhoneNumbersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(IncomingPhoneNumbersViewHolder holder, final int position) {
        final IncomingPhoneNumber numberEntity = mNumberEntities.get(position);
        holder.textNumber.setText(numberEntity.phoneNumber);

        holder.textNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemClick(position);
            }
        });

        holder.imageCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemCall(position);
            }
        });

        holder.imageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemMessage(position);
            }
        });

        holder.imageVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemVoice(position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNumberEntities.size();
    }

    public interface OnItemNumberClickListener {
        void onItemClick(int pos);

        void onItemCall(int pos);

        void onItemMessage(int pos);

        void onItemVoice(int pos);
    }
}
