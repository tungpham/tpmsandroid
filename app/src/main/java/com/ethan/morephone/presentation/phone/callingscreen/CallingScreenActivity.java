package com.ethan.morephone.presentation.phone.callingscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 4/8/17.
 */

public class CallingScreenActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof CallingScreenFragment) return;
        CallingScreenFragment callingScreenFragment = CallingScreenFragment.getInstance(getIntent().getExtras());
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                callingScreenFragment,
                R.id.content_frame,
                CallingScreenFragment.class.getSimpleName());

    }
}
