package com.ethan.morephone.presentation.message.conversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import com.ethan.morephone.R;
import com.ethan.morephone.model.ConversationModel;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardFrag;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.ConversationListAdapter;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.message.list.MessageListActivity;
import com.ethan.morephone.presentation.message.list.MessageListFragment;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * Created by Ethan on 2/16/17.
 */

public class ConversationsFragment extends BaseFragment implements
        ConversationListAdapter.OnItemConversationClickListener,
        View.OnClickListener,
        ConversationsContract.View,
        SearchView.OnQueryTextListener {

    public static ConversationsFragment getInstance(Bundle bundle) {
        ConversationsFragment conversationsFragment = new ConversationsFragment();
        conversationsFragment.setArguments(bundle);
        return conversationsFragment;
    }

    public static final String EXTRA_MESSAGE_BODY = "EXTRA_MESSAGE_BODY";
    public static final String EXTRA_MESSAGE_TO = "EXTRA_MESSAGE_TO";

    private ConversationListAdapter mConversationListAdapter;

    private ConversationsContract.Presenter mPresenter;

//    private Toolbar mToolbar;
    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

    private String mPhoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConversationsPresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetMessages(getContext()),
                Injection.providerGetMessagesIncoming(getContext()),
                Injection.providerGetMessagesOutgoing(getContext()),
                Injection.providerCreateMessage(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_conversations, container, false);

        mPhoneNumber = getArguments().getString(IncomingPhoneNumbersFragment.BUNDLE_PHONE_NUMBER);

//        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
//        BaseActivity baseActivity = (BaseActivity) getActivity();
//        baseActivity.setTitleActionBar(mToolbar, mPhoneNumber);
//        baseActivity.setSubTitleActionBar(mToolbar, getString(R.string.action_bar_title_conversation_label), mPhoneNumber);
//        mToolbar.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mConversationListAdapter = new ConversationListAdapter(getContext(), new ArrayList<ConversationModel>(), this);
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
                            loadData();
                        }
                    });

                }
            });
        }

//        setHasOptionsMenu(true);

        loadData();

        return view;
    }

    public void loadData() {
        if (Utils.isNetworkAvailable(getActivity())) {
            mPresenter.clearData();
            mPresenter.loadMessagesIncoming(mPhoneNumber);
            mPresenter.loadMessageOutgoing(mPhoneNumber);
        } else {
            Toast.makeText(getContext(), getString(R.string.message_error_lost_internet), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//
//            case android.R.id.home:
//                getActivity().finish();
//                break;
//
//            default:
//                break;
//        }
//        return true;
//    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_conversation, menu);
//
//        final MenuItem item = menu.findItem(R.id.action_search);
//        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(this);
//
//        MenuItemCompat.setOnActionExpandListener(item,
//                new MenuItemCompat.OnActionExpandListener() {
//                    @Override
//                    public boolean onMenuItemActionCollapse(MenuItem item) {
//                        // Do something when collapsed
////                        mConversationListAdapter.setFilter(mConversationEntities);
//                        return true; // Return true to collapse action view
//                    }
//
//                    @Override
//                    public boolean onMenuItemActionExpand(MenuItem item) {
//                        // Do something when expanded
//                        return true; // Return true to expand action view
//                    }
//                });
//    }

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
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onItemClick(ConversationModel conversationModel) {
        EventBus.getDefault().postSticky(conversationModel);
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(MessageListFragment.BUNDLE_PHONE_NUMBER, mPhoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_compose:
                Intent intent = new Intent(getActivity(), ComposeActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(MessageListFragment.BUNDLE_PHONE_NUMBER, mPhoneNumber);
                intent.putExtras(bundle);
                startActivityForResult(intent, DashboardFrag.REQUEST_COMPOSE);
                break;
            default:
                break;
        }
    }

    @Override
    public void showListMessage(List<ConversationModel> conversationModels) {
        Collections.sort(conversationModels);
        mConversationListAdapter.replaceData(conversationModels);
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) mSwipeRefreshLayout.setRefreshing(true);
        else mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void createMessageSuccess(MessageItem messageItem) {
        loadData();
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
        if(requestCode == DashboardFrag.REQUEST_COMPOSE){
            if(resultCode == Activity.RESULT_OK && data != null) {
                String body = data.getStringExtra(EXTRA_MESSAGE_BODY);
                String[] tos = data.getStringArrayExtra(EXTRA_MESSAGE_TO);
                for (String to : tos) {
                    if (!TextUtils.isEmpty(to)) {
                        mPresenter.createMessage(to, mPhoneNumber, body, 0);
                    }
                }
            }
        }
    }
}
