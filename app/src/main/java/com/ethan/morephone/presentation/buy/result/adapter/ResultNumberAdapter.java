package com.ethan.morephone.presentation.buy.result.adapter;

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

public class ResultNumberAdapter extends RecyclerView.Adapter<ResultNumberViewHolder> {

    private List<PhoneNumber> mPhoneNumbers;
    private Context mContext;

    public ResultNumberAdapter(Context context, List<PhoneNumber> phoneNumbers) {
        mContext = context;
        mPhoneNumbers = phoneNumbers;
    }

    public void replaceData(List<PhoneNumber> phoneNumbers) {
        mPhoneNumbers = phoneNumbers;
        notifyDataSetChanged();
    }

    @Override
    public ResultNumberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_phone_number, parent, false);
        return new ResultNumberViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ResultNumberViewHolder holder, int position) {
        final PhoneNumber phoneNumber = mPhoneNumbers.get(position);
        holder.textPhoneNumber.setText(phoneNumber.phoneNumber);
        holder.textRegion.setText(phoneNumber.rateCenter + ", " + phoneNumber.region);
    }

    @Override
    public int getItemCount() {
        return mPhoneNumbers.size();
    }

}
