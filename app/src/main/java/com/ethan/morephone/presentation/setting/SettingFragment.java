package com.ethan.morephone.presentation.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;

/**
 * Created by Ethan on 4/20/17.
 */

public class SettingFragment extends BaseFragment {


    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.enableActionBar(toolbar, getString(R.string.setting_label));

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            default:
                break;
        }
        return true;
    }
}
