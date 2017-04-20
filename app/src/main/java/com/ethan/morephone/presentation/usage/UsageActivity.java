package com.ethan.morephone.presentation.usage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;

/**
 * Created by Ethan on 4/20/17.
 */

public class UsageActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_usage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, getString(R.string.usage_label));
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
