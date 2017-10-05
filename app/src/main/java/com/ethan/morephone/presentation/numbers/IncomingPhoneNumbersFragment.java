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
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.android.morephone.data.entity.FakeData;
import com.android.morephone.data.entity.phonenumbers.PhoneNumber;
import com.ethan.morephone.R;
import com.ethan.morephone.presentation.BaseFragment;
import com.ethan.morephone.presentation.buy.SearchPhoneNumberActivity;
import com.ethan.morephone.presentation.buy.pool.PoolPhoneNumberActivity;
import com.ethan.morephone.presentation.dashboard.DashboardActivity;
import com.ethan.morephone.presentation.dashboard.DashboardFrag;
import com.ethan.morephone.presentation.main.MainActivity;
import com.ethan.morephone.presentation.main.OptionBuyPhoneNumberDialog;
import com.ethan.morephone.presentation.main.RequirePhoneNumberDialog;
import com.ethan.morephone.presentation.message.conversation.adapter.DividerSpacingItemDecoration;
import com.ethan.morephone.presentation.numbers.adapter.IncomingPhoneNumbersAdapter;
import com.ethan.morephone.presentation.numbers.adapter.IncomingPhoneNumbersViewHolder;
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
        DeletePhoneNumberDialog.DeletePhoneNumberListener,
        View.OnClickListener,
        RequirePhoneNumberDialog.RequirePhoneNumberListener,
        OptionBuyPhoneNumberDialog.OptionBuyPhoneNumberListener{

    public static final String BINDING_REGISTRATION = "BINDING_REGISTRATION";

    private static final String FCM_BINDING_TYPE = "fcm";

    public static final int REQUEST_DASHBOARD = 103;

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


        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerSpacingItemDecoration(getContext(), R.dimen.item_number_space));

        mIncomingPhoneNumbersAdapter = new IncomingPhoneNumbersAdapter(getContext(), new ArrayList<PhoneNumber>(), this);
        recyclerView.setAdapter(mIncomingPhoneNumbersAdapter);

        setHasOptionsMenu(true);

        mPresenter.loadIncomingPhoneNumbers(getContext());

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        if (mIsDelete) {
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

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_DASHBOARD && resultCode == Activity.RESULT_OK){
            mPresenter.loadIncomingPhoneNumbers(getContext());
        }
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
                if (mIsDelete) {
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
    public void onItemClick(IncomingPhoneNumbersViewHolder holder, int pos) {
        PhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
//        mIncomingPhoneNumbersAdapter.validateCurrentPhoneNumberSelected();
//        mIncomingPhoneNumbersAdapter.validatePhoneNumberSelected(holder, incomingPhoneNumber.phoneNumber);
        DashboardActivity.starter(getActivity(), incomingPhoneNumber, DashboardFrag.BUNDLE_FRAGMENT_MESSAGE);
    }

    @Override
    public void onItemDelete(int pos) {
        DeletePhoneNumberDialog deletePhoneNumberDialog = DeletePhoneNumberDialog.getInstance(pos);
        deletePhoneNumberDialog.show(getChildFragmentManager(), DeletePhoneNumberDialog.class.getSimpleName());
        deletePhoneNumberDialog.setDeletePhoneNumberListener(this);
    }

    @Override
    public void onItemMessage(IncomingPhoneNumbersViewHolder holder, int pos) {
        PhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
//        mIncomingPhoneNumbersAdapter.validateCurrentPhoneNumberSelected();
//        mIncomingPhoneNumbersAdapter.validatePhoneNumberSelected(holder, incomingPhoneNumber.phoneNumber);

        DashboardActivity.starter(getActivity(), incomingPhoneNumber, DashboardFrag.BUNDLE_FRAGMENT_MESSAGE);
    }

    @Override
    public void onItemVoice(IncomingPhoneNumbersViewHolder holder, int pos) {
        PhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
//        mIncomingPhoneNumbersAdapter.validateCurrentPhoneNumberSelected();
//        mIncomingPhoneNumbersAdapter.validatePhoneNumberSelected(holder, incomingPhoneNumber.phoneNumber);

        DashboardActivity.starter(getActivity(), incomingPhoneNumber, DashboardFrag.BUNDLE_FRAGMENT_RECORD);
    }

    @Override
    public void onItemDial(IncomingPhoneNumbersViewHolder holder, int pos) {
        PhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(pos);
//        mIncomingPhoneNumbersAdapter.validateCurrentPhoneNumberSelected();
//        mIncomingPhoneNumbersAdapter.validatePhoneNumberSelected(holder, incomingPhoneNumber.phoneNumber);
        DashboardActivity.starter(getActivity(), incomingPhoneNumber, DashboardFrag.BUNDLE_FRAGMENT_DIAL);

    }

    @Override
    public void showLoading(boolean isActive) {
        if(isAdded()) {
            if (isActive) showProgress();
            else hideProgress();
        }
    }

    @Override
    public void showPhoneNumbers(List<PhoneNumber> numberEntities) {
        if (isAdded()) {
            mIncomingPhoneNumbersAdapter.replaceData(numberEntities);
        }
    }

    @Override
    public void emptyPhoneNumber() {
        OptionBuyPhoneNumberDialog optionBuyPhoneNumberDialog = OptionBuyPhoneNumberDialog.getInstance();
        optionBuyPhoneNumberDialog.show(getChildFragmentManager(), OptionBuyPhoneNumberDialog.class.getSimpleName());
        optionBuyPhoneNumberDialog.setOptionPhoneNumberListener(this);

//        RequirePhoneNumberDialog requirePhoneNumberDialog = RequirePhoneNumberDialog.getInstance();
//        requirePhoneNumberDialog.show(getChildFragmentManager(), RequirePhoneNumberDialog.class.getSimpleName());
//        requirePhoneNumberDialog.setRequirePhoneNumberListener(this);
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
        PhoneNumber incomingPhoneNumber = mIncomingPhoneNumbersAdapter.getData().get(position);
        mIncomingPhoneNumbersAdapter.getData().remove(incomingPhoneNumber);
        mIncomingPhoneNumbersAdapter.notifyDataSetChanged();
        mPresenter.deleteIncomingPhoneNumber(getContext(), incomingPhoneNumber.getPhoneNumber(), incomingPhoneNumber.getSid(), incomingPhoneNumber.getId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_home:
                getActivity().finish();
                break;
            default:
                break;
        }
    }

//    @Override
//    public void onChoosePhone() {
////        startActivity(new Intent(this, IncomingPhoneNumbersActivity.class));
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        DebugTool.logD("requestCode FRAGMENT: " + requestCode);
//
//    }
    @Override
    public void onBuyPhone() {
        getActivity().startActivityForResult(new Intent(getActivity(), SearchPhoneNumberActivity.class), MainActivity.REQUEST_BUY_PHONE_NUMBER);
    }

    @Override
    public void onOptionBuyPhoneNumber(boolean pool) {
        if (pool) {
            startActivityForResult(new Intent(getActivity(), PoolPhoneNumberActivity.class), MainActivity.REQUEST_BUY_PHONE_NUMBER);
        } else {
            startActivityForResult(new Intent(getActivity(), SearchPhoneNumberActivity.class), MainActivity.REQUEST_BUY_PHONE_NUMBER);
        }
    }
}
