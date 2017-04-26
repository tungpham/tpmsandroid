package com.ethan.morephone.presentation.usage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.morephone.data.entity.usage.Usage;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.widget.NavigationTabStrip;

/**
 * Created by Ethan on 4/22/17.
 */

public class UsageFragment extends BaseFragment implements View.OnClickListener, UsageContract.View {

    public static UsageFragment getInstance() {
        return new UsageFragment();
    }

    private ViewPager mViewPager;

    private TextView mTextMessageTotal;
    private TextView mTextMessageSpent;
    private TextView mTextMessageIncoming;
    private TextView mTextMessageOutgoing;

    private TextView mTextVoiceTotal;
    private TextView mTextVoiceSpent;
    private TextView mTextVoiceIncoming;
    private TextView mTextVoiceOutgoing;
    private TextView mTextVoiceMissing;

    private UsageContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UsagePresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usage, container, false);

        TextView textPhoneNumber = (TextView) view.findViewById(R.id.text_usage_phone_number);
        textPhoneNumber.setText(MyPreference.getPhoneNumber(getContext()));

        mTextMessageTotal = (TextView) view.findViewById(R.id.text_usage_message_total_set);
        mTextMessageSpent = (TextView) view.findViewById(R.id.text_usage_message_total_spent);
        mTextMessageIncoming = (TextView) view.findViewById(R.id.text_usage_message_incoming_set);
        mTextMessageOutgoing = (TextView) view.findViewById(R.id.text_usage_message_outgoing_set);

        mTextVoiceTotal = (TextView) view.findViewById(R.id.text_usage_voice_total_set);
        mTextVoiceSpent = (TextView) view.findViewById(R.id.text_usage_voice_total_spent);
        mTextVoiceIncoming = (TextView) view.findViewById(R.id.text_usage_voice_incoming_set);
        mTextVoiceOutgoing = (TextView) view.findViewById(R.id.text_usage_voice_outgoing_set);
        mTextVoiceMissing = (TextView) view.findViewById(R.id.text_usage_voice_missing_set);

        mPresenter.loadUsage(getContext());
        return view;
    }


    private void setUpViewPager(View view) {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) view.findViewById(R.id.tab_strip);
        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        UsageAdapter usageAdapter = new UsageAdapter(getChildFragmentManager());
        mViewPager.setAdapter(usageAdapter);
        navigationTabStrip.setViewPager(mViewPager, 0);
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void showLoading(boolean isActive) {
        if(isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showUsage(Usage usage) {
        mTextMessageTotal.setText(String.valueOf(usage.usageMessageTotal));
        mTextMessageSpent.setText(usage.usageMessageSpent);
        mTextMessageIncoming.setText(String.valueOf(usage.usageMessageIncoming));
        mTextMessageOutgoing.setText(String.valueOf(usage.usageMessageOutgoing));

        mTextVoiceTotal.setText(String.valueOf(usage.usageVoiceTotal));
        mTextVoiceSpent.setText(usage.usageVoiceSpent);
        mTextVoiceIncoming.setText(String.valueOf(usage.usageVoiceIncoming));
        mTextVoiceOutgoing.setText(String.valueOf(usage.usageVoiceOutgoing));
        mTextVoiceMissing.setText(String.valueOf(usage.usageVoiceMissing));
    }

    @Override
    public void setPresenter(UsageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
