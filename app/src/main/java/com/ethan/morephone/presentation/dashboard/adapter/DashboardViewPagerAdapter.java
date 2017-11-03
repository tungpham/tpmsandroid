package com.ethan.morephone.presentation.dashboard.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ethan.morephone.R;
import com.ethan.morephone.presentation.contact.ContactFragment;
import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.presentation.phone.dial.DialFragment;
import com.ethan.morephone.presentation.phone.log.CallLogFragment;
import com.ethan.morephone.presentation.record.RecordFragment;

/**
 * Created by Ethan on 3/4/17.
 */

public class DashboardViewPagerAdapter extends FragmentPagerAdapter {

    private final String mPhoneNumber;
    private final String mPhoneNumbeId;
    private final Context mContext;

    public DashboardViewPagerAdapter(Context context, FragmentManager fm, String phoneNumber, String phoneNumberId) {
        super(fm);
        mPhoneNumber = phoneNumber;
        mPhoneNumbeId = phoneNumberId;
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
                return ConversationsFragment.getInstance(mPhoneNumber, mPhoneNumbeId);
            case 1:
                return RecordFragment.getInstance(mPhoneNumber, mPhoneNumbeId);
            case 2:
                return CallLogFragment.getInstance(mPhoneNumber, mPhoneNumbeId);
            case 3:
                return DialFragment.getInstance(mPhoneNumber, "");
            case 4:
                return ContactFragment.getInstance(mPhoneNumbeId, mPhoneNumber);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
