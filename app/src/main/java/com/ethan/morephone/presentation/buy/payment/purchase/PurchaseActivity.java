package com.ethan.morephone.presentation.buy.payment.purchase;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 5/4/17.
 */

public class PurchaseActivity extends BaseActivity {

    public static final String BUNDLE_FRIENDLY_PHONE_NUMBER = "BUNDLE_FRIENDLY_PHONE_NUMBER";
    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    public static final String BUNDLE_PRICE = "BUNDLE_PRICE";
    public static final String BUNDLE_IS_VOICE = "BUNDLE_IS_VOICE";
    public static final String BUNDLE_IS_SMS = "BUNDLE_IS_SMS";
    public static final String BUNDLE_IS_MMS = "BUNDLE_IS_MMS";
    public static final String BUNDLE_IS_FAX = "BUNDLE_IS_FAX";
    public static final String BUNDLE_IS_POOL = "BUNDLE_IS_POOL";

    public static final int REQUEST_PURCHASE_ACTIVITY = 100;

    public static void starter(Activity activity, String friendlyPhoneNumber, String phoneNumber, String price, boolean isVoice, boolean isSms, boolean isMms, boolean isFax, boolean isPool) {
        Intent intent = new Intent(activity, PurchaseActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_FRIENDLY_PHONE_NUMBER, friendlyPhoneNumber);
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        bundle.putString(BUNDLE_PRICE, price);
        bundle.putBoolean(BUNDLE_IS_VOICE, isVoice);
        bundle.putBoolean(BUNDLE_IS_SMS, isSms);
        bundle.putBoolean(BUNDLE_IS_MMS, isMms);
        bundle.putBoolean(BUNDLE_IS_FAX, isFax);
        bundle.putBoolean(BUNDLE_IS_POOL, isPool);
        intent.putExtras(bundle);
        activity.startActivityForResult(intent, REQUEST_PURCHASE_ACTIVITY);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setTitleActionBar(mToolbar, getString(R.string.purchase_label));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof PurchaseFragment) return;
        PurchaseFragment browserFragment = PurchaseFragment.getInstance(getIntent().getExtras());
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                PurchaseFragment.class.getSimpleName());
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
