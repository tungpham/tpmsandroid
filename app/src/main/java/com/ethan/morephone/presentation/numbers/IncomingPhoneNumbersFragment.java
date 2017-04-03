package com.ethan.morephone.presentation.numbers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.dashboard.DashboardFragment;
import com.ethan.morephone.presentation.message.conversation.ConversationsActivity;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.numbers.adapter.IncomingPhoneNumbersAdapter;
import com.ethan.morephone.presentation.phone.PhoneActivity;
import com.ethan.morephone.presentation.voice.VoiceActivity;
import com.ethan.morephone.presentation.voice.VoiceFragment;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersFragment extends BaseFragment implements
        IncomingPhoneNumbersAdapter.OnItemNumberClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        IncomingPhoneNumbersContract.View {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";

    public static IncomingPhoneNumbersFragment getInstance() {
        return new IncomingPhoneNumbersFragment();
    }

    private IncomingPhoneNumbersAdapter mIncomingPhoneNumbersAdapter;

    private IncomingPhoneNumbersContract.Presenter mPresenter;

    private DrawerLayout mDrawerLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IncomingPhoneNumbersPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numbers, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        baseActivity.setTitleActionBar(toolbar, "+17606215500");
//
//        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                getActivity(), mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        mDrawerLayout.setDrawerListener(toggle);
//        toggle.syncState();
//
//        setUpNavigation(view);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mIncomingPhoneNumbersAdapter = new IncomingPhoneNumbersAdapter(new ArrayList<IncomingPhoneNumber>(), this);
        recyclerView.setAdapter(mIncomingPhoneNumbersAdapter);

        mPresenter.loadIncomingPhoneNumbers(getContext());
        setHasOptionsMenu(true);

        return view;
    }

//    private void setUpNavigation(View view) {
//        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);
//    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_numbers, menu);
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
    public void onItemClick(int pos) {
        IncomingPhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
        Intent intent = new Intent(getActivity(), DashboardActivity.class);
        intent.putExtra(DashboardFragment.BUNDLE_PHONE_NUMBER, incomingPhoneNumber.phoneNumber);
        startActivity(intent);
    }

    @Override
    public void onItemCall(int pos) {
        IncomingPhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
        Intent intent = new Intent(getActivity(), PhoneActivity.class);
        intent.putExtra(PhoneActivity.EXTRA_PHONE_NUMBER, incomingPhoneNumber.phoneNumber);
        startActivity(intent);
    }

    @Override
    public void onItemMessage(int pos) {
        IncomingPhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
        Intent intent = new Intent(getActivity(), ConversationsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_PHONE_NUMBER, incomingPhoneNumber.phoneNumber);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void onItemVoice(int pos) {
        IncomingPhoneNumber numberEntity = mIncomingPhoneNumbersAdapter.getData().get(pos);
        Intent intent = new Intent(getActivity(), VoiceActivity.class);
        intent.putExtra(VoiceFragment.BUNDLE_PHONE_NUMBER, numberEntity.phoneNumber);
        startActivity(intent);
    }

    @Override
    public void showLoading(boolean isActive) {
        if (isActive) showProgress();
        else hideProgress();
    }

    @Override
    public void showPhoneNumbers(List<IncomingPhoneNumber> numberEntities) {
        mIncomingPhoneNumbersAdapter.replaceData(numberEntities);
    }

    @Override
    public void showFakeData(FakeData fakeData) {
        EventBus.getDefault().postSticky(fakeData);
    }

    @Override
    public void setPresenter(IncomingPhoneNumbersContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        mDrawerLayout.closeDrawers();
        return false;
    }
}
