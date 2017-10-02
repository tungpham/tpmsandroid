package com.ethan.morephone.presentation.contact;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.contact.Contact;
import com.android.morephone.data.entity.conversation.ConversationModel;
import com.android.morephone.data.log.DebugTool;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.contact.detail.ContactDetailActivity;
import com.ethan.morephone.presentation.contact.detail.QuickContactActivity;
import com.ethan.morephone.presentation.contact.editor.ContactEditorActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.widget.MultiSwipeRefreshLayout;
import com.ethan.morephone.widget.RecyclerViewFastScroller;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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

    public static ContactFragment getInstance(String phoneNumberId) {
        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DashboardActivity.BUNDLE_PHONE_NUMBER_ID, phoneNumberId);
        contactFragment.setArguments(bundle);
        return contactFragment;
    }

    private String mPhoneNumberId;

    private ContactAdapter mContactAdapter;
    private ContactContract.Presenter mPresenter;

    private MultiSwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new ContactPresenter(this);
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

        mPresenter.loadContact(getContext(), mPhoneNumberId);
        return view;
    }


    @Override
    public void onContactItemClick() {
        startActivity(new Intent(getActivity(), ContactDetailActivity.class));
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
            if(contact != null) {
                mContactAdapter.getData().add(contact);
                mContactAdapter.replaceData(mContactAdapter.getData());
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
