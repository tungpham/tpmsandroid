package com.ethan.morephone.presentation.message.list;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.morephone.data.entity.conversation.ConversationModel;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.payment.purchase.PurchaseActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.utils.ActivityUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Ethan on 2/16/17.
 */

public class MessageListActivity extends BaseActivity {

    public static void starter(Activity activity, String phoneNumber, String phoneNumberId, String body, ConversationModel conversationModel) {
        Intent intent = new Intent(activity, MessageListActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER, phoneNumber);
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, phoneNumberId);
        bundle.putString(MessageListFragment.BUNDLE_MESSAGE_BODY, body);
        intent.putExtras(bundle);
        EventBus.getDefault().postSticky(conversationModel);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof MessageListFragment) return;
        MessageListFragment browserFragment = MessageListFragment.getInstance(getIntent().getExtras());
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                MessageListFragment.class.getSimpleName());
    }

}
