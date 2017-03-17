package com.ethan.morephone.presentation.message.conversation;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.event.UpdateEvent;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.ConversationListAdapter;
import com.ethan.morephone.presentation.message.list.MessageListActivity;
import com.ethan.morephone.presentation.message.list.MessageListFragment;
import com.ethan.morephone.utils.Injection;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Ethan on 2/16/17.
 */

public class ConversationsFragment extends BaseFragment implements
        ConversationListAdapter.OnItemConversationClickListener,
        View.OnClickListener,
        ConversationsContract.View {

    public static ConversationsFragment getInstance() {
        return new ConversationsFragment();
    }

    private ConversationListAdapter mConversationListAdapter;

    private List<MessageItem> mConversationEntities;

    private ConversationsContract.Presenter mPresenter;

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

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        mConversationEntities = new ArrayList<>();
        mConversationListAdapter = new ConversationListAdapter(getContext(), mConversationEntities, this);
        recyclerView.setAdapter(mConversationListAdapter);

        loadData();

        return view;
    }

    public void loadData() {
        String phoneNumber = MyPreference.getPhoneNumber(getContext());
        if (!TextUtils.isEmpty(phoneNumber)) {
            mPresenter.loadMessagesIncoming(phoneNumber);
            mPresenter.loadMessageOutgoing(phoneNumber);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

    @Override
    public void onItemClick(MessageItem conversationEntity) {
        Bundle bundle = new Bundle();
        bundle.putString(MessageListFragment.BUNDLE_PHONE_NUMBER_TO, conversationEntity.to);
        bundle.putString(MessageListFragment.BUNDLE_PHONE_NUMBER_FROM, conversationEntity.from);
        Intent intent = new Intent(getActivity(), MessageListActivity.class);
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
    public void showListMessage(List<MessageItem> smsEntities) {
        mConversationListAdapter.replaceData(smsEntities);
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
