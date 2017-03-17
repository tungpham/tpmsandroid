package com.ethan.morephone.presentation.main;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ethan.morephone.presentation.message.conversation.ConversationsFragment;
import com.ethan.morephone.presentation.voice.VoiceFragment;

/**
 * Created by Ethan on 3/4/17.
 */

public class MyViewPagerAdapter extends FragmentPagerAdapter {

    public MyViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:

                return ConversationsFragment.getInstance(null);
            case 1:

                return VoiceFragment.getInstance();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
