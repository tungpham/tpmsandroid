package com.ethan.morephone.presentation.buy.pool;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.entity.phonenumbers.AvailablePhoneNumber;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberFragment;
import com.ethan.morephone.presentation.buy.payment.purchase.PurchaseActivity;
import com.ethan.morephone.presentation.buy.pool.adapter.PoolPhoneNumberAdapter;
import com.ethan.morephone.presentation.buy.result.AvailablePhoneNumberContract;
import com.ethan.morephone.presentation.buy.result.AvailablePhoneNumberPresenter;
import com.ethan.morephone.presentation.buy.result.adapter.AvailablePhoneNumberAdapter;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/31/17.
 */

public class PoolPhoneNumberFragment extends BaseFragment implements
        PoolPhoneNumberContract.View,
        PoolPhoneNumberAdapter.PoolPhoneNumberListener {

    public static PoolPhoneNumberFragment getInstance() {
        return new PoolPhoneNumberFragment();
    }

    private PoolPhoneNumberAdapter mAvailablePhoneNumberAdapter;
    private PoolPhoneNumberContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new PoolPhoneNumberPresenter(this);
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

        mAvailablePhoneNumberAdapter = new PoolPhoneNumberAdapter(getContext(), new ArrayList<PhoneNumber>());
        recyclerView.setAdapter(mAvailablePhoneNumberAdapter);
        mAvailablePhoneNumberAdapter.setAvailablePhoneNumbers(this);

        mPresenter.searchPhoneNumber(getContext());
        return view;
    }


    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showResultSearchNumber(List<PhoneNumber> availablePhoneNumbers) {
        mAvailablePhoneNumberAdapter.replaceData(availablePhoneNumbers);
    }

    @Override
    public void emptyPhoneNumber() {
        Toast.makeText(getContext(), getString(R.string.available_phone_number_empty), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(PoolPhoneNumberContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onBuyPhoneNumber(PhoneNumber availablePhoneNumber) {
        PurchaseActivity.starter(getActivity(), availablePhoneNumber.getFriendlyName(), availablePhoneNumber.getPhoneNumber(), getString(R.string.all_price_buy_phone_number), true, true, true, true, true);
    }

}
