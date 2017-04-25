package com.ethan.morephone.presentation.voice;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.Constant;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.message.compose.ComposeFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.phone.incall.InCallActivity;
import com.ethan.morephone.presentation.voice.adapter.VoicesAdapter;
import com.ethan.morephone.presentation.voice.adapter.VoicesViewHolder;
import com.ethan.morephone.presentation.voice.dialog.AlertDeleteRecordDialog;
import com.ethan.morephone.presentation.voice.player.MyExoPlayer;
import com.ethan.morephone.presentation.voice.player.MyExoPlayerFactory;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.mp3.Mp3Extractor;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


/**
 * Created by Ethan on 3/6/17.
 */

public class VoiceFragment extends BaseFragment implements
        VoiceContract.View,
        VoicesAdapter.OnItemVoiceClickListener,
        View.OnClickListener,
        ExoPlayer.EventListener,
        AlertDeleteRecordDialog.AlertDeleteRecordListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    private static final long PROGRESS_UPDATE_INTERNAL = 10;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 10;
    private static final int PROGRESS_MAX = 1000;

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

    private MediaPlayer mMediaPlayer;

    private Record mRecordItem;

    private MyExoPlayer mExoPlayer;
    private VoicesViewHolder mVoicesViewHolder;

    private final Handler mHandler = new Handler();
    private MyRunnable mRunnableUpdateProgress;
    private ScheduledExecutorService mExecutorService;
    private ScheduledFuture<?> mScheduleFuture;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new VoicePresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerDeleteVoice(getContext()),
                Injection.providerGetCallRecords(getContext()),
                Injection.providerDeleteCallRecord(getContext()));

        mExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mVoicesAdapter = new VoicesAdapter(getContext(), mPhoneNumber, new ArrayList<Record>(), this);
        mRecyclerView.setAdapter(mVoicesAdapter);
        mVoicesAdapter.setRecyclerView(mRecyclerView);


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

//        setHasOptionsMenu(true);

        return view;
    }

    public void loadData() {
        if (Utils.isNetworkAvailable(getActivity())) {
            mPresenter.clearData();
            mPresenter.loadRecords(getContext());
        } else {
            Toast.makeText(getContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) mSwipeRefreshLayout.setRefreshing(true);
        else mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void initializeRecord(Record record, String url) {
        mRecordItem = record;

        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";
        Uri uri = Uri.parse(url);
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSourceFactory(
                userAgent, null,
                DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS,
                DefaultHttpDataSource.DEFAULT_READ_TIMEOUT_MILLIS,
                true);
        MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, Mp3Extractor.FACTORY,
                mHandler, null);
        TrackSelector trackSelector = new DefaultTrackSelector();
        DefaultLoadControl loadControl = new DefaultLoadControl();
        mExoPlayer = MyExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        //exoPlayer.addListener(this);
//        mExoPlayer.setVoicesViewHolder();
        mExoPlayer.addListener(this);
        mExoPlayer.prepare(mediaSource);
        mRunnableUpdateProgress = new MyRunnable(this);
//        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void showRecords(List<Record> records) {
        mVoicesAdapter.replaceData(records);
    }

    @Override
    public void setPresenter(VoiceContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClick(VoicesViewHolder holder, int pos, Record recordItem) {
        holder.expandableLayout.toggleExpansion();
        if(!holder.expandableLayout.isExpanded()) {
            String url = recordItem.uri.replace("json", "mp3");
            initializeRecord(recordItem, Constant.API_ROOT + url);
        }
        mVoicesViewHolder = holder;
    }

    @Override
    public void onPauseRecord(VoicesViewHolder holder) {
        if (mExoPlayer != null) {
            if (mExoPlayer.getPlayWhenReady()) {
                mExoPlayer.setPlayWhenReady(false);
                holder.uiPlay();
            } else {
                mExoPlayer.setPlayWhenReady(true);
                holder.uiPause();
            }

            mRunnableUpdateProgress.setVoicesViewHolder(holder);
            scheduleDonutProgressUpdate();
        }
    }

    @Override
    public void onVolumeRecord(VoicesViewHolder holder) {
        if (mExoPlayer != null) {
            if (mExoPlayer.getVolume() == 0f) {
                mExoPlayer.setVolume(1f);
                holder.mute(false);
            } else {
                mExoPlayer.setVolume(0f);
                holder.mute(true);
            }
        }
    }

    @Override
    public void onDeleteRecord(VoicesViewHolder holder) {
        AlertDeleteRecordDialog recordDialog = AlertDeleteRecordDialog.getInstance(mRecordItem.callSid, mRecordItem.sid);
        recordDialog.show(getChildFragmentManager(), AlertDeleteRecordDialog.class.getSimpleName());
        recordDialog.setAlertDeleteRecordListener(this);
    }

    @Override
    public void onCall(Record voiceItem) {
        Intent intent = new Intent(getActivity(), InCallActivity.class);
        Bundle bundle = new Bundle();
        String phoneNumber = "";
//        if (voiceItem.from.equals(mPhoneNumber)) {
//            phoneNumber = voiceItem.to;
//        } else {
//            phoneNumber = voiceItem.from;
//        }
        bundle.putString(InCallActivity.BUNDLE_TO_PHONE_NUMBER, phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onMessage(Record voiceItem) {
        Intent intent = new Intent(getActivity(), ComposeActivity.class);
        Bundle bundle = new Bundle();
        String phoneNumber = "";
//        if (voiceItem.from.equals(mPhoneNumber)) {
//            phoneNumber = voiceItem.to;
//        } else {
//            phoneNumber = voiceItem.from;
//        }
        bundle.putString(ComposeFragment.BUNDLE_TO_PHONE_NUMBER, phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            default:
                break;
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        DebugTool.logD("TIME LINE: " + timeline.getIndexOfPeriod(manifest));
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        if (!isLoading && mVoicesViewHolder != null) {
            mVoicesViewHolder.visiblePlayerControl(true);
            mVoicesViewHolder.showLoading(false);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        DebugTool.logD("playWhenReady: " + playWhenReady + " |||    playbackState: " + playbackState);
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {
        DebugTool.logD("onPositionDiscontinuity");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopDonutProgressUpdate();
    }

    @Override
    public void onDelete(String callSid, String recordSid) {
        mPresenter.deleteRecord(callSid, recordSid);
        mVoicesViewHolder.visiblePlayerControl(false);
    }

    private final static class MyRunnable implements Runnable {

        private VoicesViewHolder mVoicesViewHolder;
        private final WeakReference<VoiceFragment> mWeakReference;

        public MyRunnable(VoiceFragment voiceFragment) {
            mWeakReference = new WeakReference<VoiceFragment>(voiceFragment);
        }

        public void setVoicesViewHolder(VoicesViewHolder voicesViewHolder) {
            mVoicesViewHolder = voicesViewHolder;
        }

        @Override
        public void run() {
            VoiceFragment voiceFragment = mWeakReference.get();
            if (voiceFragment != null) {
                long currentPosition = voiceFragment.getCurrentPosition();
                long duration = voiceFragment.getDuration();
                int progress = (int) (currentPosition * PROGRESS_MAX / duration);
                mVoicesViewHolder.seekBar.setMax(PROGRESS_MAX);
                mVoicesViewHolder.seekBar.setProgress(progress);
                if (progress == PROGRESS_MAX) {
                    voiceFragment.stopDonutProgressUpdate();
                }
                mVoicesViewHolder.textDuration.setText(DateUtils.formatElapsedTime((duration - currentPosition) / 1000));
            }
        }
    }

    private void scheduleDonutProgressUpdate() {
        stopDonutProgressUpdate();
        if (!mExecutorService.isShutdown()) {
            mScheduleFuture = mExecutorService.scheduleAtFixedRate(
                    new Runnable() {
                        @Override
                        public void run() {
                            mHandler.post(mRunnableUpdateProgress);
                        }
                    }, PROGRESS_UPDATE_INITIAL_INTERVAL,
                    PROGRESS_UPDATE_INTERNAL, TimeUnit.MILLISECONDS);
        }
    }

    private void stopDonutProgressUpdate() {
        if (mScheduleFuture != null) {
            mScheduleFuture.cancel(false);
        }
    }

    public long getCurrentPosition() {
        if (mExoPlayer != null)
            return mExoPlayer.getCurrentPosition();
        else
            return Integer.MIN_VALUE;
    }

    private long getDuration() {
        if (mExoPlayer != null)
            return mExoPlayer.getDuration();
        else
            return Integer.MIN_VALUE;
    }

    private VoicesViewHolder getVoicesViewHolder(int pos) {
        return (VoicesViewHolder) mRecyclerView.findViewHolderForLayoutPosition(pos);
    }
}
