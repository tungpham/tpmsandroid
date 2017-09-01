package com.ethan.morephone.presentation.setting;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 2/22/17.
 */

public class SettingActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setTitleActionBar(toolbar, getString(R.string.setting_label));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof SettingFragment) return;
        SettingFragment composeFragment = SettingFragment.getInstance(getIntent().getExtras());
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                composeFragment,
                R.id.content_frame,
                SettingFragment.class.getSimpleName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
