package com.ethan.morephone.presentation.buy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.CountryCode;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.adapter.CountryAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/30/17.
 */

public class BuyNumberFragment extends BaseFragment {


    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static BuyNumberFragment getInstance() {
        return new BuyNumberFragment();
    }

    private AppCompatSpinner mSpinnerCountry;
    private CountryAdapter mCountryAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buy_number, container, false);
        mSpinnerCountry = (AppCompatSpinner) view.findViewById(R.id.spinner_buy_number_country);
        mCountryAdapter = new CountryAdapter(getContext(), new ArrayList<CountryCode>(), R.layout.item_country_name);
        mSpinnerCountry.setAdapter(mCountryAdapter);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void onEvent(List<CountryCode> countryCodes) {
        mCountryAdapter.replaceData(countryCodes);
    }

}
