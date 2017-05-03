package com.ethan.morephone.presentation.buy.payment.card.fragment;

import android.support.v4.app.Fragment;
import android.text.TextWatcher;

import com.ethan.morephone.presentation.buy.payment.card.IActionListener;
import com.ethan.morephone.presentation.buy.payment.card.IFocus;

/**
 * Created by Ethan on 5/3/17.
 */

public abstract class CreditCardFragment extends Fragment implements TextWatcher, IFocus {

    protected IActionListener mActionListener;

    public void setActionListener(IActionListener listener) {
        mActionListener = listener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }


    public void onEdit(String edit) {

        if(mActionListener != null) {
            mActionListener.onEdit(this,edit);
        }

    }

    public void onComplete() {

        if(mActionListener != null) {
            mActionListener.onActionComplete(this);
        }

    }
}
