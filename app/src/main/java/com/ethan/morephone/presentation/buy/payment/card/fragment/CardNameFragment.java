package com.ethan.morephone.presentation.buy.payment.card.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ethan.morephone.R;

import static com.ethan.morephone.presentation.buy.payment.card.CreditCardUtils.EXTRA_CARD_HOLDER_NAME;

/**
 * Created by Ethan on 5/3/17.
 */

public class CardNameFragment extends CreditCardFragment {


    private EditText mCardNameView;

    public CardNameFragment() {

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup group, Bundle state) {

        View v = inflater.inflate(R.layout.fragment_card_holder_name, group,false);
        mCardNameView = (EditText) v.findViewById(R.id.card_name);

        String name = "";
        if(getArguments() != null && getArguments().containsKey(EXTRA_CARD_HOLDER_NAME)) {
            name = getArguments().getString(EXTRA_CARD_HOLDER_NAME);
        }


        if(name == null) {
            name = "";
        }

        mCardNameView.setText(name);
        mCardNameView.addTextChangedListener(this);

        return v;
    }

    @Override
    public void afterTextChanged(Editable s) {

        onEdit(s.toString());
        if(s.length() == 16) {
            onComplete();
        }
    }

    @Override
    public void focus() {

        if(isAdded()) {
            mCardNameView.selectAll();
        }
    }
}
