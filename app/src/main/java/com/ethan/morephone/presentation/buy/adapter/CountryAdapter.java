package com.ethan.morephone.presentation.buy.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.morephone.data.entity.CountryCode;
import com.ethan.morephone.R;

import java.util.List;

/**
 * Created by Ethan on 3/30/17.
 */

public class CountryAdapter extends BaseAdapter {

    private int mResource;
    private Context mContext;

    private List<CountryCode> mCountryCodes;

    public CountryAdapter(Context context, List<CountryCode> countryCodes, int resource) {
        mContext = context;
        mResource = resource;
        replaceData(countryCodes);
    }

    public void replaceData(List<CountryCode> countryCodes){
        mCountryCodes = countryCodes;
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return mCountryCodes.size();
    }

    @Override
    public CountryCode getItem(int i) {
        return mCountryCodes.get(i);
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

        CountryCode countryCode = mCountryCodes.get(position);
        viewHolder.mTextCountryName.setText(countryCode.name + " (+" + countryCode.number + ")");

        return convertView;
    }

    static class ViewHolder {
        public TextView mTextCountryName;
    }
}
