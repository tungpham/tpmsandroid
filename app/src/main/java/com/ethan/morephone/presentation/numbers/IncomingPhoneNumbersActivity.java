package com.ethan.morephone.presentation.numbers;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 3/23/17.
 */

public class IncomingPhoneNumbersActivity extends BaseActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableHomeActionBar(toolbar, getString(R.string.my_number_label));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof IncomingPhoneNumbersFragment) return;
        IncomingPhoneNumbersFragment voiceFragment = IncomingPhoneNumbersFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                voiceFragment,
                R.id.content_frame,
                IncomingPhoneNumbersFragment.class.getSimpleName());
    }

}
