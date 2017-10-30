package com.ethan.morephone.presentation.record;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
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

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.entity.record.Record;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.Constant;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.message.list.MessageListActivity;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.presentation.record.adapter.RecordAdapter;
import com.ethan.morephone.presentation.record.adapter.RecordsViewHolder;
import com.ethan.morephone.presentation.record.adapter.StateRecord;
import com.ethan.morephone.presentation.record.dialog.AlertDeleteRecordDialog;
import com.ethan.morephone.presentation.record.player.MyExoPlayer;
import com.ethan.morephone.presentation.record.player.MyExoPlayerFactory;
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

public class RecordFragment extends BaseFragment implements
        RecordContract.View,
        RecordAdapter.OnItemRecordClickListener,
        View.OnClickListener,
        ExoPlayer.EventListener,
        AlertDeleteRecordDialog.AlertDeleteRecordListener {

    private static final long PROGRESS_UPDATE_INTERNAL = 10;
    private static final long PROGRESS_UPDATE_INITIAL_INTERVAL = 10;
    private static final int PROGRESS_MAX = 500;

    private final String BUNDLE_SAVE_PHONE_NUMBER = "BUNDLE_SAVE_PHONE_NUMBER";
    private final String BUNDLE_SAVE_PHONE_NUMBER_ID = "BUNDLE_SAVE_PHONE_NUMBER_ID";
    private final String BUNDLE_SAVE_RECORDS = "BUNDLE_SAVE_RECORDS";

    public static RecordFragment getInstance(String phoneNumber, String phoneNumberId) {
        RecordFragment recordFragment = new RecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER, phoneNumber);
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, phoneNumberId);
        recordFragment.setArguments(bundle);
        return recordFragment;
    }


    private RecordAdapter mRecordAdapter;

    private RecordContract.Presenter mPresenter;

    private String mPhoneNumber;
    private String mPhoneNumberId;

    private RecyclerView mRecyclerView;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

    private MediaPlayer mMediaPlayer;

    private Record mRecordItem;

    private MyExoPlayer mExoPlayer;
    private RecordsViewHolder mRecordsViewHolder;

    private final Handler mHandler = new Handler();
    private MyRunnable mRunnableUpdateProgress;
    private ScheduledExecutorService mExecutorService;
    private ScheduledFuture<?> mScheduleFuture;

    private boolean isLoading = true;
    private int lastVisibleItem, totalItemCount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new RecordPresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetCallRecords(getContext()),
                Injection.providerGetCall(getContext()),
                Injection.providerDeleteCallRecord(getContext()));

        mExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice, container, false);

        mPhoneNumber = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);
        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        mRecordAdapter = new RecordAdapter(getContext(), mPhoneNumber, new ArrayList<Record>(), this);
        mRecyclerView.setAdapter(mRecordAdapter);
        mRecordAdapter.setRecyclerView(mRecyclerView);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

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
//                    mPageIncoming++;
//                    mPageOutgoing++;
//                    DebugTool.logD("PAGE: " + mPageOutgoing);
                    if (mPresenter.hasNextPage())
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
                            mRecordAdapter.getData().clear();
                            mPresenter.clearData();
                            loadData();
                        }
                    });

                }
            });
        }

//        loadData();
        restoreInstanceState(savedInstanceState);

//        setHasOptionsMenu(true);

        return view;
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPhoneNumber = savedInstanceState.getString(BUNDLE_SAVE_PHONE_NUMBER);
            mPhoneNumberId = savedInstanceState.getString(BUNDLE_SAVE_PHONE_NUMBER_ID);
            mRecordAdapter = new RecordAdapter(getContext(), mPhoneNumber, new ArrayList<Record>(), this);
            mRecyclerView.setAdapter(mRecordAdapter);
            mRecordAdapter.setRecyclerView(mRecyclerView);
            ArrayList<Record> records = savedInstanceState.getParcelableArrayList(BUNDLE_SAVE_RECORDS);
            if (records != null && !records.isEmpty()) {
                mRecordAdapter.replaceData(records);
            } else {
                loadData();
            }
            DebugTool.logD("LOAD DATA FROM INSTANCE RECORD FRAGMENT");
        } else {
            loadData();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_SAVE_PHONE_NUMBER, mPhoneNumber);
        outState.putString(BUNDLE_SAVE_PHONE_NUMBER_ID, mPhoneNumberId);
        outState.putParcelableArrayList(BUNDLE_SAVE_RECORDS, new ArrayList<Parcelable>(mRecordAdapter.getData()));
        super.onSaveInstanceState(outState);
    }


    public void loadData() {
        if (Utils.isNetworkAvailable(getActivity())) {
            mPresenter.loadRecords(getContext(), mPhoneNumber);
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
        mExoPlayer.setPlayWhenReady(true);
        mExoPlayer.addListener(this);
        mExoPlayer.prepare(mediaSource);
        mRunnableUpdateProgress = new MyRunnable(this);
//        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void showRecords(List<Record> records) {
        if (isAdded()) {
            mRecordAdapter.getData().addAll(records);
            mRecordAdapter.replaceData(mRecordAdapter.getData());
            isLoading = false;
        }
    }

    @Override
    public void setPresenter(RecordContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onItemClick(RecordsViewHolder holder, int pos, Record recordItem) {
//        holder.expandableLayout.toggleExpansion();
//        if (!holder.expandableLayout.isExpanded()) {
//            String url = recordItem.uri.replace("json", "mp3");
//            initializeRecord(recordItem, Constant.API_ROOT + url);
//        } else {
//            closeVoice();
//        }

    }

    @Override
    public void onPauseRecord(RecordsViewHolder holder, Record recordItem) {
        DebugTool.logD("STATE: " + holder.getStateRecord());

        if (mRecordsViewHolder != null && mRecordsViewHolder != holder) {

            clearVoice();
        }

        mRecordsViewHolder = holder;
        switch (mRecordsViewHolder.getStateRecord()) {
            case START:
                String url = recordItem.uri.replace("json", "mp3");
                initializeRecord(recordItem, Constant.API_ROOT + url);
                holder.setStateRecord(StateRecord.PREPARE);
                break;
            case PLAY:
                if (mExoPlayer != null) {
                    mExoPlayer.seekTo(0);
                    mExoPlayer.setPlayWhenReady(true);
                    playRecord();
                }
                break;
            case PAUSE:
                if (mExoPlayer != null) {
                    mExoPlayer.setPlayWhenReady(false);
                    pauseRecord();
                }
                break;
            case RESUME:
                if (mExoPlayer != null) {
                    mExoPlayer.setPlayWhenReady(true);
                    playRecord();
                }
                break;
            case STOP:
                if (mExoPlayer != null) {
                    mExoPlayer.seekTo(0);
                    mExoPlayer.setPlayWhenReady(true);
                    playRecord();
                }
                break;
            default:
                break;
        }
        mRunnableUpdateProgress.setVoicesViewHolder(holder);
        scheduleDonutProgressUpdate();
    }

    @Override
    public void onVolumeRecord(RecordsViewHolder holder) {
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
    public void onDeleteRecord(RecordsViewHolder holder, int pos) {
        AlertDeleteRecordDialog recordDialog = AlertDeleteRecordDialog.getInstance(mRecordItem.callSid, mRecordItem.sid, pos);
        recordDialog.show(getChildFragmentManager(), AlertDeleteRecordDialog.class.getSimpleName());
        recordDialog.setAlertDeleteRecordListener(this);
    }

    @Override
    public void onCall(Record record) {
        PhoneActivity.starterOutgoing(getActivity(), mPhoneNumber, record.phoneNumber);
    }

    @Override
    public void onMessage(Record record) {
        ConversationModel conversationModel = new ConversationModel(record.phoneNumber, "", new ArrayList<MessageItem>());

        MessageListActivity.starter(getActivity(), mPhoneNumber, mPhoneNumberId, "", conversationModel);
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
        if (!isLoading && mRecordsViewHolder != null) {
//            mRecordsViewHolder.visiblePlayerControl(true);
            mRecordsViewHolder.imagePause.setEnabled(true);
            mRecordsViewHolder.uiPause();
            mRecordsViewHolder.setStateRecord(StateRecord.PAUSE);
            mRecordsViewHolder.showLoading(false);
        }
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        DebugTool.logD("playWhenReady: " + playWhenReady + " |||    playbackState: " + playbackState);
        switch (playbackState) {
            case 2:
                mRecordsViewHolder.showLoading(true);
                mRecordsViewHolder.imagePause.setEnabled(false);
                break;
            case 3:
                mRecordsViewHolder.showLoading(false);
                mRecordsViewHolder.imagePause.setEnabled(true);
                if (playWhenReady) {
                    playRecord();
                } else {
                    pauseRecord();
                }
                break;
            case 4:
                mRecordsViewHolder.uiPlay();
                mRecordsViewHolder.setStateRecord(StateRecord.STOP);
                break;
            default:
                break;
        }
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
    public void onDelete(String callSid, String recordSid, int position) {
        mPresenter.deleteRecord(getContext(), callSid, recordSid);
        mRecordAdapter.getData().remove(position);
        mRecordAdapter.notifyDataSetChanged();
//        mRecordsViewHolder.visiblePlayerControl(false);
    }

    private final static class MyRunnable implements Runnable {

        private RecordsViewHolder mRecordsViewHolder;
        private final WeakReference<RecordFragment> mWeakReference;

        public MyRunnable(RecordFragment recordFragment) {
            mWeakReference = new WeakReference<RecordFragment>(recordFragment);
        }

        public void setVoicesViewHolder(RecordsViewHolder recordsViewHolder) {
            mRecordsViewHolder = recordsViewHolder;
        }

        @Override
        public void run() {
            RecordFragment recordFragment = mWeakReference.get();
            if (recordFragment != null) {
                long currentPosition = recordFragment.getCurrentPosition();
                long duration = recordFragment.getDuration();
                int progress = (int) (currentPosition * PROGRESS_MAX / duration);
                mRecordsViewHolder.seekBar.setMax(PROGRESS_MAX);
                mRecordsViewHolder.seekBar.setProgress(progress);
                if (progress == PROGRESS_MAX) {
                    recordFragment.stopDonutProgressUpdate();
                }

                if (duration > 0) {
                    mRecordsViewHolder.textDuration.setText(DateUtils.formatElapsedTime((duration - currentPosition) / 1000));
                }
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

    private void clearVoice() {
        stopDonutProgressUpdate();
        mRecordsViewHolder.setStateRecord(StateRecord.START);
        mRecordsViewHolder.uiPlay();
        mExoPlayer.stop();
        mRecordsViewHolder.seekBar.setProgress(0);
        mRecordsViewHolder.textDuration.setText("");
        mRecordsViewHolder = null;
        mExoPlayer = null;
        mScheduleFuture = null;
    }

    private void playRecord() {
        mRecordsViewHolder.uiPause();
        mRecordsViewHolder.setStateRecord(StateRecord.PAUSE);
    }

    private void pauseRecord() {
        mRecordsViewHolder.uiPlay();
        mRecordsViewHolder.setStateRecord(StateRecord.RESUME);
    }

    private RecordsViewHolder getVoicesViewHolder(int pos) {
        return (RecordsViewHolder) mRecyclerView.findViewHolderForLayoutPosition(pos);
    }
}
