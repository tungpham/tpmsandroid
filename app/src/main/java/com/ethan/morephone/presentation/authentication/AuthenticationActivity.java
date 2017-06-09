package com.ethan.morephone.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.main.MainActivity;
import com.ethan.morephone.utils.ActivityUtils;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.stormpath.sdk.Stormpath;
import com.stormpath.sdk.StormpathCallback;
import com.stormpath.sdk.models.Account;
import com.stormpath.sdk.models.StormpathError;


/**
 * Created by Ethan on 2/13/17.
 */

public class AuthenticationActivity extends BaseActivity {

    public static final String EXTRA_LINK = "EXTRA_LINK";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof AuthenticationFragment) return;
        AuthenticationFragment browserFragment = AuthenticationFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                AuthenticationFragment.class.getSimpleName());

        Stormpath.getAccount(new StormpathCallback<Account>() {
            @Override
            public void onSuccess(Account account) {
                DebugTool.logD("Account: " + account.getEmail());
                startActivity(new Intent(AuthenticationActivity.this, MainActivity.class));
            }

            @Override
            public void onFailure(StormpathError error) {
                DebugTool.logD("error: " + error.message());
            }
        });

    }
}
