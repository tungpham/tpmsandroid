package com.ethan.morephone.presentation.buy.payment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.payment.card.CardActivity;

/**
 * Created by Ethan on 5/3/17.
 */

public class PaymentFragment extends BaseFragment implements View.OnClickListener {

    public static PaymentFragment getInstance() {
        return new PaymentFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);

        view.findViewById(R.id.text_payment_visa).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_payment_visa:
                startActivity(new Intent(getActivity(), CardActivity.class));
                break;
            default:
                break;
        }
    }
}
