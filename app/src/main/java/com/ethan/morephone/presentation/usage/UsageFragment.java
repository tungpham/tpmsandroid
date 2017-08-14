package com.ethan.morephone.presentation.usage;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.morephone.data.entity.usage.UsageItem;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.widget.NavigationTabStrip;

/**
 * Created by Ethan on 4/22/17.
 */

public class UsageFragment extends BaseFragment implements View.OnClickListener, UsageContract.View {

    public static UsageFragment getInstance() {
        return new UsageFragment();
    }

    public static final String CATEGORY_SMS = "sms";
    public static final String CATEGORY_SMS_INBOUND = "sms-inbound";
    public static final String CATEGORY_SMS_OUTBOUND = "sms-outbound";

    public static final String CATEGORY_CALL = "calls";
    public static final String CATEGORY_CALL_INBOUND = "calls-inbound";
    public static final String CATEGORY_CALL_OUTBOUND = "calls-outbound";

    private ViewPager mViewPager;

    private TextView mTextBalance;
    private TextView mTextMessageIncoming;
    private TextView mTextMessageOutgoing;

    private TextView mTextVoiceIncoming;
    private TextView mTextVoiceOutgoing;

    private int mPage = 0;
    private String mPageToken = "";

    private int mTotalCalls = 0;
    private int mInboundCalls = 0;
    private int mTotalMessage = 0;
    private int mInboundMessage = 0;

    private UsageContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new UsagePresenter(this, Injection.providerUseCaseHandler(), Injection.providerGetUsageAllTime(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_usage, container, false);

        mTextBalance = (TextView) view.findViewById(R.id.text_usage_balance);

        mTextMessageIncoming = (TextView) view.findViewById(R.id.text_usage_message_incoming_set);
        mTextMessageOutgoing = (TextView) view.findViewById(R.id.text_usage_message_outgoing_set);

        mTextVoiceIncoming = (TextView) view.findViewById(R.id.text_usage_voice_incoming_set);
        mTextVoiceOutgoing = (TextView) view.findViewById(R.id.text_usage_voice_outgoing_set);

        mPresenter.loadUsage(getContext(), mPage, mPageToken);
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
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showUsage(UsageItem usage) {

        mTextBalance.setText(getString(R.string.usage_balance) + ": $ " + (double)Math.round(usage.getBalance() * 100) / 100);
        mTextMessageIncoming.setText(String.valueOf(usage.getMessageIncoming()));
        mTextMessageOutgoing.setText(String.valueOf(usage.getMessageOutgoing()));
        mTextVoiceIncoming.setText(String.valueOf(usage.getCallIncoming()));
        mTextVoiceOutgoing.setText(String.valueOf(usage.getCallOutgoing()));

//        if (!TextUtils.isEmpty(usage.nextPageUri)) {
//            String pageToken = Utils.getPageToken(usage.nextPageUri);
//            mPresenter.loadUsage(getContext(), mPage + 1, pageToken);
//        }




//        List<UsageRecord> usageRecords = usage.usageRecords;
//
//        if (usageRecords != null && !usageRecords.isEmpty()) {
//            for (UsageRecord usageRecord : usageRecords) {
//                if (usageRecord.category.equals(CATEGORY_SMS)) {
//                    mTextMessageSpent.setText(usageRecord.price + usageRecord.priceUnit);
//                    mTextMessageTotal.setText(usageRecord.count + " " + usageRecord.countUnit);
//                    mTotalMessage = Integer.parseInt(usageRecord.count);
//                    if(mInboundMessage > 0){
//                        int outboundMessage = mTotalMessage - mInboundMessage;
//                        mTextMessageOutgoing.setText(String.valueOf(outboundMessage));
//                    }
//
//                }
//
//                if (usageRecord.category.equals(CATEGORY_SMS_INBOUND)) {
//                    mTextMessageIncoming.setText(usageRecord.count + " " + usageRecord.countUnit);
//                    mTextMessageOutgoing.setText(usageRecord.count + " " + usageRecord.countUnit);
//                    mInboundMessage = Integer.parseInt(usageRecord.count);
//                    if(mTotalMessage > 0){
//                        int outboundMessage = mTotalMessage - mInboundMessage;
//                        mTextMessageOutgoing.setText(String.valueOf(outboundMessage));
//                    }
//                }
//
//                if (usageRecord.category.equals(CATEGORY_CALL)) {
//                    mTextVoiceTotal.setText(usageRecord.count + " " + usageRecord.countUnit);
//                    mTextVoiceSpent.setText(usageRecord.priceUnit + " " + usageRecord.price);
//
//                    mTotalCalls = Integer.parseInt(usageRecord.count);
//                    if(mInboundCalls > 0){
//                        int outboundMessage = mTotalCalls - mInboundCalls;
//                        mTextVoiceOutgoing.setText(String.valueOf(outboundMessage));
//                    }
//                }
//
//                if (usageRecord.category.equals(CATEGORY_CALL_INBOUND)) {
//                    mTextVoiceIncoming.setText(usageRecord.count + " " + usageRecord.countUnit);
//
//                    mInboundCalls = Integer.parseInt(usageRecord.count);
//                    if(mTotalCalls > 0){
//                        int outboundMessage = mTotalCalls - mInboundCalls;
//                        mTextVoiceOutgoing.setText(String.valueOf(outboundMessage));
//                    }
//                }
//
//
//            }
//        }

    }

    @Override
    public void setPresenter(UsageContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
