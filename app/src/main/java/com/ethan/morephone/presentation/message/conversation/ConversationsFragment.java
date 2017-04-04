package com.ethan.morephone.presentation.message.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.R;
import com.ethan.morephone.model.ConversationModel;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.ConversationListAdapter;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.message.list.MessageListActivity;
import com.ethan.morephone.presentation.message.list.MessageListFragment;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.ethan.morephone.utils.Injection;

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

    public static ConversationsFragment getInstance(Bundle bundle) {
        ConversationsFragment conversationsFragment = new ConversationsFragment();
        conversationsFragment.setArguments(bundle);
        return conversationsFragment;
    }

    private ConversationListAdapter mConversationListAdapter;

    private ConversationsContract.Presenter mPresenter;

    private Toolbar mToolbar;

    private String mPhoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ConversationsPresenter(this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetMessages(getContext()),
                Injection.providerGetMessagesIncoming(getContext()),
                Injection.providerGetMessagesOutgoing(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_conversations, container, false);

        mPhoneNumber = getArguments().getString(IncomingPhoneNumbersFragment.BUNDLE_PHONE_NUMBER);

        mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(mToolbar, mPhoneNumber);
        baseActivity.setSubTitleActionBar(mToolbar, getString(R.string.action_bar_title_conversation_label), mPhoneNumber);
        mToolbar.setVisibility(View.GONE);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mConversationListAdapter = new ConversationListAdapter(getContext(), new ArrayList<ConversationModel>(), this);
        recyclerView.setAdapter(mConversationListAdapter);

        view.findViewById(R.id.button_new_compose).setOnClickListener(this);

        setHasOptionsMenu(true);

        loadData();

        return view;
    }

    public void loadData() {
        mPresenter.loadMessagesIncoming(mPhoneNumber);
        mPresenter.loadMessageOutgoing(mPhoneNumber);
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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_conversation, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
//                        mConversationListAdapter.setFilter(mConversationEntities);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
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
                startActivity(new Intent(getActivity(), ComposeActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void showListMessage(List<ConversationModel> conversationModels) {
        mConversationListAdapter.replaceData(conversationModels);
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
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
}
