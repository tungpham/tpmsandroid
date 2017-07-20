package com.ethan.morephone.presentation.buy.payment.fund;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 5/10/17.
 */

public class AddFundActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableHomeActionBar(mToolbar, getString(R.string.add_fund_label));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof AddFundFrag) return;
        AddFundFrag browserFragment = AddFundFrag.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                AddFundFragment.class.getSimpleName());
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
