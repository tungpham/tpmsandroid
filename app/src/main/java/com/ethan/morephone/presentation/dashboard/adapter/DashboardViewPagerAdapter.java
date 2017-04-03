package com.ethan.morephone.presentation.dashboard.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.ethan.morephone.presentation.voice.VoiceFragment;

/**
 * Created by Ethan on 3/4/17.
 */

public class DashboardViewPagerAdapter extends FragmentPagerAdapter {

    private final String mPhoneNumber;

    public DashboardViewPagerAdapter(FragmentManager fm, String phoneNumber) {
        super(fm);
        mPhoneNumber = phoneNumber;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString(IncomingPhoneNumbersFragment.BUNDLE_PHONE_NUMBER, mPhoneNumber);
                return ConversationsFragment.getInstance(bundle);
            case 1:
                return VoiceFragment.getInstance(mPhoneNumber);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
