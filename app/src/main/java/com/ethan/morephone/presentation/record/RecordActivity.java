package com.ethan.morephone.presentation.record;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 3/21/17.
 */

public class RecordActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ConversationsFragment) return;
        RecordFragment recordFragment = RecordFragment.getInstance(getIntent().getStringExtra(DashboardActivity.BUNDLE_PHONE_NUMBER), getIntent().getStringExtra(DashboardActivity.BUNDLE_PHONE_NUMBER_ID));
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                recordFragment,
                R.id.content_frame,
                RecordFragment.class.getSimpleName());
    }

}
