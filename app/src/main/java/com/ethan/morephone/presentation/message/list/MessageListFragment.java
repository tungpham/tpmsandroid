package com.ethan.morephone.presentation.message.list;

import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.MessageItem;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.entity.group.Group;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.repository.contact.source.GetContactCallback;
import com.ethan.morephone.Constant;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.fcm.NotifyFirebaseMessagingService;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.contact.ContactFragment;
import com.ethan.morephone.presentation.contact.editor.ContactEditorActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.message.list.adapter.MessageListAdapter;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.utils.ContactUtil;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Created by Ethan on 2/17/17.
 */

public class MessageListFragment extends BaseFragment implements
        MessageListAdapter.OnItemMessageClickListener,
        View.OnClickListener,
        MessageListContract.View,
        MessageDialog.MessageDialogListener {

    public static final String BUNDLE_MESSAGE_BODY = "BUNDLE_MESSAGE_BODY";

    public static MessageListFragment getInstance(Bundle bundle) {
        MessageListFragment messageListFragment = new MessageListFragment();
        messageListFragment.setArguments(bundle);
        return messageListFragment;
    }


    private MessageListAdapter mMessageListAdapter;

    private AppCompatEditText mEditTextMessage;

    private MessageListContract.Presenter mPresenter;

    private String mPhoneNumberTo;
    private String[] mPhoneNumberGroup;
    private String mGroupId;
    private String mBody;
    private String mPhoneNumberFrom;
    private String mPhoneNumberId;

    private RecyclerView mRecyclerView;

    private String mMessageBody;

    private Contact mContact;

    private UpdateMessageReceiver mUpdateMessageReceiver = new UpdateMessageReceiver();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new MessageListPresenter(
                this,
                Injection.providerUseCaseHandler(),
                Injection.providerGetMessagesByToAndFrom(getContext()),
                Injection.providerCreateMessage(getContext()),
                Injection.providerDeleteMessage(getContext()),
                Injection.providerCreateGroup(getContext()));

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(NotifyFirebaseMessagingService.ACTION_UPDATE_MESSAGE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mUpdateMessageReceiver, intentFilter);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        setHasOptionsMenu(true);

        mPhoneNumberFrom = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);
        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);
        mMessageBody = getArguments().getString(BUNDLE_MESSAGE_BODY);

        view.findViewById(R.id.image_send).setOnClickListener(this);

        mEditTextMessage = (AppCompatEditText) view.findViewById(R.id.edit_text_msg);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        DividerSpacingItemDecoration mDividerSpacingItemDecoration = new DividerSpacingItemDecoration(Utils.dipToPixels(getContext(), 8));
//        recyclerView.addItemDecoration(mDividerSpacingItemDecoration);

        mMessageListAdapter = new MessageListAdapter(getContext(), new ArrayList<MessageItem>(), "+123", this);
        mRecyclerView.setAdapter(mMessageListAdapter);

        mMessageListAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mRecyclerView.scrollToPosition(mMessageListAdapter.getItemCount() - 1);
            }
        });
//        mPresenter.loadMessages(mPhoneNumberTo, mPhoneNumberFrom);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_message, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            case R.id.menu_call:
                PhoneActivity.starterOutgoing(getActivity(), mPhoneNumberFrom, mPhoneNumberTo);
                break;
            case R.id.menu_contact:
                if (mContact == null) {
                    mContact = Contact.getBuilder().phoneNumber(mPhoneNumberTo).phoneNumberId(mPhoneNumberId).build();
                }
                Intent intent = new Intent(getActivity(), ContactEditorActivity.class);
                intent.putExtra(ContactFragment.EXTRA_CONTACT, mContact);
                intent.putExtra(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, mContact.getPhoneNumberId());
//                startActivityForResult(intent, REQUEST_EDITTOR_CONTACT);
                startActivity(intent);
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
    public void onResend(int pos) {
        MessageItem messageItem = mMessageListAdapter.getData().get(pos);
        mPresenter.createMessage(getContext(), MyPreference.getUserId(getContext()), "", System.currentTimeMillis(), mPhoneNumberTo, mPhoneNumberFrom, messageItem.body, pos, true, false);
        mMessageListAdapter.getData().get(pos).isSendFail = false;
        mMessageListAdapter.notifyItemChanged(pos);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_send:
                mMessageBody = mEditTextMessage.getText().toString();
                mEditTextMessage.setText("");
                if (TextUtils.isEmpty(mGroupId)) {
                    mPresenter.createMessage(getContext(), MyPreference.getUserId(getContext()), "", System.currentTimeMillis(), mPhoneNumberTo, mPhoneNumberFrom, mMessageBody, mMessageListAdapter.getData().size(), false, false);
                } else {
                    sendMessageToGroup(mMessageBody);
                }

                break;
            default:
                break;
        }
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showProgress(boolean isActive, int position) {
        if (isAdded()) {
            DebugTool.logD("POS: " + position);
//            MessageOutViewHolder viewHolder = (MessageOutViewHolder) mRecyclerView.findViewHolderForAdapterPosition(2);
//            MessageOutViewHolder viewHolder = (MessageOutViewHolder)mRecyclerView.findViewHolderForLayoutPosition(position);
//            if (viewHolder != null) {
//                if (isActive){
//                    viewHolder.mTypingIndicatorView.setVisibility(View.VISIBLE);
//                    DebugTool.logD("VISIBLE: " + position);
//                }
//                else viewHolder.mTypingIndicatorView.setVisibility(View.GONE);
//            } else {
//                DebugTool.logD("CANNOT FILED" + position);
//            }
            if (isActive) mMessageListAdapter.getData().get(position).isLoading = true;
            else mMessageListAdapter.getData().get(position).isLoading = false;

            mMessageListAdapter.notifyItemChanged(position);
        }
    }

    @Override
    public void showMessages(List<MessageItem> messageItems) {
        mMessageListAdapter.replaceData(messageItems);

    }

    @Override
    public void createMessageSuccess(MessageItem messageItem) {
        List<MessageItem> messageItems = mMessageListAdapter.getData();
        messageItems.add(messageItem);
        mMessageListAdapter.replaceData(messageItems);
    }

    @Override
    public void createMessageError(int position) {
        mMessageListAdapter.getData().get(position).isSendFail = true;
        mMessageListAdapter.notifyItemChanged(position);
    }

    @Override
    public void createGroupSuccess(Group group) {
        mGroupId = group.getId();
        sendMessageToGroup(mMessageBody);
    }

    @Override
    public void createGroupError() {

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
        final Toolbar toolbar = (Toolbar) getView().findViewById(R.id.tool_bar);
        final BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, conversationModel.mPhoneNumber);

        ContactUtil.getContactDisplay(toolbar, Injection.providerUseCaseHandler(), Injection.providerGetContactByPhoneNumber(getContext()), conversationModel.mPhoneNumber, new GetContactCallback() {
            @Override
            public void onContactLoaded(View view, Contact contact) {
                mContact = contact;
                if (view instanceof Toolbar) {
                    baseActivity.setTitleActionBar(toolbar, contact.getDisplayName());
                }
            }

            @Override
            public void onContactNotAvailable() {

            }
        });

        showMessages(conversationModel.mMessageItems);
        mPhoneNumberTo = conversationModel.mPhoneNumber;

        if (mPhoneNumberTo.contains(",")) {
            mPhoneNumberGroup = mPhoneNumberTo.split(",");

            if (TextUtils.isEmpty(conversationModel.mGroupId)) {
                Group group = Group.getBuilder()
                        .name(mPhoneNumberTo)
                        .phoneNumberId(mPhoneNumberId)
                        .groupPhone(Arrays.asList(mPhoneNumberGroup))
                        .userId(MyPreference.getUserId(getContext()))
                        .build();

                mPresenter.createGroup(getContext(), group);
            } else {
                mGroupId = conversationModel.mGroupId;
                sendMessageToGroup(mMessageBody);
            }
        } else {
            if (!TextUtils.isEmpty(mMessageBody)) {
                mPresenter.createMessage(getContext(), MyPreference.getUserId(getContext()), "", System.currentTimeMillis(), mPhoneNumberTo, mPhoneNumberFrom, mMessageBody, mMessageListAdapter.getData().size(), false, false);
            }
        }
    }

    private void sendMessageToGroup(String body) {
        long dateSent = System.currentTimeMillis();
//        boolean isGroup = false;
//        for (String to : mPhoneNumberGroup) {
            mPresenter.createMessage(getContext(),
                    MyPreference.getUserId(getContext()),
                    mGroupId,
                    dateSent,
                    "",
                    mPhoneNumberFrom,
                    body,
                    mMessageListAdapter.getData().size(),
                    false,
                    false
            );
//            isGroup = true;
//        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mUpdateMessageReceiver);
    }

    class UpdateMessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null || !TextUtils.isEmpty(mGroupId))
                return;
            if (NotifyFirebaseMessagingService.ACTION_UPDATE_MESSAGE.equals(intent.getAction())) {

                //Check type UI
                String body = intent.getStringExtra(NotifyFirebaseMessagingService.EXTRA_MESSAGE_BODY);
                String fromPhoneNumber = intent.getStringExtra(NotifyFirebaseMessagingService.EXTRA_FROM_PHONE_NUMBER);
                String toPhoneNumber = intent.getStringExtra(NotifyFirebaseMessagingService.EXTRA_TO_PHONE_NUMBER);

                if (mPhoneNumberTo.equals(fromPhoneNumber) && mPhoneNumberFrom.equals(toPhoneNumber)) {

                    final MessageItem messageItem = new MessageItem(
                            "",
                            "",
                            "",
                            "",
                            null,
                            toPhoneNumber,
                            fromPhoneNumber,
                            null,
                            body,
                            Constant.MESSAGE_STATUS_RECEIVED,
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            null);

                    createMessageSuccess(messageItem);
                }
            }
        }
    }
}
