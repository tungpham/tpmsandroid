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

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
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
        ConfigureEmailDialog.ConfigureEmailListener{


    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    private SettingContract.Presenter mPresenter;
    private TextView mTextFriendlyName;

    private SwitchCompat mSwitchRecord;
    private SwitchCompat mSwitchNotification;
    private SwitchCompat mSwitchConfigure;

    private RelativeLayout mRelativePhone;
    private TextView mTextPhone;
    private RelativeLayout mRelativeEmail;
    private TextView mTextEmail;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SettingPresenter(this, Injection.providerUseCaseHandler(), Injection.providerChangeFriendlyName(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        TextView textPhoneNumber = (TextView) view.findViewById(R.id.text_setting_phone_number);
        textPhoneNumber.setText(MyPreference.getPhoneNumber(getContext()));

        mTextFriendlyName = (TextView) view.findViewById(R.id.text_setting_friendly_name_set);
        mTextFriendlyName.setText(MyPreference.getFriendlyName(getContext()));

        view.findViewById(R.id.relative_setting_friendly_name).setOnClickListener(this);
        view.findViewById(R.id.relative_setting_enable_record).setOnClickListener(this);
        view.findViewById(R.id.relative_setting_enable_notification).setOnClickListener(this);
        view.findViewById(R.id.relative_setting_configure_sms_forwarding).setOnClickListener(this);

        mRelativePhone = (RelativeLayout) view.findViewById(R.id.relative_setting_phone);
        mRelativePhone.setOnClickListener(this);
        mRelativeEmail = (RelativeLayout) view.findViewById(R.id.relative_setting_email);
        mRelativeEmail.setOnClickListener(this);

        mSwitchRecord = (SwitchCompat) view.findViewById(R.id.switch_setting_enable_record);
        mSwitchRecord.setOnCheckedChangeListener(this);
        mSwitchRecord.setChecked(MyPreference.getSettingEnableRecord(getContext()));

        mSwitchNotification = (SwitchCompat) view.findViewById(R.id.switch_setting_enable_notification);
        mSwitchNotification.setOnCheckedChangeListener(this);
        mSwitchNotification.setChecked(MyPreference.getSettingEnableNotification(getContext()));

        mSwitchConfigure = (SwitchCompat) view.findViewById(R.id.switch_setting_configure_sms_forwarding);
        mSwitchConfigure.setOnCheckedChangeListener(this);
        mSwitchConfigure.setChecked(MyPreference.getSettingConfigure(getContext()));



        mTextPhone = (TextView) view.findViewById(R.id.text_setting_phone_summary);
        mTextEmail = (TextView) view.findViewById(R.id.text_setting_email_summary);

        visibleConfigure();

        loadData();

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
                ChangeFriendlyNameDialog changeFriendlyNameDialog = ChangeFriendlyNameDialog.getInstance();
                changeFriendlyNameDialog.show(getChildFragmentManager(), ChangeFriendlyNameDialog.class.getSimpleName());
                changeFriendlyNameDialog.setChangeFriendlyNameListener(this);
                break;

            case R.id.relative_setting_enable_record:
                mSwitchRecord.setChecked(!mSwitchRecord.isChecked());
                break;

            case R.id.relative_setting_enable_notification:
                mSwitchNotification.setChecked(!mSwitchNotification.isChecked());
                break;

            case R.id.relative_setting_configure_sms_forwarding:
                mSwitchConfigure.setChecked(!mSwitchConfigure.isChecked());
                break;

            case R.id.relative_setting_phone:
                ConfigurePhoneDialog configurePhoneDialog = ConfigurePhoneDialog.getInstance();
                configurePhoneDialog.show(getChildFragmentManager(), ConfigurePhoneDialog.class.getSimpleName());
                configurePhoneDialog.setConfigurePhoneListener(this);
                break;

            case R.id.relative_setting_email:
                ConfigureEmailDialog configureEmailDialog = ConfigureEmailDialog.getInstance();
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
    public void setPresenter(SettingContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        switch (compoundButton.getId()) {

            case R.id.switch_setting_enable_record:
                MyPreference.setSettingEnableRecord(getContext(), mSwitchRecord.isChecked());
                break;

            case R.id.switch_setting_enable_notification:
                MyPreference.setSettingEnableNotification(getContext(), mSwitchNotification.isChecked());
                break;

            case R.id.switch_setting_configure_sms_forwarding:
                MyPreference.setSettingConfigure(getContext(), mSwitchConfigure.isChecked());
                visibleConfigure();
                break;
            default:
                break;
        }
    }

    private void visibleConfigure() {
        if (MyPreference.getSettingConfigure(getContext())) {
            mRelativePhone.setVisibility(View.VISIBLE);
            mRelativeEmail.setVisibility(View.VISIBLE);
        } else {
            mRelativePhone.setVisibility(View.GONE);
            mRelativeEmail.setVisibility(View.GONE);
        }
    }

    private void loadData(){
        mTextPhone.setText(MyPreference.getSettingConfigurePhone(getContext()));
        mTextEmail.setText(MyPreference.getSettingConfigureEmail(getContext()));
    }

    @Override
    public void configurePhone(String phoneNumber) {
        MyPreference.setSettingConfigurePhone(getContext(), phoneNumber);
        loadData();
    }

    @Override
    public void configureEmail(String phoneNumber) {
        MyPreference.setSettingConfigureEmail(getContext(), phoneNumber);
        loadData();
    }
}
