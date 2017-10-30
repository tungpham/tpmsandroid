package com.ethan.morephone.presentation.message.conversation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.fcm.NotifyFirebaseMessagingService;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.dashboard.DashboardFrag;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.ConversationListAdapter;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.message.list.MessageListActivity;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ethan on 2/16/17.
 */

public class ConversationsFragment extends BaseFragment implements
        ConversationListAdapter.OnItemConversationClickListener,
        View.OnClickListener,
        ConversationsContract.View,
        SearchView.OnQueryTextListener {

    public static ConversationsFragment getInstance(String phoneNumber, String phoneNumberId) {
        ConversationsFragment conversationsFragment = new ConversationsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER, phoneNumber);
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, phoneNumberId);
        conversationsFragment.setArguments(bundle);
        return conversationsFragment;
    }

    public static final String EXTRA_MESSAGE_BODY = "EXTRA_MESSAGE_BODY";
    public static final String EXTRA_MESSAGE_TO = "EXTRA_MESSAGE_TO";

    private final String BUNDLE_SAVE_PHONE_NUMBER = "BUNDLE_SAVE_PHONE_NUMBER";
    private final String BUNDLE_SAVE_PHONE_NUMBER_ID = "BUNDLE_SAVE_PHONE_NUMBER_ID";
    private final String BUNDLE_SAVE_CONVERSATION = "BUNDLE_SAVE_CONVERSATION";

    private ConversationListAdapter mConversationListAdapter;

    private ConversationsContract.Presenter mPresenter;

    //    private Toolbar mToolbar;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

    private String mPhoneNumber;
    private String mPhoneNumberId;

    private boolean isLoading = true;
    private int lastVisibleItem, totalItemCount;

    private UpdateMessageReceiver mUpdateMessageReceiver = new UpdateMessageReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConversationsPresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetMessages(getContext()),
                Injection.providerGetMessagesIncoming(getContext()),
                Injection.providerGetMessagesOutgoing(getContext()),
                Injection.providerCreateMessage(getContext()),
                Injection.providerCreateGroup(getContext()));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyFirebaseMessagingService.ACTION_UPDATE_MESSAGE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateMessageReceiver, intentFilter);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_conversations, container, false);

        mPhoneNumber = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);
        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

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
                    if (mPresenter.hasNextPage())
                        loadData(true);
                }
            }
        });


        mConversationListAdapter = new ConversationListAdapter(getContext(), Injection.providerUseCaseHandler(), Injection.providerGetContactByPhoneNumber(getContext()), new ArrayList<ConversationModel>(), this);
        recyclerView.setAdapter(mConversationListAdapter);

        view.findViewById(R.id.button_new_compose).setOnClickListener(this);

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
                            mConversationListAdapter.getData().clear();
                            mPresenter.clearData();
                            loadData(false);
                        }
                    });

                }
            });
        }

        restoreInstanceState(savedInstanceState);
        return view;
    }

    public void loadData(boolean isShowLoading) {
        if (Utils.isNetworkAvailable(getActivity())) {

            mPresenter.loadListMessageResource(getContext(), mPhoneNumber, mPhoneNumberId, isShowLoading);
        } else {
            Toast.makeText(getContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
        }
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPhoneNumber = savedInstanceState.getString(BUNDLE_SAVE_PHONE_NUMBER);
            mPhoneNumberId = savedInstanceState.getString(BUNDLE_SAVE_PHONE_NUMBER_ID);
            List<ConversationModel> conversationModels = savedInstanceState.getParcelableArrayList(BUNDLE_SAVE_CONVERSATION);
            if (conversationModels != null) {
                mConversationListAdapter.replaceData(conversationModels);
            } else {
                loadData(true);
            }
            DebugTool.logD("LOAD DATA FROM INSTANCE");
        } else {
            loadData(true);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_SAVE_PHONE_NUMBER, mPhoneNumber);
        outState.putString(BUNDLE_SAVE_PHONE_NUMBER_ID, mPhoneNumberId);
        outState.putParcelableArrayList(BUNDLE_SAVE_CONVERSATION, new ArrayList<Parcelable>(mConversationListAdapter.getData()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<MessageItem> filter(List<MessageItem> models, String query) {
        query = query.toLowerCase();
        final List<MessageItem> filteredModelList = new ArrayList<>();
        for (MessageItem model : models) {
            final String text = model.body.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }


    @Override
    public void onItemClick(ConversationModel conversationModel) {
        MessageListActivity.starter(getActivity(), mPhoneNumber, mPhoneNumberId, "", conversationModel);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_compose:
                Intent intent = new Intent(getActivity(), ComposeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER, mPhoneNumber);
                intent.putExtras(bundle);
                startActivityForResult(intent, DashboardFrag.REQUEST_COMPOSE);
                break;
            default:
                break;
        }
    }

    @Override
    public void showListMessage(List<ConversationModel> conversationModels) {
        if (isAdded()) {
            mConversationListAdapter.getData().addAll(conversationModels);
            mConversationListAdapter.replaceData(mConversationListAdapter.getData());
            isLoading = false;
        }
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) mSwipeRefreshLayout.setRefreshing(true);
        else mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void createMessageSuccess(List<MessageItem> messageItem) {
        loadData(false);
    }

    @Override
    public void createMessageError() {
        Toast.makeText(getContext(), "SEND ERROR", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setPresenter(ConversationsContract.Presenter presenter) {
        mPresenter = presenter;
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
        mPresenter.parseFakeData(fakeData, mPhoneNumber);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DashboardFrag.REQUEST_COMPOSE) {
            if (resultCode == Activity.RESULT_OK && data != null) {

                String body = data.getStringExtra(EXTRA_MESSAGE_BODY);
                String[] tos = data.getStringArrayExtra(EXTRA_MESSAGE_TO);

                if (tos != null && tos.length != 0) {
                    if (tos.length == 1) {
                        String toPhoneNumber = tos[0];
                        DebugTool.logD("toPhoneNumber: " + toPhoneNumber);
                        List<ConversationModel> conversationModels = mConversationListAdapter.getData();
                        if (conversationModels != null && !conversationModels.isEmpty()) {
                            for (ConversationModel model : conversationModels) {
                                if (model.mPhoneNumber.trim().equals(toPhoneNumber.trim())) {
                                    EventBus.getDefault().postSticky(model);
                                    MessageListActivity.starter(getActivity(), mPhoneNumber, mPhoneNumberId, body, model);
                                    DebugTool.logD("SEND: " + body);
                                    return;
                                }
                            }
                        }

                        ConversationModel model = new ConversationModel(toPhoneNumber, "", new ArrayList<MessageItem>());
                        MessageListActivity.starter(getActivity(), mPhoneNumber, mPhoneNumberId, body, model);

                        DebugTool.logD("OUTSIDE: " + body);

                    } else {
                        String groupPhone = TextUtils.join(",", tos);
                        ConversationModel model = new ConversationModel(groupPhone, "", new ArrayList<MessageItem>());
                        MessageListActivity.starter(getActivity(), mPhoneNumber, mPhoneNumberId, body, model);

                        //Send to group

                    }
                }

//                for (String to : tos) {
//                    if (!TextUtils.isEmpty(to)) {
//                        mPresenter.createMessage(MyPreference.getUserId(getContext()), to, mPhoneNumber, body, 0);
//                    }
//                }

            }
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateMessageReceiver);
        getActivity().finish();
    }

    class UpdateMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null)
                return;
            if (NotifyFirebaseMessagingService.ACTION_UPDATE_MESSAGE.equals(intent.getAction())) {

                //Check type UI
                String body = intent.getStringExtra(NotifyFirebaseMessagingService.EXTRA_MESSAGE_BODY);
                String fromPhoneNumber = intent.getStringExtra(NotifyFirebaseMessagingService.EXTRA_FROM_PHONE_NUMBER);
                String toPhoneNumber = intent.getStringExtra(NotifyFirebaseMessagingService.EXTRA_TO_PHONE_NUMBER);
                loadData(false);
            }
        }
    }
}
