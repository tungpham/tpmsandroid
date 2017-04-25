package com.ethan.morephone.presentation.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.utils.Injection;

/**
 * Created by Ethan on 4/20/17.
 */

public class SettingFragment extends BaseFragment implements
        SettingContract.View,
        View.OnClickListener,
        ChangeFriendlyNameDialog.ChangeFriendlyNameListener {


    public static SettingFragment getInstance() {
        return new SettingFragment();
    }

    private SettingContract.Presenter mPresenter;
    private TextView mTextFriendlyName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SettingPresenter(this, Injection.providerUseCaseHandler(), Injection.providerChangeFriendlyName(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);
        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity activity = (BaseActivity) getActivity();
        activity.enableHomeActionBar(toolbar, MyPreference.getPhoneNumber(getContext()) + getString(R.string.setting_label));

        TextView textPhoneNumber = (TextView) view.findViewById(R.id.text_setting_phone_number);
        textPhoneNumber.setText(MyPreference.getPhoneNumber(getContext()));

        mTextFriendlyName = (TextView) view.findViewById(R.id.text_setting_friendly_name_set);
        mTextFriendlyName.setText(MyPreference.getFriendlyName(getContext()));

        view.findViewById(R.id.relative_setting_friendly_name).setOnClickListener(this);

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
            default:
                break;
        }
    }

    @Override
    public void changeFriendlyName(String friendlyName) {
        mPresenter.changeFriendlyName(MyPreference.getPhoneNumberSid(getContext()), friendlyName);
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
}
