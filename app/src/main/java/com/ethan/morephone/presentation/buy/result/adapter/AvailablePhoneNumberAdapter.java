package com.ethan.morephone.presentation.buy.result.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumber;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/31/17.
 */

public class AvailablePhoneNumberAdapter extends RecyclerView.Adapter<AvailablePhoneNumberViewHolder> {

    private List<AvailablePhoneNumber> mAvailablePhoneNumbers;
    private Context mContext;
    private AvailablePhoneNumberListener mAvailablePhoneNumberListener;

    public AvailablePhoneNumberAdapter(Context context, List<AvailablePhoneNumber> availablePhoneNumbers) {
        mContext = context;
        mAvailablePhoneNumbers = availablePhoneNumbers;
    }

    public void replaceData(List<AvailablePhoneNumber> availablePhoneNumbers) {
        mAvailablePhoneNumbers = availablePhoneNumbers;
        notifyDataSetChanged();
    }

    public void setAvailablePhoneNumbers(AvailablePhoneNumberListener availablePhoneNumberListener){
        mAvailablePhoneNumberListener = availablePhoneNumberListener;
    }

    @Override
    public AvailablePhoneNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_number, parent, false);
        return new AvailablePhoneNumberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AvailablePhoneNumberViewHolder holder, int position) {
        final AvailablePhoneNumber availablePhoneNumber = mAvailablePhoneNumbers.get(position);
        holder.textPhoneNumber.setText(availablePhoneNumber.phoneNumber);
        holder.textRegion.setText(availablePhoneNumber.rateCenter + ", " + availablePhoneNumber.region);
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

    public interface AvailablePhoneNumberListener {
        void onBuyPhoneNumber(AvailablePhoneNumber availablePhoneNumber);
    }

}
