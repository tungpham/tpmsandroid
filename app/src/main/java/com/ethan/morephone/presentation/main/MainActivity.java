package com.ethan.morephone.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.android.morephone.data.entity.MessageItem;
import com.ethan.morephone.MyPreference;
import com.ethan.morephone.R;
import com.ethan.morephone.event.UpdateEvent;
import com.ethan.morephone.presentation.BaseActivity;
import com.ethan.morephone.presentation.main.adapter.ToolbarSpinnerAdapter;
import com.ethan.morephone.presentation.message.compose.ComposeActivity;
import com.ethan.morephone.widget.NavigationTabStrip;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by Ethan on 3/4/17.
 */

public class MainActivity extends BaseActivity implements
        SearchView.OnQueryTextListener,
        NavigationView.OnNavigationItemSelectedListener,
        View.OnClickListener {

    private Toolbar mToolbar;

    private ToolbarSpinnerAdapter mSpinnerAdapter;

    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        enableActionBar(mToolbar, "+18052284394");

        findViewById(R.id.button_new_compose).setOnClickListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setUpNavigation();
        setUpViewPager();
        setUpSpinnerToolbar();
    }

    private void setUpViewPager() {
        NavigationTabStrip navigationTabStrip = (NavigationTabStrip) findViewById(R.id.tab_strip);

        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        MyViewPagerAdapter myViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myViewPagerAdapter);
        navigationTabStrip.setViewPager(viewPager, 0);
    }

    private void setUpNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        invalidateItemNavigation();
    }

    private void setUpSpinnerToolbar() {
        View spinnerContainer = LayoutInflater.from(getApplicationContext()).inflate(R.layout.toolbar_spinner, mToolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        mToolbar.addView(spinnerContainer, lp);

        mSpinnerAdapter = new ToolbarSpinnerAdapter(getApplicationContext());
        mSpinnerAdapter.addItems(getSpinnerList());

        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(mSpinnerAdapter);

        String phoneNumber = MyPreference.getPhoneNumber(getApplicationContext());
        if (TextUtils.isEmpty(phoneNumber)) {
            MyPreference.setPhoneNumber(getApplicationContext(), mSpinnerAdapter.getItem(0));
        } else {
            spinner.setSelection(getIndex(spinner, phoneNumber));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                MyPreference.setPhoneNumber(getApplicationContext(), mSpinnerAdapter.getItem(i));
                EventBus.getDefault().post(new UpdateEvent(true));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                /* Nothing */
            }
        });
    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    private void invalidateItemNavigation() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (MyPreference.getInbox(getApplicationContext())) {
            navigationView.getMenu().findItem(R.id.nav_inbox).setChecked(true);
            navigationView.getMenu().findItem(R.id.nav_outbox).setChecked(false);
        } else {
            navigationView.getMenu().findItem(R.id.nav_inbox).setChecked(false);
            navigationView.getMenu().findItem(R.id.nav_outbox).setChecked(true);
        }
    }

    private List<String> getSpinnerList() {
        List<String> categories = new ArrayList<String>();
        categories.add("+18052284394");
        categories.add("+17606215500");
        return categories;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_inbox:
                MyPreference.setInbox(getApplicationContext(), true);
                EventBus.getDefault().post(new UpdateEvent(true));
                break;
            case R.id.nav_outbox:
                MyPreference.setInbox(getApplicationContext(), false);
                EventBus.getDefault().post(new UpdateEvent(true));
                break;
        }
        invalidateItemNavigation();
        mDrawerLayout.closeDrawers();

        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversation, menu);

        final MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        // Do something when collapsed
//                        mConversationListAdapter.setFilter(mConversationEntities);
                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        // Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
//        final List<MessageItem> filteredModelList = filter(mConversationEntities, newText);
//
//        mConversationListAdapter.setFilter(filteredModelList);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    private List<MessageItem> filter(List<MessageItem> models, String query) {
        query = query.toLowerCase();
        final List<MessageItem> filteredModelList = new ArrayList<>();
        for (MessageItem model : models) {
            final String text = model.body.toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_new_compose:
                startActivity(new Intent(this, ComposeActivity.class));
                break;
            default:
                break;
        }
    }

}
