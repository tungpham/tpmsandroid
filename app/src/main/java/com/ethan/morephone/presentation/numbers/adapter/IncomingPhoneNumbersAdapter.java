package com.ethan.morephone.presentation.numbers.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.utils.DateUtils;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersAdapter extends RecyclerView.Adapter<IncomingPhoneNumbersViewHolder> {

    private Context mContext;
    private List<PhoneNumber> mNumberEntities;
    private IncomingPhoneNumbersAdapter.OnItemNumberClickListener mOnItemNumberClickListener;
    private IncomingPhoneNumbersViewHolder mCurrentHolder;

    public IncomingPhoneNumbersAdapter(Context context, List<PhoneNumber> numberEntities, IncomingPhoneNumbersAdapter.OnItemNumberClickListener onItemNumberClickListener) {
        mContext = context;
        mNumberEntities = numberEntities;
        mOnItemNumberClickListener = onItemNumberClickListener;
    }

    public void replaceData(List<PhoneNumber> numberEntities) {
        mNumberEntities = numberEntities;
        notifyDataSetChanged();
    }


    public List<PhoneNumber> getData() {
        return mNumberEntities;
    }

    @Override
    public IncomingPhoneNumbersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_number, parent, false);
        return new IncomingPhoneNumbersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final IncomingPhoneNumbersViewHolder holder, final int position) {
        final PhoneNumber numberEntity = mNumberEntities.get(position);
        holder.textNumber.setText(numberEntity.getPhoneNumber());

        if (numberEntity.isPool()) {
            DebugTool.logD("EXPIRE NUMBER: " + DateUtils.formatDateExpire(numberEntity.getExpire()));
            String expire = expireRemain(numberEntity.getExpire());
            if (!expire.equals(mContext.getString(R.string.incoming_phone_number_expired))) {
                holder.textNumber.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
            } else {
                holder.textNumber.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
            }
            holder.textExpire.setVisibility(View.VISIBLE);
            holder.textExpire.setText(expire);
        } else {
            holder.textExpire.setVisibility(View.GONE);
            holder.textNumber.setTextColor(ContextCompat.getColor(mContext, R.color.colorText));
        }

//        validatePhoneNumberSelected(holder, numberEntity.phoneNumber);

        holder.relativeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemClick(holder, position);
            }
        });

        holder.imageDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemDelete(position);
            }
        });

        holder.imageMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemMessage(holder, position);
            }
        });

        holder.imageVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemVoice(holder, position);
            }
        });

        holder.imageDial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnItemNumberClickListener != null)
                    mOnItemNumberClickListener.onItemDial(holder, position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mNumberEntities.size();
    }

    public IncomingPhoneNumbersViewHolder getCurrentHolder() {
        return mCurrentHolder;
    }

    public void validatePhoneNumberSelected(IncomingPhoneNumbersViewHolder holder, String phoneNumber) {
        mCurrentHolder = holder;
//        if (MyPreference.getPhoneNumber(mContext).equals(phoneNumber)) {
//            holder.viewChoose.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
//            holder.textNumber.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimary));
//        } else {
//            holder.viewChoose.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
//            holder.textNumber.setTextColor(ContextCompat.getColor(mContext, R.color.colorText));
//        }
    }

    public void validateCurrentPhoneNumberSelected() {
        if (mCurrentHolder != null) {
//            mCurrentHolder.viewChoose.setBackgroundColor(ContextCompat.getColor(mContext, android.R.color.transparent));
//            mCurrentHolder.textNumber.setTextColor(ContextCompat.getColor(mContext, R.color.colorText));
        }
    }

    private String expireRemain(long date) {
        long different = date - System.currentTimeMillis();
        if (different < 0) {
            return mContext.getResources().getString(R.string.incoming_phone_number_expired);
        }

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        String result = mContext.getResources().getString(R.string.incoming_phone_number_remain);
        if (elapsedDays != 0) {
            result += mContext.getResources().getQuantityString(R.plurals.elapsed_day, (int) elapsedDays, elapsedDays);
        }

        if (elapsedHours != 0) {
            result += " " + mContext.getResources().getQuantityString(R.plurals.elapsed_hour, (int) elapsedHours, elapsedHours);
        }

        if (elapsedMinutes != 0) {
            result += " " + mContext.getResources().getQuantityString(R.plurals.elapsed_min, (int) elapsedMinutes, elapsedMinutes);
        }

        if (elapsedSeconds != 0) {
            result += " " + mContext.getResources().getQuantityString(R.plurals.elapsed_second, (int) elapsedSeconds, elapsedSeconds);
        }

        return result;
    }


    public interface OnItemNumberClickListener {
        void onItemClick(IncomingPhoneNumbersViewHolder holder, int pos);

        void onItemDelete(int pos);

        void onItemMessage(IncomingPhoneNumbersViewHolder holder, int pos);

        void onItemVoice(IncomingPhoneNumbersViewHolder holder, int pos);

        void onItemDial(IncomingPhoneNumbersViewHolder holder, int pos);
    }
}
