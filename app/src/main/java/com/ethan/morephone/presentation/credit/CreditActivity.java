package com.ethan.morephone.presentation.credit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.usage.UsageFragment;
import com.ethan.morephone.utils.ActivityUtils;
import com.google.firebase.crash.FirebaseCrash;

/**
 * Created by Ethan on 4/20/17.
 */

public class CreditActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usage);

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof CreditFragment) return;
        CreditFragment creditFragment = CreditFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                creditFragment,
                R.id.content_frame,
                UsageFragment.class.getSimpleName());

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableHomeActionBar(toolbar, getString(R.string.credit_label));

//        findViewById(R.id.text_home).setOnClickListener(this);
//        MyTextView myTextView = (MyTextView) findViewById(R.id.text_toolbar_title);
//        myTextView.setText(getString(R.string.usage_label));
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
        DebugTool.logD("REQUEST: " + requestCode);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.text_home:
//                finish();
//                break;
            default:
                break;
        }
    }
}
