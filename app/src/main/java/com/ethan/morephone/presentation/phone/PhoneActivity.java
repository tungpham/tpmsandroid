package com.ethan.morephone.presentation.phone;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.phone.adapter.PhoneViewPagerAdapter;
import com.ethan.morephone.widget.NavigationTabStrip;

/**
 * Created by Ethan on 3/21/17.
 */

public class PhoneActivity extends BaseActivity {

    public static final String EXTRA_PHONE_NUMBER = "EXTRA_PHONE_NUMBER";

    private Toolbar mToolbar;

    private String mPhoneNumber;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        mPhoneNumber = getIntent().getStringExtra(EXTRA_PHONE_NUMBER);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSubTitleActionBar(mToolbar, getString(R.string.action_bar_title_phone_label), mPhoneNumber);

        setUpViewPager();

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

    private void setUpViewPager() {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.tab_strip);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PhoneViewPagerAdapter myViewPagerAdapter = new PhoneViewPagerAdapter(getSupportFragmentManager(), mPhoneNumber);
        viewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(viewPager, 0);
    }
}
