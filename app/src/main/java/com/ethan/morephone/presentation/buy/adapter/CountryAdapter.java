package com.ethan.morephone.presentation.buy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/30/17.
 */

public class CountryAdapter extends ArrayAdapter<AvailableCountry> {

    private int mResource;
    private Context mContext;

    private List<AvailableCountry> mAvailableCountries;

    public CountryAdapter(Context context, List<AvailableCountry> availableCountries, int resource) {
        super(context, resource);
        mContext = context;
        mResource = resource;
        replaceData(availableCountries);
    }

    public void replaceData(List<AvailableCountry> availableCountries){
        mAvailableCountries = availableCountries;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mAvailableCountries.size();
    }

    @Override
    public AvailableCountry getItem(int i) {
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

        AvailableCountry availableCountry = mAvailableCountries.get(position);
        viewHolder.mTextCountryName.setText(availableCountry.country);

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

        AvailableCountry availableCountry = mAvailableCountries.get(position);
        viewHolder.mTextCountryName.setText(availableCountry.country);

        return convertView;
    }

    static class ViewHolder {
        public TextView mTextCountryName;
    }
}
