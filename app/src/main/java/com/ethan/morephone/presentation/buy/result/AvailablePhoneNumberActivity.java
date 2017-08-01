package com.ethan.morephone.presentation.buy.result;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberFragment;
import com.ethan.morephone.presentation.buy.payment.purchase.PurchaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 3/31/17.
 */

public class AvailablePhoneNumberActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_number);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);

        Bundle bundle = getIntent().getExtras();
        String countryName = bundle.getString(SearchPhoneNumberFragment.BUNDLE_COUNTRY_NAME);

        setTitleActionBar(mToolbar, countryName);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof SearchPhoneNumberFragment) return;
        AvailablePhoneNumberFragment browserFragment = AvailablePhoneNumberFragment.getInstance(bundle);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                AvailablePhoneNumberFragment.class.getSimpleName());
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PurchaseActivity.REQUEST_PURCHASE_ACTIVITY && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();
        }
    }

}
