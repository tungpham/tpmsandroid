package com.ethan.morephone.presentation.contact.editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.android.morephone.data.entity.contact.Contact;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberFragment;
import com.ethan.morephone.presentation.contact.ContactFragment;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactEditorActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        String phoneNumberId = getIntent().getStringExtra(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);
        Contact contact = getIntent().getParcelableExtra(ContactFragment.EXTRA_CONTACT);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof ContactEditorFragment) return;
        ContactEditorFragment contactEditorFragment = ContactEditorFragment.getInstance(phoneNumberId, contact);
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                contactEditorFragment,
                R.id.content_frame,
                ContactEditorFragment.class.getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
