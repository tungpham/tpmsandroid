package com.ethan.morephone.presentation.buy.result;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.buy.BuyNumberFragment;
import com.ethan.morephone.utils.ActivityUtils;

/**
 * Created by Ethan on 3/31/17.
 */

public class ResultSearchNumberActivity extends BaseActivity {




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_number);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setTitleActionBar(mToolbar, "Result");

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_frame);
        if (fragment instanceof BuyNumberFragment) return;
        ResultSearchNumberFragment browserFragment = ResultSearchNumberFragment.getInstance();
        ActivityUtils.replaceFragmentToActivity(
                getSupportFragmentManager(),
                browserFragment,
                R.id.content_frame,
                ResultSearchNumberFragment.class.getSimpleName());
    }

}
