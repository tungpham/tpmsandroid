package com.ethan.morephone.presentation.buy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.morephone.data.entity.phonenumbers.AvailableCountries;
import com.android.morephone.data.entity.phonenumbers.CountryCode;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.adapter.CountryAdapter;
import com.ethan.morephone.presentation.buy.result.ResultSearchNumberActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

/**
 * Created by Ethan on 3/30/17.
 */

public class BuyNumberFragment extends BaseFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener{


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
        mSpinnerCountry.setOnItemSelectedListener(this);
        view.findViewById(R.id.button_buy_number_search).setOnClickListener(this);
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
    public void onEvent(AvailableCountries availableCountries) {
        mCountryAdapter.replaceData(availableCountries.countries);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_buy_number_search:
                startActivity(new Intent(getActivity(), ResultSearchNumberActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mSpinnerCountry.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
