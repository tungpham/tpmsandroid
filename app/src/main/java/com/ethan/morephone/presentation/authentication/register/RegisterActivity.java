package com.ethan.morephone.presentation.authentication.register;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;

/**
 * Created by Ethan on 2/23/17.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_register);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(toolbar, false);

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
    public void onClick(View v) {

    }
}
