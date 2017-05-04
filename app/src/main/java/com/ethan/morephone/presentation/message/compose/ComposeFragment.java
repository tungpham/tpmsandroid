package com.ethan.morephone.presentation.message.compose;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.ex.chips.recipientchip.DrawableRecipientChip;
import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.widget.AutoCompleteContactView;

/**
 * Created by Ethan on 2/22/17.
 */

public class ComposeFragment extends BaseFragment implements View.OnClickListener, ComposeContract.View {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    public static final String BUNDLE_TO_PHONE_NUMBER = "BUNDLE_TO_PHONE_NUMBER";

    public static ComposeFragment getInstance(Bundle bundle) {
        ComposeFragment composeFragment = new ComposeFragment();
        composeFragment.setArguments(bundle);
        return composeFragment;
    }

    private AutoCompleteContactView mRecipients;
    private AppCompatEditText mEditTextBody;

    private ComposeContract.Presenter mPresenter;
    private String mPhoneNumber;
    private String mToPhoneNumber;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ComposePresenter(this, Injection.providerUseCaseHandler(), Injection.providerCreateMessage(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_compose, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);
        mToPhoneNumber = getArguments().getString(BUNDLE_TO_PHONE_NUMBER);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, getString(R.string.message_compose_label));

        mRecipients = (AutoCompleteContactView) view.findViewById(R.id.auto_complete_recipient);

        if(!TextUtils.isEmpty(mToPhoneNumber)){
            mRecipients.setText(mToPhoneNumber);
        }

        mEditTextBody = (AppCompatEditText) view.findViewById(R.id.edit_text_msg);


        view.findViewById(R.id.image_send).setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_send:
                String body = mEditTextBody.getEditableText().toString();
                Intent intent = new Intent();
                intent.putExtra(ConversationsFragment.EXTRA_MESSAGE_BODY, body);
                intent.putExtra(ConversationsFragment.EXTRA_MESSAGE_TO, getRecipientAddresses());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
//                mEditTextBody.setText("");
//
//                for (String to : getRecipientAddresses()) {
//                    if (!TextUtils.isEmpty(to)) {
//                        mPresenter.createMessage(to, mPhoneNumber, body, 0);
//                    }
//                }
                break;
        }
    }

    @Override
    public void createMessageSuccess(MessageItem messageItem) {
//        getActivity().setResult(Activity.RESULT_OK);
//        getActivity().finish();
    }

    @Override
    public void createMessageError() {
        Toast.makeText(getContext(), "SEND ERROR", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    public void setPresenter(ComposeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public String[] getRecipientAddresses() {
        DrawableRecipientChip[] chips = mRecipients.getRecipients();
        String[] addresses = new String[chips.length];

        for (int i = 0; i < chips.length; i++) {
            addresses[i] = PhoneNumberUtils.stripSeparators(chips[i].getEntry().getDestination());
        }

        return addresses;
    }

}
