package com.ethan.morephone.presentation.license;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.webkit.WebView;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;


/**
 * Created by Ethan on 12/28/16.
 */

public class LicenseActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_license);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableHomeActionBar(toolbar, getString(R.string.license_label));

        WebView webView = (WebView) findViewById(R.id.web_view);
        webView.loadUrl("file:///android_asset/license.html");
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
