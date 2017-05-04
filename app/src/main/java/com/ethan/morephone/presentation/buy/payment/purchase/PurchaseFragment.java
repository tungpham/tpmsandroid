package com.ethan.morephone.presentation.buy.payment.purchase;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;

/**
 * Created by Ethan on 5/4/17.
 */

public class PurchaseFragment extends BaseFragment {

    public static PurchaseFragment getInstance() {
        return new PurchaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        return view;
    }
}
