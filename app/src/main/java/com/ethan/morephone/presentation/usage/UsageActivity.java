package com.ethan.morephone.presentation.usage;

import android.os.Bundle;
import android.support.annotation.Nullable;

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
    }
}
