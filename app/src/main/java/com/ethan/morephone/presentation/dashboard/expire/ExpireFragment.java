package com.ethan.morephone.presentation.dashboard.expire;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.morephone.data.database.DatabaseHelpper;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.android.morephone.data.utils.DateUtils;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.setting.ChangeFriendlyNameDialog;
import com.ethan.morephone.presentation.setting.ConfigureEmailDialog;
import com.ethan.morephone.presentation.setting.ConfigurePhoneDialog;
import com.ethan.morephone.presentation.setting.SettingContract;
import com.ethan.morephone.presentation.setting.SettingPresenter;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.widget.countdown.CountDownView;

/**
 * Created by Ethan on 4/20/17.
 */

public class ExpireFragment extends BaseFragment {

    public static ExpireFragment getInstance(Bundle bundle) {
        ExpireFragment fragment = new ExpireFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_expire, container, false);
        String phoneNumber = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);
        String phoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);

        PhoneNumber phoneNumberDTO = DatabaseHelpper.findPhoneNumber(getContext(), phoneNumber);

        long duration = phoneNumberDTO.getExpire() - System.currentTimeMillis();
        DebugTool.logD("EXPIRE: " + DateUtils.formatDateExpire(phoneNumberDTO.getExpire()));

        CountDownView countDownView = (CountDownView) view.findViewById(R.id.count_down_view);
        countDownView.setInitialTime(duration); // Initial time of 30 seconds.
        countDownView.start();
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            default:
                break;
        }
        return true;
    }


}
