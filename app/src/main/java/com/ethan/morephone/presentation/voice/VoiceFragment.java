package com.ethan.morephone.presentation.voice;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.twilio.voice.VoiceItem;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.phone.CallActivity;
import com.ethan.morephone.presentation.voice.adapter.VoicesAdapter;
import com.ethan.morephone.presentation.voice.adapter.VoicesViewHolder;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;

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
        VoicesAdapter.OnItemVoiceClickListener,
        View.OnClickListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static VoiceFragment getInstance(String phoneNumber) {
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
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

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
        baseActivity.setSubTitleActionBar(toolbar, getString(R.string.action_bar_title_voice_label), mPhoneNumber);
        toolbar.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mVoicesAdapter = new VoicesAdapter(getContext(), mPhoneNumber, new ArrayList<VoiceItem>(), this);
        mRecyclerView.setAdapter(mVoicesAdapter);
        mVoicesAdapter.setRecyclerView(mRecyclerView);

        view.findViewById(R.id.button_dial).setOnClickListener(this);

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
                            loadData();
                        }
                    });

                }
            });
        }

        loadData();

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
        if (Utils.isNetworkAvailable(getActivity())) {
            mPresenter.clearData();
            mPresenter.loadVoicesIncoming(mPhoneNumber);
            mPresenter.loadVoicesOutgoing(mPhoneNumber);
        } else {
            Toast.makeText(getContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showVoices(List<VoiceItem> voiceItems) {
        mVoicesAdapter.replaceData(voiceItems);
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) mSwipeRefreshLayout.setRefreshing(true);
        else mSwipeRefreshLayout.setRefreshing(false);
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
//        showVoices(fakeData.call_log);
        DebugTool.logD("COME: " + fakeData.call_log.size());
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_dial:
                startActivity(new Intent(getActivity(), CallActivity.class));
                break;
            default:
                break;
        }
    }
}
