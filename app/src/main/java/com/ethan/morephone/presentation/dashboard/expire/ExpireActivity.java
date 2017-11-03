package com.ethan.morephone.presentation.dashboard.expire;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.utils.ActivityUtils;


/**
 * Created by truongnguyen on 8/30/17.
 */

public class ExpireActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Bundle bundle = getIntent().getExtras();
        String mPhoneNumber = bundle.getString(DashboardActivity.BUNDLE_PHONE_NUMBER);
//        String mPhoneNumberId = bundle.getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setTitleActionBar(toolbar, mPhoneNumber);


        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ExpireFragment) return;
        ExpireFragment expireFragment = ExpireFragment.getInstance(getIntent().getExtras());
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                expireFragment,
                R.id.content_frame,
                ExpireFragment.class.getSimpleName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return true;
    }
}
