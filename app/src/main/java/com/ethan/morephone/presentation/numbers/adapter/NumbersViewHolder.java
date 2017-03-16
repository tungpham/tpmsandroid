package com.ethan.morephone.presentation.numbers.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.ethan.morephone.R;

/**
 * Created by Ethan on 3/16/17.
 */

public class NumbersViewHolder extends RecyclerView.ViewHolder {

    public TextView textNumber;
    public View viewDivider;

    public NumbersViewHolder(View itemView) {
        super(itemView);
        textNumber = (TextView) itemView.findViewById(R.id.text_number);
        viewDivider = itemView.findViewById(R.id.view_divider);
    }
}
