package com.ethan.morephone.presentation.phone.log;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.entity.call.Calls;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.phone.incall.InCallActivity;
import com.ethan.morephone.presentation.phone.log.adapter.CallLogAdapter;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;

import java.util.ArrayList;

/**
 * Created by Ethan on 3/21/17.
 */

public class CallLogFragment extends BaseFragment implements
        CallLogContract.View,
        CallLogAdapter.OnItemCallLogClickListener{

    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static CallLogFragment getInstance(String phoneNumber){
        CallLogFragment callLogFragment = new CallLogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        callLogFragment.setArguments(bundle);
        return callLogFragment;
    }

    private MultiSwipeRefreshLayout mSwipeRefreshLayout;
    private CallLogAdapter mCallLogAdapter;
    private String mPhoneNumber;

    private CallLogContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CallLogPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_log, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));


        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

         /*-------------------Pull to request ----------------*/
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            mPresenter.loadCallLogs(getContext());
                        }
                    });

                }
            });
        }

        mCallLogAdapter = new CallLogAdapter(getContext(), mPhoneNumber, new ArrayList<Call>(), this);
        recyclerView.setAdapter(mCallLogAdapter);
        mPresenter.loadCallLogs(getContext());
        return view;
    }

    @Override
    public void setPresenter(CallLogContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) mSwipeRefreshLayout.setRefreshing(true);
        else mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void showCallLog(Calls calls) {
        mCallLogAdapter.replaceData(calls.calls);
    }

    @Override
    public void onCall(Call call) {
        Intent intent = new Intent(getActivity(), InCallActivity.class);
        Bundle bundle = new Bundle();
        String phoneNumber;
        if (call.from.equals(mPhoneNumber)) {
            phoneNumber = call.to;
        } else {
            phoneNumber = call.from;
        }
        bundle.putString(InCallActivity.BUNDLE_TO_PHONE_NUMBER, phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
