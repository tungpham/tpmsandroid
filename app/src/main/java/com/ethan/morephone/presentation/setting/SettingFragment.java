package com.ethan.morephone.presentation.setting;

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

import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.utils.Injection;

/**
 * Created by Ethan on 4/20/17.
 */

public class SettingFragment extends BaseFragment implements
        SettingContract.View,
        View.OnClickListener,
        ChangeFriendlyNameDialog.ChangeFriendlyNameListener,
        CompoundButton.OnCheckedChangeListener,
        ConfigurePhoneDialog.ConfigurePhoneListener,
        ConfigureEmailDialog.ConfigureEmailListener {

    public static SettingFragment getInstance(Bundle bundle) {
        SettingFragment fragment = new SettingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private SettingContract.Presenter mPresenter;
    private TextView mTextPhoneNumber;
    private TextView mTextFriendlyName;

    private SwitchCompat mSwitchRecord;
    private SwitchCompat mSwitchNotification;
    private SwitchCompat mSwitchConfigure;

    private RelativeLayout mRelativePhone;
    private TextView mTextPhoneForward;
    private RelativeLayout mRelativeEmail;
    private TextView mTextEmailForward;

    private String mPhoneNumberId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SettingPresenter(this, Injection.providerUseCaseHandler(), Injection.providerChangeFriendlyName(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        mTextPhoneNumber = (TextView) view.findViewById(R.id.text_setting_phone_number);

        mTextFriendlyName = (TextView) view.findViewById(R.id.text_setting_friendly_name_set);

        view.findViewById(R.id.relative_setting_friendly_name).setOnClickListener(this);
//        view.findViewById(R.id.relative_setting_enable_record).setOnClickListener(this);
//        view.findViewById(R.id.relative_setting_enable_notification).setOnClickListener(this);
        view.findViewById(R.id.relative_setting_configure_sms_forwarding).setOnClickListener(this);

        mRelativePhone = (RelativeLayout) view.findViewById(R.id.relative_setting_phone);
        mRelativePhone.setOnClickListener(this);
        mRelativeEmail = (RelativeLayout) view.findViewById(R.id.relative_setting_email);
        mRelativeEmail.setOnClickListener(this);

//        mSwitchRecord = (SwitchCompat) view.findViewById(R.id.switch_setting_enable_record);
//        mSwitchRecord.setOnCheckedChangeListener(this);
//        mSwitchRecord.setChecked(MyPreference.getSettingEnableRecord(getContext()));
//
//        mSwitchNotification = (SwitchCompat) view.findViewById(R.id.switch_setting_enable_notification);
//        mSwitchNotification.setOnCheckedChangeListener(this);
//        mSwitchNotification.setChecked(MyPreference.getSettingEnableNotification(getContext()));

        mSwitchConfigure = (SwitchCompat) view.findViewById(R.id.switch_setting_configure_sms_forwarding);
        mSwitchConfigure.setOnCheckedChangeListener(this);

        mTextPhoneForward = (TextView) view.findViewById(R.id.text_setting_phone_summary);
        mTextEmailForward = (TextView) view.findViewById(R.id.text_setting_email_summary);

        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);
        mPresenter.getPhoneNumber(getContext(), mPhoneNumberId);

        setHasOptionsMenu(true);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.relative_setting_friendly_name:
                ChangeFriendlyNameDialog changeFriendlyNameDialog = ChangeFriendlyNameDialog.getInstance(mTextFriendlyName.getText().toString());
                changeFriendlyNameDialog.show(getChildFragmentManager(), ChangeFriendlyNameDialog.class.getSimpleName());
                changeFriendlyNameDialog.setChangeFriendlyNameListener(this);
                break;

//            case R.id.relative_setting_enable_record:
//                mSwitchRecord.setChecked(!mSwitchRecord.isChecked());
//                break;

//            case R.id.relative_setting_enable_notification:
//                mSwitchNotification.setChecked(!mSwitchNotification.isChecked());
//                break;

            case R.id.relative_setting_configure_sms_forwarding:
                mSwitchConfigure.setChecked(!mSwitchConfigure.isChecked());
                break;

            case R.id.relative_setting_phone:
                ConfigurePhoneDialog configurePhoneDialog = ConfigurePhoneDialog.getInstance(mTextPhoneForward.getText().toString());
                configurePhoneDialog.show(getChildFragmentManager(), ConfigurePhoneDialog.class.getSimpleName());
                configurePhoneDialog.setConfigurePhoneListener(this);
                break;

            case R.id.relative_setting_email:
                ConfigureEmailDialog configureEmailDialog = ConfigureEmailDialog.getInstance(mTextEmailForward.getText().toString());
                configureEmailDialog.show(getChildFragmentManager(), ConfigureEmailDialog.class.getSimpleName());
                configureEmailDialog.setConfigureEmailListener(this);
                break;
            default:
                break;
        }
    }

    @Override
    public void changeFriendlyName(String friendlyName) {
        mPresenter.changeFriendlyName(getContext(), MyPreference.getPhoneNumberSid(getContext()), friendlyName);
        mTextFriendlyName.setText(friendlyName);
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void updateFriendlyName(String friendlyName) {
        if (isAdded())
            MyPreference.setFriendlyName(getContext(), friendlyName);
    }

    @Override
    public void showPhoneNumber(PhoneNumber phoneNumber) {
        DebugTool.logD("SHOW PHONE");
        if (isAdded()) {
            mTextPhoneNumber.setText(phoneNumber.getPhoneNumber());
            mTextFriendlyName.setText(phoneNumber.getFriendlyName());
            mSwitchConfigure.setChecked(phoneNumber.isForward());

            showConfigure(phoneNumber.isForward());

            updateForward(phoneNumber.getForwardPhoneNumber(), phoneNumber.getForwardEmail());
        }

    }

    @Override
    public void emptyPhoneNumber() {
        DebugTool.logD("EMPTY PHONE");
    }

    @Override
    public void updateForward(String phoneNumber, String email) {
        mTextPhoneForward.setText(phoneNumber);
        mTextEmailForward.setText(email);
    }

    @Override
    public void showConfigure(boolean isEnable) {
        if (isEnable) {
            mRelativePhone.setVisibility(View.VISIBLE);
            mRelativeEmail.setVisibility(View.VISIBLE);
        } else {
            mRelativePhone.setVisibility(View.GONE);
            mRelativeEmail.setVisibility(View.GONE);
        }
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {

//            case R.id.switch_setting_enable_record:
//                MyPreference.setSettingEnableRecord(getContext(), mSwitchRecord.isChecked());
//                break;
//
//            case R.id.switch_setting_enable_notification:
//                MyPreference.setSettingEnableNotification(getContext(), mSwitchNotification.isChecked());
//                break;

            case R.id.switch_setting_configure_sms_forwarding:
                mPresenter.enableForward(getContext(), mPhoneNumberId, mSwitchConfigure.isChecked());
                break;
            default:
                break;
        }
    }

    @Override
    public void configurePhone(String phoneNumber) {
        mPresenter.settingForward(getContext(), mPhoneNumberId, phoneNumber, mTextEmailForward.getText().toString());
    }

    @Override
    public void configureEmail(String email) {
        mPresenter.settingForward(getContext(),mPhoneNumberId, mTextPhoneForward.getText().toString(), email);
    }
}
