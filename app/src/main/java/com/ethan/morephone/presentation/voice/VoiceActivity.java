package com.ethan.morephone.presentation.voice;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 3/21/17.
 */

public class VoiceActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ConversationsFragment) return;
        VoiceFragment voiceFragment = VoiceFragment.getInstance(getIntent().getStringExtra(VoiceFragment.BUNDLE_PHONE_NUMBER));
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                voiceFragment,
                R.id.content_frame,
                VoiceFragment.class.getSimpleName());
    }

}
