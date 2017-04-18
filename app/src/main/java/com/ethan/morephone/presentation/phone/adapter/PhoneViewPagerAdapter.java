package com.ethan.morephone.presentation.phone.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ethan.morephone.presentation.phone.dial.DialFragment;
import com.ethan.morephone.presentation.phone.log.CallLogFragment;

/**
 * Created by Ethan on 3/4/17.
 */

public class PhoneViewPagerAdapter extends FragmentPagerAdapter {

    private final String mPhoneNumber;

    public PhoneViewPagerAdapter(FragmentManager fm, String phoneNumber) {
        super(fm);
        mPhoneNumber = phoneNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return DialFragment.getInstance(mPhoneNumber, "");
            case 1:

                return CallLogFragment.getInstance(mPhoneNumber);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
