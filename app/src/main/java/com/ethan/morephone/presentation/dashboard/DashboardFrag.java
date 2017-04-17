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

    public static final String BUNDLE_CHOOSE_VOICE = "BUNDLE_CHOOSE_VOICE";

    public static DashboardFrag getInstance(String phoneNumber, boolean isVoice) {
        DashboardFrag dashboardFragment = new DashboardFrag();
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
        bundle.putBoolean(BUNDLE_CHOOSE_VOICE, isVoice);
        dashboardFragment.setArguments(bundle);
        return dashboardFragment;
    }

    private String mPhoneNumber;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mPhoneNumber = getArguments().getString(BUNDLE_PHONE_NUMBER);

        boolean isVoice = getArguments().getBoolean(BUNDLE_CHOOSE_VOICE);

        setUpViewPager(view, isVoice);

        setHasOptionsMenu(true);
        return view;
    }

    private void setUpViewPager(View view, boolean isVoice) {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.tab_strip);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        DashboardViewPagerAdapter myViewPagerAdapter = new DashboardViewPagerAdapter(getChildFragmentManager(), mPhoneNumber);
        mViewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(mViewPager, 0);

        if (isVoice) {
            mViewPager.setCurrentItem(1);
        } else {
            mViewPager.setCurrentItem(0);
        }
    }
}
