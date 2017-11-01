package com.ethan.morephone.presentation.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

import com.auth0.android.provider.WebAuthProvider;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;
//import com.facebook.FacebookSdk;


/**
 * Created by Ethan on 2/13/17.
 */

public class AuthenticationActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

//        FacebookSdk.sdkInitialize(getApplicationContext());

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof AuthenticationFragment) return;
        AuthenticationFragment browserFragment = AuthenticationFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                AuthenticationFragment.class.getSimpleName());


    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (WebAuthProvider.resume(intent)) {
            return;
        }
        super.onNewIntent(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

//            case R.id.menu_login:
//                int REQUEST_LOGIN = 100;
//                startActivityForResult(new Intent(this, LoginActivity.class), REQUEST_LOGIN);
//                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        finish();
    }


}
