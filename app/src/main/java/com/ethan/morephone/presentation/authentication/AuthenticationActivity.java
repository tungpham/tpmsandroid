package com.ethan.morephone.presentation.authentication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;


/**
 * Created by Ethan on 2/13/17.
 */

public class AuthenticationActivity extends BaseActivity {

    public static final String EXTRA_LINK = "EXTRA_LINK";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof AuthenticationFragment) return;
        AuthenticationFragment browserFragment = AuthenticationFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                AuthenticationFragment.class.getSimpleName());
    }
}
