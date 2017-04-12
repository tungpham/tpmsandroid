package com.ethan.morephone.presentation.dashboard;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.adapter.DashboardViewPagerAdapter;
import com.ethan.morephone.widget.NavigationTabStrip;

/**
 * Created by Ethan on 4/12/17.
 */

public class DashboardFrag extends BaseFragment {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static DashboardFrag getInstance(String phoneNumber) {
        DashboardFrag dashboardFragment = new DashboardFrag();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        dashboardFragment.setArguments(bundle);
        return dashboardFragment;
    }

    private String mPhoneNumber;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);
        setUpViewPager(view);
        setHasOptionsMenu(true);
        return view;
    }

    private void setUpViewPager(View view) {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.tab_strip);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        DashboardViewPagerAdapter myViewPagerAdapter = new DashboardViewPagerAdapter(getChildFragmentManager(), mPhoneNumber);
        viewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(viewPager, 0);
    }
}
