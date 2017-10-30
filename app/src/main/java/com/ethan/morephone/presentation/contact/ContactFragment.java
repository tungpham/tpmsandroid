package com.ethan.morephone.presentation.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.contact.detail.ContactDetailActivity;
import com.ethan.morephone.presentation.contact.editor.ContactEditorActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.utils.Injection;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;
import com.ethan.morephone.widget.RecyclerViewFastScroller;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by truongnguyen on 9/25/17.
 */

public class ContactFragment extends BaseFragment implements
        ContactAdapter.ContactItemClick,
        View.OnClickListener,
        ContactContract.View {

    public static final String EXTRA_CONTACT = "EXTRA_CONTACT";
    private final int REQUEST_CREATE_CONTACT = 100;
    private final int REQUEST_DETAIL_CONTACT = 101;

    private final String BUNDLE_SAVE_PHONE_NUMBER = "BUNDLE_SAVE_PHONE_NUMBER";
    private final String BUNDLE_SAVE_PHONE_NUMBER_ID = "BUNDLE_SAVE_PHONE_NUMBER_ID";
    private final String BUNDLE_SAVE_CONTACTS = "BUNDLE_SAVE_CONTACTS";


    public static ContactFragment getInstance(String phoneNumberId, String phoneNumber) {
        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, phoneNumberId);
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER, phoneNumber);
        contactFragment.setArguments(bundle);
        return contactFragment;
    }

    private String mPhoneNumberId;
    private String mPhoneNumber;

    private ContactAdapter mContactAdapter;
    private ContactContract.Presenter mPresenter;
    private Contact mContactItem;

    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ContactPresenter(this, Injection.providerUseCaseHandler(), Injection.providerGetContacts(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        mContactAdapter = new ContactAdapter(getContext(), new ArrayList<Contact>(), this);
        recyclerView.setAdapter(mContactAdapter);
        final RecyclerViewFastScroller fastScroller = (RecyclerViewFastScroller) view.findViewById(R.id.fastscroller);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false) {
            @Override
            public void onLayoutChildren(final RecyclerView.Recycler recycler, final RecyclerView.State state) {
                super.onLayoutChildren(recycler, state);
                //TODO if the items are filtered, considered hiding the fast scroller here
                final int firstVisibleItemPosition = findFirstVisibleItemPosition();
                if (firstVisibleItemPosition != 0) {
                    // this avoids trying to handle un-needed calls
                    if (firstVisibleItemPosition == -1)
                        //not initialized, or no items shown, so hide fast-scroller
                        fastScroller.setVisibility(View.GONE);
                    return;
                }
                final int lastVisibleItemPosition = findLastVisibleItemPosition();
                int itemsShown = lastVisibleItemPosition - firstVisibleItemPosition + 1;
                //if all items are shown, hide the fast-scroller
                fastScroller.setVisibility(mContactAdapter.getItemCount() > itemsShown ? View.VISIBLE : View.GONE);
            }
        });
        fastScroller.setRecyclerView(recyclerView);
        fastScroller.setViewsToUse(R.layout.recycler_view_fast_scroller__fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

        view.findViewById(R.id.button_create).setOnClickListener(this);

        mSwipeRefreshLayout = (MultiSwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

         /*-------------------Pull to request ----------------*/
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setColorSchemeResources(
                    R.color.refresh_progress_1,
                    R.color.refresh_progress_2,
                    R.color.refresh_progress_3);
            mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {

                            mContactAdapter.getData().clear();
                            mPresenter.loadContact(getContext(), mPhoneNumberId);
                        }
                    });

                }
            });
        }

        mPhoneNumberId = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID);
        mPhoneNumber = getArguments().getString(DashboardActivity.BUNDLE_PHONE_NUMBER);

       restoreInstanceState(savedInstanceState);
        return view;
    }

    private void restoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mPhoneNumber = savedInstanceState.getString(BUNDLE_SAVE_PHONE_NUMBER);
            mPhoneNumberId = savedInstanceState.getString(BUNDLE_SAVE_PHONE_NUMBER_ID);
            ArrayList<Contact> contacts = savedInstanceState.getParcelableArrayList(BUNDLE_SAVE_CONTACTS);
            if (contacts != null && !contacts.isEmpty()) {
                mContactAdapter.replaceData(contacts);
            } else {
                mPresenter.loadContact(getContext(), mPhoneNumberId);
            }
            DebugTool.logD("LOAD DATA FROM INSTANCE RECORD FRAGMENT");
        } else {
            mPresenter.loadContact(getContext(), mPhoneNumberId);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(BUNDLE_SAVE_PHONE_NUMBER, mPhoneNumber);
        outState.putString(BUNDLE_SAVE_PHONE_NUMBER_ID, mPhoneNumberId);
        outState.putParcelableArrayList(BUNDLE_SAVE_CONTACTS, new ArrayList<Parcelable>(mContactAdapter.getData()));
        super.onSaveInstanceState(outState);
    }



    @Override
    public void onContactItemClick(Contact contact) {
        mContactItem = contact;
        Intent intent = new Intent(getActivity(), ContactDetailActivity.class);
        intent.putExtra(EXTRA_CONTACT, mContactItem);
        intent.putExtra(DashboardActivity.BUNDLE_PHONE_NUMBER, mPhoneNumber);
        startActivityForResult(intent, REQUEST_DETAIL_CONTACT);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_create:
                Intent intent = new Intent(getActivity(), ContactEditorActivity.class);
                intent.putExtra(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, mPhoneNumberId);
                startActivityForResult(intent, REQUEST_CREATE_CONTACT);
                break;
            default:
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CREATE_CONTACT && resultCode == Activity.RESULT_OK) {
            Contact contact = data.getParcelableExtra(EXTRA_CONTACT);
            if (contact != null) {
                mContactAdapter.getData().add(contact);
                mContactAdapter.replaceData(mContactAdapter.getData());
            }
        } else if (requestCode == REQUEST_DETAIL_CONTACT && resultCode == Activity.RESULT_OK) {
            boolean isDelete = data.getBooleanExtra(ContactDetailActivity.EXTRA_DELETE_CONTACT, false);
            if (isDelete) {
                mContactAdapter.getData().remove(mContactItem);
                mContactAdapter.replaceData(mContactAdapter.getData());
            } else {
                Contact contact = data.getParcelableExtra(EXTRA_CONTACT);
                if (contact != null && mContactItem != null) {
                    mContactAdapter.getData().remove(mContactItem);
                    mContactAdapter.getData().add(contact);
                    mContactAdapter.replaceData(mContactAdapter.getData());
                }
            }
        }

    }

    @Override
    public void setPresenter(ContactContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isAdded()) {
            if (isActive) mSwipeRefreshLayout.setRefreshing(true);
            else mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void showAllContact(List<Contact> contacts) {
        if (isAdded()) {
            mContactAdapter.replaceData(contacts);
        }
    }
}
