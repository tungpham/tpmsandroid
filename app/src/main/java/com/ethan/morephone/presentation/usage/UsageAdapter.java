package com.ethan.morephone.presentation.usage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Ethan on 3/4/17.
 */

public class UsageAdapter extends FragmentPagerAdapter {


    public UsageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
//            case 0:
//                return MessageUsageFragment.getInstance();
//            case 1:
//                return VoiceUsageFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
