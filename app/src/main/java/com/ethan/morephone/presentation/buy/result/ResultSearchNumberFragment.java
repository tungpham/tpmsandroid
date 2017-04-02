package com.ethan.morephone.presentation.buy.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.result.adapter.ResultNumberAdapter;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/31/17.
 */

public class ResultSearchNumberFragment extends BaseFragment implements ResultSearchNumberContract.View{

    public static ResultSearchNumberFragment getInstance() {
        return new ResultSearchNumberFragment();
    }

    private ResultNumberAdapter mResultNumberAdapter;
    private ResultSearchNumberContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ResultSearchNumberPresenter(this);
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

        mResultNumberAdapter = new ResultNumberAdapter(getContext(), new ArrayList<PhoneNumber>());
        recyclerView.setAdapter(mResultNumberAdapter);
        mPresenter.searchPhoneNumber(getContext(), "US");
        return view;
    }


    @Override
    public void showLoading(boolean isActive) {
        if(isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showResultSearchNumber(List<PhoneNumber> phoneNumbers) {
        mResultNumberAdapter.replaceData(phoneNumbers);
    }

    @Override
    public void setPresenter(ResultSearchNumberContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
