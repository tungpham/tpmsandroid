package com.ethan.morephone.presentation.phone.log;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.entity.call.Call;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.presentation.phone.log.adapter.CallLogAdapter;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/21/17.
 */

public class CallLogFragment extends BaseFragment implements
        CallLogContract.View,
        CallLogAdapter.OnItemCallLogClickListener {

    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static CallLogFragment getInstance(String phoneNumber) {
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

    private int mPageOutgoing = 0;
    private int mPageIncoming = 0;
    private boolean isLoading = true;
    private int lastVisibleItem, totalItemCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new CallLogPresenter(this, Injection.providerUseCaseHandler(), Injection.providerGetCallsIncoming(getContext()), Injection.providerGetCallsOutgoing(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_log, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));
        layoutManager.setReverseLayout(true);
//        layoutManager.setStackFromEnd(true);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null) {
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    linearLayoutManager.findFirstVisibleItemPosition();
                }

                if (!isLoading && totalItemCount <= (lastVisibleItem + 5)) {
                    isLoading = true;
                    mPageIncoming++;
                    mPageOutgoing++;
                    DebugTool.logD("PAGE: " + mPageOutgoing);
                    loadData();
//                    mPresenter.getTasks(mCurrPage);
                }
            }
        });


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
                            mPageIncoming = 0;
                            mPageOutgoing = 0;
                            mCallLogAdapter.getData().clear();
                            loadData();
                        }
                    });

                }
            });
        }

        mCallLogAdapter = new CallLogAdapter(getContext(), mPhoneNumber, new ArrayList<Call>(), this);
        recyclerView.setAdapter(mCallLogAdapter);

        loadData();
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
    public void showCallLog(List<Call> calls) {
        if (isAdded()) {
            mCallLogAdapter.getData().addAll(calls);
            mCallLogAdapter.replaceData(mCallLogAdapter.getData());
            isLoading = false;
        }
    }

    @Override
    public void onCall(Call call) {
        String phoneNumber;
        if (call.from.equals(mPhoneNumber)) {
            phoneNumber = call.to;
        } else {
            phoneNumber = call.from;
        }

        PhoneActivity.starterOutgoing(getActivity(), mPhoneNumber, phoneNumber);
    }

    public void loadData() {
        if (Utils.isNetworkAvailable(getActivity())) {
            mPresenter.clearData();
            mPresenter.loadCallsIncoming(mPhoneNumber, mPageIncoming);
            mPresenter.loadCallsOutgoing(mPhoneNumber, mPageOutgoing);
        } else {
            Toast.makeText(getContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
        }
    }
}
