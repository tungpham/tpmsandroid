package com.ethan.morephone.presentation.buy;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.android.morephone.data.entity.phonenumbers.AvailableCountry;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.adapter.CountryAdapter;
import com.ethan.morephone.presentation.buy.result.AvailablePhoneNumberActivity;
import com.ethan.morephone.utils.Injection;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/30/17.
 */

public class SearchPhoneNumberFragment extends BaseFragment implements
        View.OnClickListener,
        AdapterView.OnItemSelectedListener,
        SearchPhoneNumberContract.View {

    public static final String BUNDLE_COUNTRY_NAME = "BUNDLE_COUNTRY_NAME";
    public static final String BUNDLE_COUNTRY_CODE = "BUNDLE_COUNTRY_CODE";
    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";
    public static final String BUNDLE_SMS_ENABLE = "BUNDLE_SMS_ENABLE";
    public static final String BUNDLE_MMS_ENABLE = "BUNDLE_MMS_ENABLE";
    public static final String BUNDLE_VOICE_ENABLE = "BUNDLE_VOICE_ENABLE";

    private final int REQUEST_AVAILABLE_PHONE_NUMBER = 100;


    public static SearchPhoneNumberFragment getInstance() {
        return new SearchPhoneNumberFragment();
    }

    private AppCompatSpinner mSpinnerCountry;
    private CountryAdapter mCountryAdapter;

    private SearchPhoneNumberContract.Presenter mPresenter;

    private AppCompatEditText mEditTextPhoneNumber;
    private SwitchCompat mSwitchSms;
    private SwitchCompat mSwitchMms;
    private SwitchCompat mSwitchVoice;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new SearchPhoneNumberPresenter(this, Injection.providerUseCaseHandler(), Injection.providerAvailableCountries(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_phone_number, container, false);

        mSpinnerCountry = (AppCompatSpinner) view.findViewById(R.id.spinner_search_phone_number_country);
        mCountryAdapter = new CountryAdapter(getContext(), new ArrayList<AvailableCountry>(), R.layout.item_country_name);
        mSpinnerCountry.setAdapter(mCountryAdapter);
        mCountryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpinnerCountry.setOnItemSelectedListener(this);

        mEditTextPhoneNumber = (AppCompatEditText) view.findViewById(R.id.edit_text_search_phone_number_number);
        mSwitchSms = (SwitchCompat) view.findViewById(R.id.switch_search_phone_number_sms);
        mSwitchMms = (SwitchCompat) view.findViewById(R.id.switch_search_phone_number_mms);
        mSwitchVoice = (SwitchCompat) view.findViewById(R.id.switch_search_phone_number_voice);

        view.findViewById(R.id.button_buy_number_search).setOnClickListener(this);

        mPresenter.loadAvailableCountries(getContext());
        return view;
    }


    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.button_buy_number_search:

                String countryName = ((AvailableCountry) mSpinnerCountry.getSelectedItem()).country;
                String countryCode = ((AvailableCountry) mSpinnerCountry.getSelectedItem()).countryCode;
                String phoneNumber = mEditTextPhoneNumber.getText().toString();
                boolean smsEnabled = mSwitchSms.isChecked();
                boolean mmsEnabled = mSwitchMms.isChecked();
                boolean voiceEnabled = mSwitchVoice.isChecked();

                Intent intent = new Intent(getActivity(), AvailablePhoneNumberActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(BUNDLE_COUNTRY_NAME, countryName);
                bundle.putString(BUNDLE_COUNTRY_CODE, countryCode);
                bundle.putString(BUNDLE_PHONE_NUMBER, phoneNumber);
                bundle.putBoolean(BUNDLE_SMS_ENABLE, smsEnabled);
                bundle.putBoolean(BUNDLE_MMS_ENABLE, mmsEnabled);
                bundle.putBoolean(BUNDLE_VOICE_ENABLE, voiceEnabled);
                intent.putExtras(bundle);

                startActivityForResult(intent, REQUEST_AVAILABLE_PHONE_NUMBER);
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mSpinnerCountry.setSelection(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showAvailableCountries(List<AvailableCountry> availableCountries) {
        mCountryAdapter.replaceData(availableCountries);
    }

    @Override
    public void setPresenter(SearchPhoneNumberContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_AVAILABLE_PHONE_NUMBER && resultCode == Activity.RESULT_OK) {
            getActivity().setResult(Activity.RESULT_OK);
            getActivity().finish();
        }
    }
}
