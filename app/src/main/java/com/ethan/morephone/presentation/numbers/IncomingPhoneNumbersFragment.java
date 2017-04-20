package com.ethan.morephone.presentation.numbers;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.phonenumbers.IncomingPhoneNumber;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.dashboard.DashboardFrag;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.numbers.adapter.IncomingPhoneNumbersAdapter;
import com.ethan.morephone.utils.Injection;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ethan on 3/16/17.
 */

public class IncomingPhoneNumbersFragment extends BaseFragment implements
        IncomingPhoneNumbersAdapter.OnItemNumberClickListener,
        NavigationView.OnNavigationItemSelectedListener,
        IncomingPhoneNumbersContract.View,
        DeletePhoneNumberDialog.DeletePhoneNumberListener {

    public static final String BUNDLE_PHONE_NUMBER = "BUNDLE_PHONE_NUMBER";


    public static IncomingPhoneNumbersFragment getInstance() {
        return new IncomingPhoneNumbersFragment();
    }

    private IncomingPhoneNumbersAdapter mIncomingPhoneNumbersAdapter;

    private IncomingPhoneNumbersContract.Presenter mPresenter;

    private DrawerLayout mDrawerLayout;

    private boolean mIsDelete;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new IncomingPhoneNumbersPresenter(this, Injection.providerUseCaseHandler(), Injection.providerDeleteIncomingPhoneNumber(getContext()));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_numbers, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.tool_bar);
        BaseActivity baseActivity = (BaseActivity) getActivity();
        String phoneNumber = MyPreference.getPhoneNumber(getContext());
//        if (TextUtils.isEmpty(phoneNumber)) {
//            phoneNumber = "";
//        }

        baseActivity.setTitleActionBar(toolbar, phoneNumber);
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

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if(mIsDelete){
                            getActivity().setResult(Activity.RESULT_OK);
                        }
                        getActivity().finish();
                        return true;
                    }
                }
                return false;
            }
        });

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
                if(mIsDelete){
                    getActivity().setResult(Activity.RESULT_OK);
                }
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
        MyPreference.setPhoneNumber(getContext(), incomingPhoneNumber.phoneNumber);
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void onItemDelete(int pos) {
        DeletePhoneNumberDialog deletePhoneNumberDialog = DeletePhoneNumberDialog.getInstance(pos);
        deletePhoneNumberDialog.show(getChildFragmentManager(), DeletePhoneNumberDialog.class.getSimpleName());
        deletePhoneNumberDialog.setDeletePhoneNumberListener(this);
//        getActivity().setResult(Activity.RESULT_OK);
//        getActivity().finish();
    }

    @Override
    public void onItemMessage(int pos) {
        IncomingPhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
        MyPreference.setPhoneNumber(getContext(), incomingPhoneNumber.phoneNumber);

        Intent intent = new Intent();
        intent.putExtra(DashboardFrag.BUNDLE_CHOOSE_VOICE, false);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onItemVoice(int pos) {
        IncomingPhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
        MyPreference.setPhoneNumber(getContext(), incomingPhoneNumber.phoneNumber);

        Intent intent = new Intent();
        intent.putExtra(DashboardFrag.BUNDLE_CHOOSE_VOICE, true);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
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

    @Override
    public void onDelete(int position) {
        IncomingPhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(position);
        mIncomingPhoneNumbersAdapter.getData().remove(incomingPhoneNumber);
        mIncomingPhoneNumbersAdapter.notifyDataSetChanged();
        mPresenter.deleteIncomingPhoneNumber(incomingPhoneNumber.sid);
        if (incomingPhoneNumber.phoneNumber.equals(MyPreference.getPhoneNumber(getContext()))) {
            MyPreference.setPhoneNumber(getContext(), "");
            mIsDelete = true;
        }
    }
}
