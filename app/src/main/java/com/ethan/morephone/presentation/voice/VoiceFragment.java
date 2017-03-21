package com.ethan.morephone.presentation.voice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.CallEntity;
import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.voice.adapter.VoicesAdapter;
import com.ethan.morephone.presentation.voice.adapter.VoicesViewHolder;
import com.ethan.morephone.utils.Injection;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ethan on 3/6/17.
 */

public class VoiceFragment extends BaseFragment implements
        VoiceContract.View,
        VoicesAdapter.OnItemVoiceClickListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static VoiceFragment getInstance(String phoneNumber){
        VoiceFragment voiceFragment = new VoiceFragment();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        voiceFragment.setArguments(bundle);
        return voiceFragment;
    }



    private VoicesAdapter mVoicesAdapter;

    private VoiceContract.Presenter mPresenter;

    private String mPhoneNumber;

    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new VoicePresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetAllVoice(getContext()),
                Injection.providerGetVoices(getContext()),
                Injection.providerGetVoicesIncoming(getContext()),
                Injection.providerGetVoicesOutgoing(getContext()),
                Injection.providerDeleteVoice(getContext()),
                Injection.providerCreateVoice(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, mPhoneNumber);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mVoicesAdapter = new VoicesAdapter(getContext(), mPhoneNumber, new ArrayList<CallEntity>(), this);
        mRecyclerView.setAdapter(mVoicesAdapter);
        mVoicesAdapter.setRecyclerView(mRecyclerView);

//        loadData();

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            default:
                break;
        }
        return true;
    }

    public void loadData() {
        String phoneNumber = MyPreference.getPhoneNumber(getContext());
        if (!TextUtils.isEmpty(phoneNumber)) {
            if (MyPreference.getInbox(getContext())) {
                mPresenter.loadVoicesIncoming(phoneNumber);
            } else {
                mPresenter.loadVoicesOutgoing(phoneNumber);
            }
        }
    }

    @Override
    public void showVoices(List<CallEntity> voiceItems) {
        mVoicesAdapter.replaceData(voiceItems);
    }

    @Override
    public void showLoading(boolean isActive) {

    }

    @Override
    public void setPresenter(VoiceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClick(View view, int pos) {
        if (view.getTag() instanceof VoicesViewHolder) {
            VoicesViewHolder holder = (VoicesViewHolder) view.getTag();
            holder.expandableLayout.toggleExpansion();
        }
//        mPresenter.deleteVoice(mVoicesAdapter.getData().get(pos).sid);
//        mVoicesAdapter.getData().remove(pos);
//        mVoicesAdapter.notifyDataSetChanged();
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
        showVoices(fakeData.call_log);
        DebugTool.logD("COME: " + fakeData.call_log.size());
    }


}
