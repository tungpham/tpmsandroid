package com.ethan.morephone.presentation.buy.payment.fund;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 5/10/17.
 */

public class AddFundActivity extends BaseActivity {

    public static final String EXTRA_BALANCE_ADD = "EXTRA_BALANCE_ADD";

    private AddFundFrag mAddFundFrag;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setTitleActionBar(mToolbar, getString(R.string.add_fund_label));

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof AddFundFrag) return;
        mAddFundFrag = AddFundFrag.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                mAddFundFrag,
                R.id.content_frame,
                AddFundFrag.class.getSimpleName());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                setResult();
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        setResult();

        super.onBackPressed();
    }

    private void setResult(){
        double balance = mAddFundFrag.getBalance();
        Intent intent = new Intent();
        intent.putExtra(EXTRA_BALANCE_ADD, balance);
        setResult(RESULT_OK, intent);
        DebugTool.logD("BACK PRESS: " + balance);
    }
}
