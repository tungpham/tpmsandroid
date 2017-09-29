package com.ethan.morephone.presentation.contact.editor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.morephone.data.entity.BaseResponse;
import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.network.ApiMorePhone;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberContract;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;

import org.greenrobot.eventbus.EventBus;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by truongnguyen on 9/28/17.
 */

public class ContactEditorFragment extends BaseFragment implements ContactEditorContract.View {


    public static ContactEditorFragment getInstance(String phoneNumberId) {
        ContactEditorFragment contactEditorFragment = new ContactEditorFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, phoneNumberId);
        contactEditorFragment.setArguments(bundle);
        return contactEditorFragment;
    }

    private AppCompatEditText mEditTextName;
    private AppCompatEditText mEditTextPhoneNumber;
    private AppCompatEditText mEditTextEmail;
    private AppCompatEditText mEditTextAddress;
    private AppCompatEditText mEditTextRelationship;

    private String mPhoneNumberId;

    private ContactEditorContract.Presenter mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ContactEditorPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_creation, container, false);

        mEditTextName = (AppCompatEditText) view.findViewById(R.id.edit_text_display_name);
        mEditTextPhoneNumber = (AppCompatEditText) view.findViewById(R.id.edit_text_phone_number);
        mEditTextEmail = (AppCompatEditText) view.findViewById(R.id.edit_text_email);
        mEditTextAddress = (AppCompatEditText) view.findViewById(R.id.edit_text_address);
        mEditTextRelationship = (AppCompatEditText) view.findViewById(R.id.edit_text_relationship);

        BaseActivity baseActivity = (BaseActivity) getActivity();

        Toolbar mToolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        baseActivity.setTitleActionBar(mToolbar, getString(R.string.contact_creation_label));

        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact_creation, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                break;

            case R.id.menu_done:
                if (checkConditionContact()) {
                    Contact contact = new Contact(
                            "",
                            mEditTextName.getText().toString(),
                            mEditTextPhoneNumber.getText().toString(),
                            "",
                            mPhoneNumberId,
                            mEditTextAddress.getText().toString(),
                            mEditTextEmail.getText().toString(),
                            "",
                            mEditTextRelationship.getText().toString(),
                            "",
                            MyPreference.getUserId(getContext()));
                    mPresenter.createContact(getContext(), contact);
                } else {
                    Toast.makeText(getContext(), R.string.message_missing_info, Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
        return true;
    }

    private boolean checkConditionContact() {
        return !TextUtils.isEmpty(mEditTextName.getText().toString()) && !TextUtils.isEmpty(mEditTextPhoneNumber.getText().toString());
    }

    @Override
    public void setPresenter(ContactEditorContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isAdded()) {
            if (isActive) showProgress();
            else hideProgress();
        }
    }

    @Override
    public void createContactSuccess() {
        getActivity().finish();
    }

    @Override
    public void createContactFail() {
        Toast.makeText(getContext(), R.string.message_error_create_contact, Toast.LENGTH_SHORT).show();
    }
}
