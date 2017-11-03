package com.ethan.morephone.presentation.buy.pool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/31/17.
 */

public class PoolPhoneNumberAdapter extends RecyclerView.Adapter<PoolPhoneNumberViewHolder> {

    private List<PhoneNumber> mAvailablePhoneNumbers;
    private Context mContext;
    private PoolPhoneNumberListener mAvailablePhoneNumberListener;

    public PoolPhoneNumberAdapter(Context context, List<PhoneNumber> availablePhoneNumbers) {
        mContext = context;
        mAvailablePhoneNumbers = availablePhoneNumbers;
    }

    public void replaceData(List<PhoneNumber> availablePhoneNumbers) {
        mAvailablePhoneNumbers = availablePhoneNumbers;
        notifyDataSetChanged();
    }

    public void setAvailablePhoneNumbers(PoolPhoneNumberListener availablePhoneNumberListener){
        mAvailablePhoneNumberListener = availablePhoneNumberListener;
    }

    @Override
    public PoolPhoneNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_number, parent, false);
        return new PoolPhoneNumberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PoolPhoneNumberViewHolder holder, int position) {
        final PhoneNumber availablePhoneNumber = mAvailablePhoneNumbers.get(position);
        holder.textPhoneNumber.setText(availablePhoneNumber.getPhoneNumber());
        holder.imageBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAvailablePhoneNumberListener.onBuyPhoneNumber(availablePhoneNumber);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAvailablePhoneNumbers.size();
    }

    public interface PoolPhoneNumberListener {
        void onBuyPhoneNumber(PhoneNumber phoneNumber);
    }

}
