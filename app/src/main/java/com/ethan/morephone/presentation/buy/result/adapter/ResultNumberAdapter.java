package com.ethan.morephone.presentation.buy.result.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/31/17.
 */

public class ResultNumberAdapter extends BaseAdapter {

    private Context mContext;
    private int mResource;

    public ResultNumberAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(mResource, null, false);

            viewHolder.mTextNumber = (TextView) convertView.findViewById(R.id.text_country_name);

            convertView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {
        public TextView mTextNumber;
    }
}
