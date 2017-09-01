package com.ethan.morephone.presentation.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.adapter.DashboardViewPagerAdapter;
import com.ethan.morephone.widget.PagerSlidingTabStrip;

/**
 * Created by Ethan on 4/12/17.
 */

public class DashboardFrag extends BaseFragment {

    public static final int BUNDLE_FRAGMENT_MESSAGE = 0;
    public static final int BUNDLE_FRAGMENT_RECORD = 1;
    public static final int BUNDLE_FRAGMENT_CALL_LOGS = 2;
    public static final int BUNDLE_FRAGMENT_DIAL = 3;

    public static final int REQUEST_COMPOSE = 100;

    public static DashboardFrag getInstance(Bundle bundle) {
        DashboardFrag dashboardFragment = new DashboardFrag();
        dashboardFragment.setArguments(bundle);
        return dashboardFragment;
    }

    private String mPhoneNumber;
    private ViewPager mViewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        mPhoneNumber = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);

        int mode = getArguments().getInt(DashboardActivity.BUNDLE_FRAGMENT_MODE);

        setUpViewPager(view, mode);

        return view;
    }

    private void setUpViewPager(View view, int mode) {
        PagerSlidingTabStrip navigationTabStrip = (PagerSlidingTabStrip) view.findViewById(R.id.tab_strip);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setOffscreenPageLimit(4);
        DashboardViewPagerAdapter myViewPagerAdapter = new DashboardViewPagerAdapter(getContext(), getChildFragmentManager(), mPhoneNumber);
        mViewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(mViewPager);

        mViewPager.setCurrentItem(mode);
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == REQUEST_SETTING && requestCode == Activity.RESULT_OK){
//            getActivity().setResult(Activity.RESULT_OK);
//            getActivity().finish();
//        }
//    }

}
