package com.ethan.morephone.presentation.message.list;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.model.ConversationModel;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.message.list.adapter.MessageListAdapter;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Ethan on 2/17/17.
 */

public class MessageListFragment extends BaseFragment implements
        MessageListAdapter.OnItemMessageClickListener,
        View.OnClickListener,
        MessageListContract.View,
        MessageDialog.MessageDialogListener{

    public static final String BUNDLE_PHONE_NUMBER_TO = "BUNDLE_PHONE_NUMBER_TO";
    public static final String BUNDLE_PHONE_NUMBER_FROM = "BUNDLE_PHONE_NUMBER_FROM";

    public static MessageListFragment getInstance(Bundle bundle) {
        MessageListFragment messageListFragment = new MessageListFragment();
        messageListFragment.setArguments(bundle);
        return messageListFragment;
    }


    private MessageListAdapter mMessageListAdapter;

    private AppCompatEditText mEditTextMessage;

    private MessageListContract.Presenter mPresenter;

//    private String mPhoneNumberTo;
//    private String mPhoneNumberFrom;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MessageListPresenter(
                this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetMessagesByToAndFrom(getContext()),
                Injection.providerCreateMessage(getContext()),
                Injection.providerDeleteMessage(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        setHasOptionsMenu(true);

        Bundle bundle = getArguments();

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, "");

        view.findViewById(R.id.image_send).setOnClickListener(this);

        mEditTextMessage = (AppCompatEditText) view.findViewById(R.id.edit_text_msg);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerSpacingItemDecoration mDividerSpacingItemDecoration = new DividerSpacingItemDecoration(Utils.dipToPixels(getContext(), 8));
//        recyclerView.addItemDecoration(mDividerSpacingItemDecoration);

        mMessageListAdapter = new MessageListAdapter(getContext(), new ArrayList<MessageItem>(), "+123", this);
        recyclerView.setAdapter(mMessageListAdapter);

//        mPresenter.loadMessages(mPhoneNumberTo, mPhoneNumberFrom);
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

    @Override
    public void onItemClick(int pos) {
        MessageDialog alertLoginDialog = MessageDialog.getInstance(pos);
        alertLoginDialog.show(getChildFragmentManager(), MessageDialog.class.getSimpleName());
        alertLoginDialog.setListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_send:
                String body =  mEditTextMessage.getText().toString();
                mEditTextMessage.setText("");
//                mPresenter.createMessage(mPhoneNumberTo, mPhoneNumberFrom, body);
                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading(boolean isActive) {
        if(isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showMessages(List<MessageItem> messageItems) {
        mMessageListAdapter.replaceData(messageItems);
    }

    @Override
    public void createMessageSuccess(MessageItem messageItem) {

    }

    @Override
    public void createMessageError() {

    }

    @Override
    public void setPresenter(MessageListContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCopyText(int pos) {
        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("simple text", mMessageListAdapter.getData().get(pos).body);
        clipboard.setPrimaryClip(clip);
    }

    @Override
    public void onDelete(int pos) {
        mPresenter.deleteMessage(mMessageListAdapter.getData().get(pos).sid);
        mMessageListAdapter.getData().remove(pos);
        mMessageListAdapter.notifyDataSetChanged();
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
    public void onEvent(ConversationModel conversationModel) {
        DebugTool.logD("EVENT: " + conversationModel.getPhoneNumber());
        showMessages(conversationModel.getMessageItems());
    }
}
