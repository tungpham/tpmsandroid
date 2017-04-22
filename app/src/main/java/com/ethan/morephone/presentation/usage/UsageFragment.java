package com.ethan.morephone.presentation.usage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.widget.NavigationTabStrip;

/**
 * Created by Ethan on 4/22/17.
 */

public class UsageFragment extends BaseFragment {

    public static UsageFragment getInstance() {
        return new UsageFragment();
    }

    private ViewPager mViewPager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usage, container, false);

        setUpViewPager(view);

        return view;
    }


    private void setUpViewPager(View view) {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.tab_strip);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        UsageAdapter usageAdapter = new UsageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(usageAdapter);
        navigationTabStrip.setViewPager(mViewPager, 0);
    }

}
