package com.ethan.morephone.presentation.dashboard.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.contact.ContactFragment;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.presentation.numbers.IncomingPhoneNumbersFragment;
import com.ethan.morephone.presentation.phone.dial.DialFragment;
import com.ethan.morephone.presentation.phone.log.CallLogFragment;
import com.ethan.morephone.presentation.record.RecordFragment;

/**
 * Created by Ethan on 3/4/17.
 */

public class DashboardViewPagerAdapter extends FragmentPagerAdapter {

    private final String mPhoneNumber;
    private final Context mContext;

    public DashboardViewPagerAdapter(Context context, FragmentManager fm, String phoneNumber) {
        super(fm);
        mPhoneNumber = phoneNumber;
        this.mContext = context;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getStringArray(R.array.titles)[position];
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString(IncomingPhoneNumbersFragment.BUNDLE_PHONE_NUMBER, mPhoneNumber);
                return ConversationsFragment.getInstance(bundle);
            case 1:
                return RecordFragment.getInstance(mPhoneNumber);
            case 2:
                return CallLogFragment.getInstance(mPhoneNumber);
            case 3:
                return DialFragment.getInstance(mPhoneNumber, "");
            case 4:
                return ContactFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
