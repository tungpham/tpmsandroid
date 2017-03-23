package com.ethan.morephone.presentation.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 3/23/17.
 */

public class DashboardActivity extends BaseActivity{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ConversationsFragment) return;
        DashboardFragment voiceFragment = DashboardFragment.getInstance(getIntent().getStringExtra(DashboardFragment.BUNDLE_PHONE_NUMBER));
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                voiceFragment,
                R.id.content_frame,
                DashboardFragment.class.getSimpleName());
    }

}
