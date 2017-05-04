package com.ethan.morephone.presentation.buy.result;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumber;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberFragment;
import com.ethan.morephone.presentation.buy.payment.purchase.PurchaseActivity;
import com.ethan.morephone.presentation.buy.result.adapter.AvailablePhoneNumberAdapter;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/31/17.
 */

public class AvailablePhoneNumberFragment extends BaseFragment implements
        AvailablePhoneNumberContract.View,
        AvailablePhoneNumberAdapter.AvailablePhoneNumberListener {

    public static AvailablePhoneNumberFragment getInstance(Bundle bundle) {
        AvailablePhoneNumberFragment availablePhoneNumberFragment = new AvailablePhoneNumberFragment();
        availablePhoneNumberFragment.setArguments(bundle);
        return availablePhoneNumberFragment;
    }

    private AvailablePhoneNumberAdapter mAvailablePhoneNumberAdapter;
    private AvailablePhoneNumberContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new AvailablePhoneNumberPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_result_search_number, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mAvailablePhoneNumberAdapter = new AvailablePhoneNumberAdapter(getContext(), new ArrayList<AvailablePhoneNumber>());
        recyclerView.setAdapter(mAvailablePhoneNumberAdapter);
        mAvailablePhoneNumberAdapter.setAvailablePhoneNumbers(this);

        Bundle bundle = getArguments();
        String countryCode = bundle.getString(SearchPhoneNumberFragment.BUNDLE_COUNTRY_CODE);
        String phoneNumber = bundle.getString(SearchPhoneNumberFragment.BUNDLE_PHONE_NUMBER);
        boolean smsEnabled = bundle.getBoolean(SearchPhoneNumberFragment.BUNDLE_SMS_ENABLE);
        boolean mmsEnabled = bundle.getBoolean(SearchPhoneNumberFragment.BUNDLE_MMS_ENABLE);
        boolean voiceEnabled = bundle.getBoolean(SearchPhoneNumberFragment.BUNDLE_VOICE_ENABLE);

        mPresenter.searchPhoneNumber(getContext(), countryCode, phoneNumber, smsEnabled, mmsEnabled, voiceEnabled);
        return view;
    }


    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showResultSearchNumber(List<AvailablePhoneNumber> availablePhoneNumbers) {
        mAvailablePhoneNumberAdapter.replaceData(availablePhoneNumbers);
    }

    @Override
    public void setPresenter(AvailablePhoneNumberContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onBuyPhoneNumber(AvailablePhoneNumber availablePhoneNumber) {
        startActivity(new Intent(getActivity(), PurchaseActivity.class));
    }
}
