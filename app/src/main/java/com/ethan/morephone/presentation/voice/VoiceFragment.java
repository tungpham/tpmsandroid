package com.ethan.morephone.presentation.voice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.event.UpdateEvent;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.voice.adapter.VoicesAdapter;
import com.ethan.morephone.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Ethan on 3/6/17.
 */

public class VoiceFragment extends BaseFragment implements
        VoiceContract.View,
        VoicesAdapter.OnItemVoiceClickListener {

    public static VoiceFragment getInstance() {
        return new VoiceFragment();
    }


    private VoicesAdapter mVoicesAdapter;

    private VoiceContract.Presenter mPresenter;

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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mVoicesAdapter = new VoicesAdapter(getContext(), new ArrayList<VoiceItem>(), this);
        recyclerView.setAdapter(mVoicesAdapter);

        loadData();

        return view;
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
    public void showVoices(List<VoiceItem> voiceItems) {
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
    public void onItemClick(int pos) {
//        mPresenter.deleteVoice(mVoicesAdapter.getData().get(pos).sid);
//        mVoicesAdapter.getData().remove(pos);
//        mVoicesAdapter.notifyDataSetChanged();
        VoiceItem voiceItem = mVoicesAdapter.getData().get(pos);
        mPresenter.createVoice(voiceItem.to, voiceItem.from, Constant.APPLICATION_SID, Constant.VOICE_SID, Constant.VOICE_SECRET);
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    // This method will be called when a HelloWorldEvent is posted
    public void onEvent(UpdateEvent event) {
        if (event.isUpdate()) loadData();
    }
}
