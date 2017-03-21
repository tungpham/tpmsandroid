package com.ethan.morephone.presentation.phone.log;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.CallEntity;
import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.phone.log.adapter.CallLogAdapter;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/21/17.
 */

public class CallLogFragment extends BaseFragment implements
        CallLogContract.View,
        CallLogAdapter.OnItemNumberClickListener {

    private static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static CallLogFragment getInstance(String phoneNumber){
        CallLogFragment callLogFragment = new CallLogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        callLogFragment.setArguments(bundle);
        return callLogFragment;
    }

    private CallLogAdapter mCallLogAdapter;
    private String mPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_call_log, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mCallLogAdapter = new CallLogAdapter(getContext(), mPhoneNumber, new ArrayList<CallEntity>(), this);
        recyclerView.setAdapter(mCallLogAdapter);

        return view;
    }

    @Override
    public void onItemClick(int pos) {

    }

    @Override
    public void showCallLog(List<CallEntity> callEntities) {
        mCallLogAdapter.replaceData(callEntities);
    }

    @Override
    public void setPresenter(CallLogContract.Presenter presenter) {

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
    public void onEvent(FakeData fakeData) {
        showCallLog(fakeData.call_log);
        DebugTool.logD("CALL LOG 2222: " + fakeData.call_log.size());
    }
}
