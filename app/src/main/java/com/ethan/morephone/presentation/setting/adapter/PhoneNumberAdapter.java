package com.ethan.morephone.presentation.setting.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/30/17.
 */

public class PhoneNumberAdapter extends ArrayAdapter<PhoneNumber> {

    private int mResource;
    private Context mContext;

    private List<PhoneNumber> mAvailableCountries;

    public PhoneNumberAdapter(Context context, List<PhoneNumber> availableCountries, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
        replaceData(availableCountries);
    }

    public void replaceData(List<PhoneNumber> availableCountries) {
        mAvailableCountries = availableCountries;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAvailableCountries.size();
    }

    @Override
    public PhoneNumber getItem(int i) {
        return mAvailableCountries.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, null, false);

            viewHolder.mTextCountryName = (TextView) convertView.findViewById(R.id.text_country_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PhoneNumber availableCountry = mAvailableCountries.get(position);
        viewHolder.mTextCountryName.setText(availableCountry.getPhoneNumber());

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, null, false);

            viewHolder.mTextCountryName = (TextView) convertView.findViewById(R.id.text_country_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        PhoneNumber availableCountry = mAvailableCountries.get(position);
        viewHolder.mTextCountryName.setText(availableCountry.getPhoneNumber());

        return convertView;
    }

    static class ViewHolder {
        public TextView mTextCountryName;
    }
}
